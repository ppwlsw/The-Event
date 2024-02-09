package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.EventSchedule;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.EventScheduleList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.EventScheduleListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
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

public class CreateScheduleController {
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private TextField activityNameTextField;
    @FXML private TextArea detailTextArea;
    @FXML ImageView eventImageView;

    private String eventId;
    private EventScheduleListDataSource dataSource;
    private EventListDataSource eventListDataSource;
    private EventScheduleList eventScheduleList;
    private EventList eventList;
    private Event currentEvent;
    String eventImagePath;
    @FXML
    public void initialize() {
        eventId = (String) FXRouter.getData();
        dataSource = new EventScheduleListDataSource("data", "schedule-list.csv");
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventScheduleList = dataSource.readData();
        eventList = eventListDataSource.readData();
        currentEvent = eventList.findEventById(eventId);
        eventImageView.setImage(new Image("file:" + currentEvent.getEventImagePath()));
    }

    private String getDate(DatePicker datePicker, String time) {
        LocalDate date = datePicker.getValue();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time;
        return formattedDate;
    }
    @FXML
    public void onBackButton() {
        try {
            FXRouter.goTo("edit-schedule", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onCreateScheduleButton() {
        String startTime = startTimeTextField.getText().trim();
        String endTime = endTimeTextField.getText().trim();
        if (Regex.checkTimeFormat(startTime) && Regex.checkTimeFormat(endTime)) {
            String startDateTime = getDate(startDatePicker, startTime);
            String endDateTime = getDate(endDatePicker, endTime);
            String name = activityNameTextField.getText().trim();
            String detail = detailTextArea.getText().trim();
            EventSchedule exist = eventScheduleList.findScheduleByEventIdAndName(eventId, name);
            Event thisEvent = eventList.findEventById(eventId);
            if (exist == null && !name.equals("") && !detail.equals("") &&
                    startDateTime.compareTo(endDateTime) <= 0 && startDateTime.compareTo(thisEvent.getDateStart()) >= 0 &&
                    startDateTime.compareTo(thisEvent.getDateEnd()) <= 0 && endDateTime.compareTo(thisEvent.getDateEnd()) <= 0) {
                eventScheduleList.addNewEventSchedule(eventId, name, detail, startDateTime, endDateTime);
                dataSource.writeData(eventScheduleList);
                try {
                    FXRouter.goTo("edit-schedule", eventId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
                eventListDataSource.writeData(eventList);
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
