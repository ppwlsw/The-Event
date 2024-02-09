package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.services.DataSource;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.Regex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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

public class CreateEventController {
    @FXML TextField eventNameTextField;
    @FXML TextField eventDescriptionTextField;
    @FXML TextField startTime;
    @FXML TextField endTime;
    @FXML TextField maxParticipants;
    @FXML ImageView eventImageView;
    @FXML DatePicker pickStartDate;
    @FXML DatePicker pickEndDate;

    private EventList eventList;
    private User currentUser;
    private DataSource<EventList> dataSource;

    private Event event;
    private String imagePath;



    @FXML
    public void initialize() {
        currentUser = (User) FXRouter.getData();
        dataSource = new EventListDataSource("data", "event-list.csv");
        eventList = dataSource.readData();
        imagePath = "images/default-event-image.jpg";
        eventImageView.setImage(new Image("file:" + imagePath, true));
    }

    @FXML
    public void backToHub() {
        try {
            FXRouter.goTo("user-hub", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate(DatePicker datePicker, String time) {
        LocalDate date = datePicker.getValue();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time;
        return formattedDate;
    }

    @FXML
    public void onCreateEventClick() {
        try {
            int check = 1;
            String name = eventNameTextField.getText().trim();
            String maxpaci = maxParticipants.getText().trim();
            String stime = startTime.getText().trim();
            String etime = endTime.getText().trim();
            if (!Regex.checkNumberFormat(maxpaci) || !Regex.checkTimeFormat(stime) || !Regex.checkTimeFormat(etime)) {
                check = 0;
            }
            String description = eventDescriptionTextField.getText().trim();
            String startDate = getDate(pickStartDate, stime);
            String endDate = getDate(pickEndDate, etime);
            String userId = currentUser.getUserId();
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
            if (startDate.compareTo(endDate) > 0 || currentTime.compareTo(startDate) > 0) {
                check = 0;
            }
            Event exist = eventList.findEventByEventName(name);

            if (exist == null && check == 1) {
                int maxpaciNumber = Integer.parseInt(maxpaci);
                eventList.addNewEvent(name, description, startDate, endDate, imagePath , userId, maxpaciNumber);
                dataSource.writeData(eventList);
                FXRouter.goTo("user-hub", currentUser);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Can't create event");
                alert.setHeaderText("Create Event Error!");
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void uploadEventImageButton(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                eventImageView.setImage(new Image(target.toUri().toString()));
                imagePath = destDir + "/" + filename;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
