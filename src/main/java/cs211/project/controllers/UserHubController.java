package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class UserHubController {
    @FXML Button goCreatePageButton ;
    @FXML Button goYourEventButton ;
    @FXML Button goAccountPageButton ;
    @FXML Button goHistoryPageButton ;
    @FXML Button goUserPageButton ;
    @FXML
    Circle circle;
    private User currentUser;
    String userImagePath;
    @FXML
    Image image;
    @FXML
    public void initialize() {
        currentUser = (User) FXRouter.getData();
        userImagePath = "file:" + currentUser.getUserImageResource();
        image = new Image(userImagePath);
        circle.setFill(new ImagePattern(image));

    }

    @FXML
    public void onCreateEventClick(){
        try {
            FXRouter.goTo("create-event", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onYourEventClick(){
        try {
            FXRouter.goTo("my-event", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onAccountSettingClick(){
        try {
            FXRouter.goTo("account-setting", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onHistoryEventClick(){
        try {
            FXRouter.goTo("history-event", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onUserPageButtonClick(){
        try {
            FXRouter.goTo("show-event", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onHelloPageButtonClick(){
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


