package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Participant;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.ParticipantList;
import cs211.project.models.collections.UserList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.ParticipantListDataSource;
import cs211.project.services.UserListFileDataSource;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class EventMemberController {
    @FXML
    ImageView eventImageView;
    @FXML
    TableView memberTableView;
    private String eventImagePath;
    private EventListDataSource eventListDataSource;
    private EventList eventList;
    private Event currentEvent;
    private ParticipantListDataSource participantListDataSource;
    private ParticipantList participantList;
    private UserListFileDataSource userListFileDataSource;
    private UserList userList;

    private String eventId;
    private Participant selectedParticipant;
    public void initialize() {
        eventId = (String) FXRouter.getData();
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventList = eventListDataSource.readData();
        currentEvent = eventList.findEventById(eventId);
        eventImagePath = currentEvent.getEventImagePath();
        eventImageView.setImage(new Image("file:" + eventImagePath));
        participantListDataSource = new ParticipantListDataSource("data", "participant-list.csv");
        participantList = participantListDataSource.readData();
        userListFileDataSource = new UserListFileDataSource("data", "user-list.csv");
        userList = userListFileDataSource.readData();
        showTable(participantList);

        memberTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Participant>() {
            @Override
            public void changed(ObservableValue<? extends Participant> observableValue, Participant oldValue, Participant newValue) {
                if (newValue != null) {
                    selectedParticipant = newValue;
                } else {
                    selectedParticipant = null;
                }
            }
        });

    }
    private void showTable(ParticipantList participantList) {
        TableColumn<Participant, String> nameColumn = new TableColumn<>("name");
        nameColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            User user = userList.findUserByID(participant.getUserId());
            return new ReadOnlyStringWrapper(user.getUsername());
        });
        TableColumn<Participant, String> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            return new ReadOnlyStringWrapper(participant.getBanStatus() == 0 ? "active": "banned");
        });
        memberTableView.getColumns().clear();
        memberTableView.getColumns().add(nameColumn);
        memberTableView.getColumns().add(statusColumn);
        memberTableView.getItems().clear();

        for (Participant participant: participantList.getParticipants()) {
            if(participant.getEventId().equals(eventId)) {
                memberTableView.getItems().add(participant);
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
                eventList.changeEventImageById(eventId, eventImagePath);
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
    @FXML
    public void onClickBannedButton() {
        if(selectedParticipant != null) {
            participantList.setBannedStatusParticipantByUserIdAndEventId(selectedParticipant.getUserId(), eventId, 1);
            participantListDataSource.writeData(participantList);
            eventList.updateParticipant(participantList);
            eventListDataSource.writeData(eventList);
            showTable(participantList);
        }
    }
    @FXML
    public void onClickUnbannedButton() {
        if(selectedParticipant != null) {
            participantList.setBannedStatusParticipantByUserIdAndEventId(selectedParticipant.getUserId(), eventId, 0);
            participantListDataSource.writeData(participantList);
            eventList.updateParticipant(participantList);
            eventListDataSource.writeData(eventList);
            showTable(participantList);
        }
    }
    @FXML
    public void goToParticipator() {
        onParticipatorButtonClick();
    }
}
