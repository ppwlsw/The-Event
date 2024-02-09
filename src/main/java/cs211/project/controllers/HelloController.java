package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class HelloController {
    @FXML private Rectangle rec;
    @FXML
    public void initialize() {
        rec.setArcHeight(40);
        rec.setArcWidth(40);

    }


    @FXML
    public void goToGuidePage() {
        try {
            FXRouter.goTo("guide");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void goToCreditPage() {
        try {
            FXRouter.goTo("credit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }@FXML
    public void goToLoginPage() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            System.err.println("Can't go to login");
            throw new RuntimeException(e);
        }
    }@FXML
    public void goToRegisterPage() {
        try {
            FXRouter.goTo("register");
        } catch (IOException e) {
            System.out.println("can't go to register");
            throw new RuntimeException(e);
        }
    }
}