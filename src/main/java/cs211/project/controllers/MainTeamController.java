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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.HashMap;

public class MainTeamController {
    TeamListDataSource teamListDataSource;
    TeamList teamList;
    Team team;
    UserListFileDataSource userListFileDataSource;
    UserList userList;
    EventListDataSource eventListDataSource;
    VolunteerListDataSource volunteerListDataSource;
    VolunteerList volunteerList;
    Volunteer volunteer;
    EventList eventList;
    Event event;
    String eventId;
    @FXML
    Label teamNameLabel;
    @FXML
    TableView teamMemberTableView;
    @FXML
    Button unbanBtn;
    @FXML Button banBtn;
    private User user;
    private HashMap<String, Object> data;



    public void initialize(){

        clearButton();
        data = (HashMap<String, Object>) FXRouter.getData();
        user = (User) data.get("user");
        team = (Team) data.get("team");
        teamNameLabel.setText(team.getTeamName());
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventList = eventListDataSource.readData();
        volunteerListDataSource = new VolunteerListDataSource("data", "volunteer-list.csv");
        volunteerList = volunteerListDataSource.readData();
        userListFileDataSource = new UserListFileDataSource("data", "user-list.csv");
        userList = userListFileDataSource.readData();
        teamListDataSource = new TeamListDataSource("data","team-list.csv");
        teamList = teamListDataSource.readData();
        eventId = team.getEventId();
        event = eventList.findEventById(eventId);
        showTable(volunteerList);

        teamMemberTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Volunteer>() {
            @Override
            public void changed(ObservableValue<? extends Volunteer> observable, Volunteer oldValue, Volunteer newValue) {
                if (newValue == null) {
                    volunteer = null;
                } else {
                    volunteer = newValue;
                    if (!newValue.getUserId().equals(user.getUserId()) && volunteer.getBanStatus() == 0 ){
                        banBtn.setVisible(true);
                        unbanBtn.setVisible(false);
                    } else {
                        unbanBtn.setVisible(true);
                        if(newValue.getUserId().equals(user.getUserId())){
                            unbanBtn.setVisible(false);
                        }
                        banBtn.setVisible(false);
                    }
                }
            }
        });
    }

    private void clearButton() {
        banBtn.setVisible(false);
        unbanBtn.setVisible(false);
    }
    @FXML
    private void showTable(VolunteerList volunteerList) {
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
            if (volunteer.getTeamId().equals(team.getTeamId()) && volunteer.getEventId().equals(team.getEventId())) {
                teamMemberTableView.getItems().add(volunteer);
            }
        }
    }

    @FXML
    private void banButton() {
        if (volunteer != null) {
            volunteerList.banMemberById(volunteer.getUserId(), team.getEventId(), team.getTeamId());
            volunteerListDataSource.writeData(volunteerList);
            teamList.removeMember(team.getTeamId(), team.getEventId());
            teamListDataSource.writeData(teamList);
            showTable(volunteerList);
            clearButton();
        }
    }
    @FXML
    private void unbannedButton() {
        if (volunteer != null) {
            volunteerList.unbannedMemberById(volunteer.getUserId(), team.getEventId(), team.getTeamId());
            volunteerListDataSource.writeData(volunteerList);
            teamList.addMember(team.getTeamId(), team.getEventId());
            teamListDataSource.writeData(teamList);
            showTable(volunteerList);
            clearButton();
        }
    }
    @FXML
    public void onBackButton(){
        try{
            FXRouter.goTo("schedule-team",data);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }




}
