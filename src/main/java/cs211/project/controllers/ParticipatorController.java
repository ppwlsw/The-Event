package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.collections.EventList;
import cs211.project.services.DataSource;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParticipatorController {
    @FXML
    DatePicker openDatePicker;
    @FXML
    DatePicker closeDatePicker;
    @FXML
    TextField openTimeTextField;
    @FXML
    TextField closeTimeTextField;
    @FXML
    TextField maxParticipatorTextField;
    @FXML
    Button confirmButton;
    @FXML
    ImageView eventImageView;



    private DataSource<EventList> datasource;
    private EventList eventList;
    private Event currentEvent;
    String eventId;
    String eventImagePath;

    @FXML
    public void initialize() {
        datasource = new EventListDataSource("data", "event-list.csv");
        eventList = datasource.readData();
        eventId = (String) FXRouter.getData();
        currentEvent = eventList.findEventById(eventId);
        eventImageView.setImage(new Image("file:" + currentEvent.getEventImagePath()));
        maxParticipatorTextField.setText(String.valueOf(currentEvent.getMaxParticipants()));
        openDatePicker.setPromptText(currentEvent.getDateOpenJoin().split(" ")[0]);
        closeDatePicker.setPromptText(currentEvent.getDateCloseJoin().split(" ")[0]);
        openTimeTextField.setText(currentEvent.getDateOpenJoin().split(" ")[1]);
        closeTimeTextField.setText(currentEvent.getDateCloseJoin().split(" ")[1]);

    }

    @FXML
    public void onConfirmButtonClick() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        String openDate = openDatePicker.getValue() != null ? setDatePickerFormat(openDatePicker, openTimeTextField.getText()) : currentEvent.getDateStart();
        String closeDate = closeDatePicker.getValue() != null ? setDatePickerFormat(closeDatePicker, closeTimeTextField.getText()) : currentEvent.getDateEnd();

        int newMaxParticipants = Integer.parseInt(maxParticipatorTextField.getText());
        if (openDate.compareTo(closeDate) < 0 && currentTime.compareTo(openDate) < 0 && newMaxParticipants > 0 && Regex.checkTimeFormat(openTimeTextField.getText()) && Regex.checkTimeFormat(closeTimeTextField.getText())) {
            currentEvent.setMaxParticipants(newMaxParticipants);
            currentEvent.setDateOpenJoin(openDate);
            currentEvent.setDateCloseJoin(closeDate);
            datasource.writeData(eventList);
            try {
                FXRouter.goTo("participator", eventId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Can't change data");
            alert.setHeaderText("Change Data Error!");
            alert.showAndWait();
        }

    }

    private String setDatePickerFormat(DatePicker datePicker, String time) {
        if (datePicker != null) {
            LocalDate date = datePicker.getValue();
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time;
            return formattedDate;
        } else {
            return null;
        }
    }

    @FXML
    public void onChangeImageButtonClick(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null) {
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_" + System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath() + System.getProperty("file.separator") + filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                eventImageView.setImage(new Image(target.toUri().toString()));
                eventImagePath = destDir + "/" + filename;
                eventList.changeEventImageById(currentEvent.getId(), eventImagePath);
                datasource.writeData(eventList);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void onBackToMyEventPageButton() {
        try {
            FXRouter.goTo("my-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onScheduleCilck() {
        try {
            FXRouter.goTo("edit-schedule", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onVolunteerButtonCLick() {
        try {
            FXRouter.goTo("create-team", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onParticipatorButtonClick() {
        try {
            FXRouter.goTo("participator", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onDetailButtonClick() {
        try {
            FXRouter.goTo("manage-event", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void goToShowMemberPage() {
        try {
            FXRouter.goTo("show-member", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
