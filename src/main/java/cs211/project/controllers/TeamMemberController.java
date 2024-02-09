package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.User;
import cs211.project.models.Volunteer;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.TeamList;
import cs211.project.models.collections.UserList;
import cs211.project.models.collections.VolunteerList;
import cs211.project.services.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class TeamMemberController {
    TeamListDataSource teamListDataSource;
    TeamList teamList;
    Team currentTeam;
    UserListFileDataSource userListFileDataSource;
    UserList userList;
    EventListDataSource eventListDataSource;
    VolunteerListDataSource volunteerListDataSource;
    VolunteerList volunteerList;
    Volunteer volunteer;
    EventList eventList;
    Event event;
    String eventImagePath;
    String eventId;
    @FXML Label teamNameLabel;
    @FXML
    ImageView eventImageView;
    @FXML
    TableView teamMemberTableView;
    @FXML Button unbanBtn;
    @FXML Button banBtn;
    @FXML Button leaderBtn;
    @FXML Button unLeaderBtn;


    public void initialize(){
        clearButton();
        currentTeam = (Team) FXRouter.getData();
        teamNameLabel.setText(currentTeam.getTeamName());
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventList = eventListDataSource.readData();
        volunteerListDataSource = new VolunteerListDataSource("data", "volunteer-list.csv");
        volunteerList = volunteerListDataSource.readData();
        userListFileDataSource = new UserListFileDataSource("data", "user-list.csv");
        userList = userListFileDataSource.readData();
        teamListDataSource = new TeamListDataSource("data","team-list.csv");
        teamList = teamListDataSource.readData();
        eventId = currentTeam.getEventId();
        event = eventList.findEventById(eventId);
        eventImagePath = event.getEventImagePath();
        eventImageView.setImage(new Image("file:" + eventImagePath));
        showTable(volunteerList);

        teamMemberTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Volunteer>() {
            @Override
            public void changed(ObservableValue<? extends Volunteer> observable, Volunteer oldValue, Volunteer newValue) {
                if (newValue == null) {
                    volunteer = null;
               } else {
                    volunteer = newValue;
                    if (volunteer.getBanStatus() == 0) {
                        banBtn.setVisible(true);
                        unbanBtn.setVisible(false);
                        if (volunteer.isLeaderStatus()) {
                            leaderBtn.setVisible(false);
                            unLeaderBtn.setVisible(true);
                        } else {
                            leaderBtn.setVisible(true);
                            unLeaderBtn.setVisible(false);
                        }
                    } else {
                        banBtn.setVisible(false);
                        unbanBtn.setVisible(true);
                    }
                }
            }
        });
    }

    private void clearButton() {
        banBtn.setVisible(false);
        unbanBtn.setVisible(false);
        leaderBtn.setVisible(false);
        unLeaderBtn.setVisible(false);
    }
    @FXML
    private void showTable(VolunteerList volunteerList) {
        // name column
        TableColumn<Volunteer, String> userIdColumn = new TableColumn<>("Username");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<Volunteer, String> banStatusColumn = new TableColumn<>("Status");
        banStatusColumn.setCellValueFactory(cellData -> {
            Volunteer v = cellData.getValue();
            return new ReadOnlyStringWrapper(v.getBanStatus() == 1 ? "banned" : "active");
        });

        TableColumn<Volunteer, String> leaderStatusColumn = new TableColumn<>("Leader");
        leaderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("leaderStatus"));

        userIdColumn.setCellValueFactory(cellData -> {
            Volunteer v = cellData.getValue();
            User user = userList.findUserByID(v.getUserId());
            return new ReadOnlyStringWrapper(user.getUsername());
        });

        teamMemberTableView.getColumns().clear();
        teamMemberTableView.getColumns().add(userIdColumn);
        teamMemberTableView.getColumns().add(banStatusColumn);
        teamMemberTableView.getColumns().add(leaderStatusColumn);
        teamMemberTableView.getItems().clear();
        for (Volunteer volunteer: volunteerList.getVolunteers()) {
            if (volunteer.getTeamId().equals(currentTeam.getTeamId()) && volunteer.getEventId().equals(currentTeam.getEventId())) {
                teamMemberTableView.getItems().add(volunteer);
            }
        }
    }

    @FXML
    private void banButton() {
        if (volunteer != null) {
            volunteerList.banMemberById(volunteer.getUserId(), currentTeam.getEventId(), currentTeam.getTeamId());
            volunteerListDataSource.writeData(volunteerList);
            teamList.removeMember(currentTeam.getTeamId(), currentTeam.getEventId());
            teamListDataSource.writeData(teamList);
            onClickUnLeaderButton();
            showTable(volunteerList);
            clearButton();
        }
    }
    @FXML
    private void unbannedButton() {
        if (volunteer != null) {
            volunteerList.unbannedMemberById(volunteer.getUserId(), currentTeam.getEventId(), currentTeam.getTeamId());
            volunteerListDataSource.writeData(volunteerList);
            teamList.addMember(currentTeam.getTeamId(), currentTeam.getEventId());
            teamListDataSource.writeData(teamList);
            showTable(volunteerList);
            clearButton();
        }
    }

    public void onLeaderButton() {
        if (volunteer != null) {
            volunteerList.setLeaderByUserIdAndEventIdAndTeamIdAndSetFalseAnother(volunteer.getUserId(), currentTeam.getEventId(), currentTeam.getTeamId(), true);
            volunteerListDataSource.writeData(volunteerList);
            showTable(volunteerList);
            clearButton();
        }
    }

    public void onClickUnLeaderButton() {
        if (volunteer != null) {
            volunteerList.setLeaderByUserIdAndEventIdAndTeamId(volunteer.getUserId(), currentTeam.getEventId(), currentTeam.getTeamId(), false);
            volunteerListDataSource.writeData(volunteerList);
            showTable(volunteerList);
            clearButton();
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
    public void onBackButton(){
        try{
            FXRouter.goTo("create-team", eventId);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
