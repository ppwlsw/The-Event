package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class MyEventController {
    @FXML
    TableView myEventTableView;
    private EventList eventList;
    private User currentUser;
    @FXML
    public void initialize() {
        EventListDataSource dataSource = new EventListDataSource("data", "event-list.csv");
        eventList = dataSource.readData();
        currentUser = (User) FXRouter.getData();
        showTable(eventList);

        myEventTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue observableValue, Event oldValue , Event newValue) {
                if(newValue != null){
                    try {
                        FXRouter.goTo("manage-event", newValue.getId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


        });
    }
    
    @FXML
    public void BackToUserHub() {
        try {
            FXRouter.goTo("user-hub", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showTable(EventList eventList) {
        // name column
        TableColumn<Event, String> nameColumn = new TableColumn<>("Event Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        // date start column
        TableColumn<Event, String> dateStartColumn = new TableColumn<>("Starting Date");
        dateStartColumn.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        // date end column
        TableColumn<Event, String> dateEndColumn = new TableColumn<>("Ending Date");
        dateEndColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        // participants column
        TableColumn<Event, Integer> participantColumn = new TableColumn<>("Participants");
        participantColumn.setCellValueFactory(new PropertyValueFactory<>("participants"));
        // max participants column
        TableColumn<Event, Integer> maxParticipantColumn = new TableColumn<>("Max Participants");
        maxParticipantColumn.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        // images column
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

        myEventTableView.getColumns().clear();
        myEventTableView.getColumns().add(imageColumn);
        myEventTableView.getColumns().add(nameColumn);
        myEventTableView.getColumns().add(dateStartColumn);
        myEventTableView.getColumns().add(dateEndColumn);
        myEventTableView.getColumns().add(participantColumn);
        myEventTableView.getColumns().add(maxParticipantColumn);
        myEventTableView.getItems().clear();

        for (Event event: eventList.getEvents()) {
            if (event.getUserId().equals(currentUser.getUserId())) {
                myEventTableView.getItems().add(event);
            }
        }
    }
    
    
    
    
    
    
}
