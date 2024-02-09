package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class MainPageController {
    @FXML
    private Circle userImageCircle;
    @FXML
    private GridPane eventContainer;
    @FXML
    private TextField searchTextField;
    @FXML
    private ScrollPane scrollPane;

    private User currentUser;
    private EventListDataSource dataSource;
    private EventList eventList;
    private HashMap<String, Object> data;
    private int column = 0;
    private int row = 1;

    @FXML
    public void initialize() {
        data = new HashMap<>();
        currentUser = (User) FXRouter.getData();
        data.put("user", currentUser);
        dataSource = new EventListDataSource("data", "event-list.csv");
        eventList = dataSource.readData();
        showDetail(currentUser);
        showEvent(eventList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchFilter(newValue));
    }

    private void showDetail(User currentUser) {
        userImageCircle.setFill(new ImagePattern(new Image("file:" + currentUser.getUserImageResource())));
    }

    private void showEvent(EventList eventList) {
        for (Event event : eventList.getEvents()) {
            if (event.getDateEnd().compareTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) > 0)
                addEventCard(event);
        }
    }

    private void addEventCard(Event event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cs211/project/views/event-card.fxml"));
            VBox cardBox = fxmlLoader.load();
            EventCardController cardController = fxmlLoader.getController();
            cardController.setData(event);

            cardBox.setOnMouseClicked(activity -> {
                try {
                    data.put("event", event);
                    FXRouter.goTo("join-event", data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            if (column == 4) {
                column = 0;
                row++;
            }
            eventContainer.add(cardBox, column++, row);
            GridPane.setMargin(cardBox, new Insets(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchFilter(String keyword) {
        keyword = keyword.toLowerCase().trim();
        eventContainer.getChildren().clear();
        row = 1;
        column = 0;

        if (keyword.isEmpty()) {
            showEvent(eventList);
        } else {
            for (Event event : eventList.getEvents()) {
                if (event.getEventName().toLowerCase().contains(keyword)) {
                    if (event.getDateEnd().compareTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) > 0)
                        addEventCard(event);
                }
            }
        }
    }


    @FXML
    public void onUserHubClick() {
        try {
            FXRouter.goTo("user-hub", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}