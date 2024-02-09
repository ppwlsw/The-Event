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
import java.time.format.DateTimeFormatter;

public class ManageEventController {
    @FXML
    TextField eventIdTextField;
    @FXML
    TextField eventNameTextField;
    @FXML
    TextField eventMaxParticipateTextField;
    @FXML
    TextField eventDescriptionTextField;
    @FXML
    DatePicker eventStartDatePicker;
    @FXML
    DatePicker eventEndDatePicker;
    @FXML
    TextField eventStartTimeTextField;
    @FXML
    TextField eventEndTimeTextField;
    @FXML
    ImageView eventImageView;
    @FXML
    Button saveEditButton;
    @FXML
    Button editEventButton;

    private DataSource<EventList> datasource;
    private boolean isEditable = false;
    private EventList eventList;
    private Event currentEvent;
    String eventImagePath;
    String eventId;

    @FXML
    public void initialize() {
        saveEditButton.setVisible(false);

        datasource = new EventListDataSource("data", "event-list.csv");
        eventList = datasource.readData();
        eventId = (String) FXRouter.getData();
        currentEvent = eventList.findEventById(eventId);


        eventIdTextField.setEditable(false);
        eventNameTextField.setEditable(isEditable);
        eventDescriptionTextField.setEditable(isEditable);
        eventMaxParticipateTextField.setEditable(isEditable);
        eventStartDatePicker.setEditable(isEditable);
        eventEndDatePicker.setEditable(isEditable);
        eventStartTimeTextField.setEditable(isEditable);
        eventEndTimeTextField.setEditable(isEditable);

        eventImageView.setImage(new Image("file:" + currentEvent.getEventImagePath(), true));
        eventIdTextField.setText(currentEvent.getId());
        eventNameTextField.setText(currentEvent.getEventName());
        eventDescriptionTextField.setText(currentEvent.getDescription());
        eventMaxParticipateTextField.setText(String.valueOf(currentEvent.getMaxParticipants()));
        eventStartDatePicker.setPromptText(currentEvent.getDateStart().split(" ")[0]);
        eventEndDatePicker.setPromptText(currentEvent.getDateEnd().split(" ")[0]);
        eventStartTimeTextField.setText(currentEvent.getDateStart().split(" ")[1]);
        eventEndTimeTextField.setText(currentEvent.getDateEnd().split(" ")[1]);


    }

    @FXML
    public void onEditEventButtonClick() {
        saveEditButton.setVisible(true);
        editEventButton.setVisible(false);
        isEditable = !isEditable;
        eventNameTextField.setEditable(isEditable);
        eventDescriptionTextField.setEditable(isEditable);
        eventMaxParticipateTextField.setEditable(isEditable);
        eventStartDatePicker.setEditable(isEditable);
        eventEndDatePicker.setEditable(isEditable);
        eventStartTimeTextField.setEditable(isEditable);
        eventEndTimeTextField.setEditable(isEditable);
    }

    @FXML
    public void onSaveEditButtonClick() {
        saveEditButton.setVisible(false);
        editEventButton.setVisible(true);
        isEditable = !isEditable;
        eventNameTextField.setEditable(isEditable);
        eventDescriptionTextField.setEditable(isEditable);
        eventMaxParticipateTextField.setEditable(isEditable);
        eventStartDatePicker.setEditable(isEditable);
        eventEndDatePicker.setEditable(isEditable);
        eventStartTimeTextField.setEditable(isEditable);
        eventEndTimeTextField.setEditable(isEditable);

        String eventName = eventNameTextField.getText().trim();
        String eventDescription = eventDescriptionTextField.getText().trim();
        String eventMaxParticipate = eventMaxParticipateTextField.getText().trim();
        String eventStartTime = eventStartTimeTextField.getText().trim();
        String eventEndTime = eventEndTimeTextField.getText().trim();

        int check = 1;

        if (!Regex.checkNumberFormat(eventMaxParticipate) || !Regex.checkTimeFormat(eventStartTime) || !Regex.checkTimeFormat(eventEndTime)) {
            check = 0;
        }

        if (check != 0) {

            String eventStartDate = eventStartDatePicker.getValue() != null ? getDatePickerData(eventStartDatePicker, eventStartTime) : currentEvent.getDateStart();
            String eventEndDate = eventEndDatePicker.getValue() != null ? getDatePickerData(eventEndDatePicker, eventEndTime) : currentEvent.getDateEnd();


            eventList.changeMaxParticipatorById(currentEvent.getId(), Integer.parseInt(eventMaxParticipate));
            eventList.changeEventNameById(currentEvent.getId(), eventName);
            eventList.changeEventDescriptionById(currentEvent.getId(), eventDescription);
            eventList.changeEventStartDateTimeById(currentEvent.getId(), eventStartDate);
            eventList.changeEventEndDateTimeById(currentEvent.getId(), eventEndDate);

            datasource.writeData(eventList);

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Can't edit event");
            alert.setHeaderText("Edit Event Error!");
            alert.showAndWait();

        }

    }

    public String getDatePickerData(DatePicker datePicker, String time) {
        if (datePicker != null) {
            LocalDate date = datePicker.getValue();
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time;
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
    public void onVolunteerButtonCLick(){
        try{
            FXRouter.goTo("create-team", eventId);
        } catch (IOException e){
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
            FXRouter.goTo("manage-event" , eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
