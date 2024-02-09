package cs211.project.controllers;

import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.awt.*;
public class GuideController {

    @FXML
    ImageView guideImageView;
    public void initialize() {
        Image guideImage = new Image(getClass().getResource("/images/guide.png").toString());
        guideImageView.setImage(guideImage);
    }
    @FXML
    public void goToHelloPage() {
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openGuideBookPdf() {
        try {
            File pdfFile = new File("tri_o_guidebook.pdf"); // Replace with the actual path to your PDF file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.err.println("Desktop not supported, unable to open PDF.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening PDF");
        }
    }
}
