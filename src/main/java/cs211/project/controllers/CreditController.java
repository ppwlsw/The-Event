package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class CreditController {
    @FXML ImageView henryImageView;
    @FXML ImageView tleImageView;
    @FXML ImageView toonImageView;
    @FXML ImageView tentImageView;

    @FXML
    public void initialize() {
        Image henryImage = new Image(getClass().getResource("/images/henry.jpg").toString());
        Image tleImage = new Image(getClass().getResource("/images/tle.jpg").toString());
        Image toonImage = new Image(getClass().getResource("/images/toon.jpg").toString());
        Image tentImage = new Image(getClass().getResource("/images/tent.png").toString());
        henryImageView.setImage(henryImage);
        tleImageView.setImage(tleImage);
        toonImageView.setImage(toonImage);
        tentImageView.setImage(tentImage);
    }
    @FXML
    public void goToHelloPage() {
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
