package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Participant;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.ParticipantList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.ParticipantListDataSource;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEventController {
    @FXML
    TableView inProgessEventTableView;
    @FXML
    TableView endedEventTableView;

    private ParticipantListDataSource participantListDataSource;
    private ParticipantList participantList;
    private EventList eventList;
    private User currentUser;
    @FXML
    public void initialize() {
        EventListDataSource dataSource = new EventListDataSource("data", "event-list.csv");
        participantListDataSource = new ParticipantListDataSource("data", "participant-list.csv");
        participantList = participantListDataSource.readData();
        eventList = dataSource.readData();
        currentUser = (User) FXRouter.getData();
        showTable(eventList);
    }

    private void showTable(EventList eventList) {
        TableColumn<Event, String> nameColumn = new TableColumn<>("Event Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        TableColumn<Event, String> nameEndedColumn = new TableColumn<>("Event Name");
        nameEndedColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));

        TableColumn<Event, String> dateStartColumn = new TableColumn<>("Starting Date");
        dateStartColumn.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        TableColumn<Event, String> dateStartEndedColumn = new TableColumn<>("Starting Date");
        dateStartEndedColumn.setCellValueFactory(new PropertyValueFactory<>("dateStart"));

        TableColumn<Event, String> dateEndColumn = new TableColumn<>("Ending Date");
        dateEndColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        TableColumn<Event, String> dateEndEndedColumn = new TableColumn<>("Ending Date");
        dateEndEndedColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));

        TableColumn<Event, String> participantColumn = new TableColumn<>("Participants");
        participantColumn.setCellValueFactory(new PropertyValueFactory<>("participants"));
        TableColumn<Event, String> participantEndedColumn = new TableColumn<>("Participants");
        participantEndedColumn.setCellValueFactory(new PropertyValueFactory<>("participants"));

        TableColumn<Event, String> maxParticipantColumn = new TableColumn<>("Max Participants");
        maxParticipantColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        TableColumn<Event, String> maxParticipantEndedColumn = new TableColumn<>("Max Participants");
        maxParticipantEndedColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));

        TableColumn<Event, String> imageColumn = new TableColumn<>("Event Image");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("eventImagePath"));

        imageColumn.setCellFactory(column -> {
            return new TableCell<Event, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);
                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        Image image = new Image("file:" + imagePath, true);
                        imageView.setImage(image);
                        imageView.setFitWidth(75);
                        imageView.setFitHeight(75);
                        setGraphic(imageView);
                    }
                }
            };
        });
        TableColumn<Event, String> imageEndedColumn = new TableColumn<>("Event Image");
        imageEndedColumn.setCellValueFactory(new PropertyValueFactory<>("eventImagePath"));

        imageEndedColumn.setCellFactory(column -> {
            return new TableCell<Event, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);
                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        Image image = new Image("file:" + imagePath, true);
                        imageView.setImage(image);
                        imageView.setFitWidth(75);
                        imageView.setFitHeight(75);
                        setGraphic(imageView);
                    }
                }
            };
        });
        inProgessEventTableView.getColumns().clear();
        inProgessEventTableView.getColumns().add(imageColumn);
        inProgessEventTableView.getColumns().add(nameColumn);
        inProgessEventTableView.getColumns().add(dateStartColumn);
        inProgessEventTableView.getColumns().add(dateEndColumn);
        inProgessEventTableView.getColumns().add(participantColumn);
        inProgessEventTableView.getColumns().add(maxParticipantColumn);
        inProgessEventTableView.getItems().clear();

        endedEventTableView.getColumns().clear();
        endedEventTableView.getColumns().add(imageEndedColumn);
        endedEventTableView.getColumns().add(nameEndedColumn);
        endedEventTableView.getColumns().add(dateStartEndedColumn);
        endedEventTableView.getColumns().add(dateEndEndedColumn);
        endedEventTableView.getColumns().add(participantEndedColumn);
        endedEventTableView.getColumns().add(maxParticipantEndedColumn);
        endedEventTableView.getItems().clear();


        for (Event event: eventList.getEvents()) {
            for (Participant participant: participantList.getParticipants()) {
                if (participant.getUserId().equals(currentUser.getUserId()) && event.getId().equals(participant.getEventId())) {
                    String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    if (currentTime.compareTo(event.getDateEnd()) > 0) {
                        endedEventTableView.getItems().add(event);
                    } else {
                        inProgessEventTableView.getItems().add(event);
                    }
                }
            }
        }
    }


    @FXML
    public void goToUserHubPage() {
        try {
            FXRouter.goTo("user-hub", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
