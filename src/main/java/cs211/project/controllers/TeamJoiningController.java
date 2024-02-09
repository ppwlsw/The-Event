package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.User;
import cs211.project.models.Volunteer;
import cs211.project.models.collections.TeamList;
import cs211.project.models.collections.VolunteerList;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamListDataSource;
import cs211.project.services.VolunteerListDataSource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class TeamJoiningController {
    @FXML
    TableView teamTableView;
    private TeamList teamList;
    private VolunteerList volunteerList;
    private VolunteerListDataSource volunteerListDataSource;
    private TeamListDataSource teamListDataSource;
    private HashMap<String, Object> data ;
    String eventId;
    Event event;
    String userId;
    @FXML
    public void initialize(){
        teamListDataSource = new TeamListDataSource("data" , "team-list.csv");
        volunteerListDataSource = new VolunteerListDataSource("data" , "volunteer-list.csv");
        teamList = teamListDataSource.readData();
        data = (HashMap<String, Object>) FXRouter.getData();
        event = (Event) data.get("event");
        eventId = event.getId();
        userId = ((User) data.get("user")).getUserId();
        volunteerList = volunteerListDataSource.readData();

        teamTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Team team = (Team) newSelection;
                Volunteer exist = volunteerList.findVolunteerByUserIdEventIdTeamId(userId,eventId,team.getTeamId());
                data.put("team", team);
                boolean check = event.getUserId().equals(userId);
                teamTableView.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getClickCount() == 1 && (exist != null || check)) {
                        if(!check && exist.getBanStatus() == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("YOU GOT BANNED !");
                            alert.setHeaderText("This username got banned from this team.");
                            alert.showAndWait();
                            return;
                        }
                        else{
                            try {
                                FXRouter.goTo("schedule-team", data);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                    if(mouseEvent.getClickCount() == 2){
                        openJoinDialog((Team) newSelection);
                    }

                });
            }
        });
        showTable(teamList);
    }

    public void onBackToJoinEventPage(){
        try{
            FXRouter.goTo("join-event");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void showTable(TeamList teamList) {
        // start time column
        TableColumn<Team, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        // end time column
        TableColumn<Team, String> memberColumn = new TableColumn<>("Member");
        memberColumn.setCellValueFactory(new PropertyValueFactory<>("member"));
        // name column
        TableColumn<Team, String> maxMemberColumn = new TableColumn<>("Max Member");
        maxMemberColumn.setCellValueFactory(new PropertyValueFactory<>("maxMember"));
        // detail column
        TableColumn<Team, String> openDateAndTimeColumn = new TableColumn<>("Open Date and Time");
        openDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTimeOpenToJoin"));

        TableColumn<Team, String> closeDateAndTimeColumn = new TableColumn<>("Close Date and Time");
        closeDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTimeCloseToJoin"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(teamNameColumn);
        teamTableView.getColumns().add(memberColumn);
        teamTableView.getColumns().add(maxMemberColumn);
        teamTableView.getColumns().add(openDateAndTimeColumn);
        teamTableView.getColumns().add(closeDateAndTimeColumn);
        teamTableView.getItems().clear();

        for (Team team: teamList.getTeams()) {
            if (team.getEventId().equals(eventId))
                teamTableView.getItems().add(team);
        }

    }

    private void openJoinDialog(Team selectedTeam) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Join Team");
        dialog.setHeaderText("Do you want to join this team?");

        ButtonType joinButton = new ButtonType("Join", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(joinButton, cancelButton);

        boolean isVolunteerInTeam = volunteerList.isUserAlreadyInTeam(userId,eventId,selectedTeam.getTeamId());

        if (isVolunteerInTeam) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("You are already a member of this team.");
            alert.setContentText("You cannot join the team again.");

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(closeButton);

            alert.showAndWait();
            return;
        }

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == joinButton && !volunteerList.isUserAlreadyInTeam(userId,eventId,selectedTeam.getTeamId()) && teamList.addMember(selectedTeam.getTeamId(), eventId)) {
            volunteerList.addNewVolunteer(userId, eventId, selectedTeam.getTeamId());
            volunteerListDataSource.writeData(volunteerList);
            teamListDataSource.writeData(teamList);
            showTable(teamList);
        }
    }

}
