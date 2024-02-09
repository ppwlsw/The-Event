package cs211.project.controllers;

import cs211.project.models.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class EventCardController {
    @FXML
    Rectangle rectangleView;
    @FXML
    Label nameLabel;
    @FXML
    Label spaceLabel;
    Event currentEvent;
    public boolean setData(Event event) {
        currentEvent = event;
        rectangleView.setFill(new ImagePattern(new Image("file:"+event.getEventImagePath())));
        nameLabel.setText(event.getEventName());
        if (event.getMaxParticipants() > event.getParticipants()) {
            int max = event.getMaxParticipants();
            int join = event.getParticipants();
            String space = "Avaliable " + (max-join);
            spaceLabel.setText(space);
        }
        else {
            spaceLabel.setText("full");
            return false;
        }
        return true;
    }
    public Event getData() {
        return currentEvent;
    }
}
