package cs211.project.controllers;

import cs211.project.models.*;
import cs211.project.models.collections.TeamScheduleList;
import cs211.project.models.collections.UserList;
import cs211.project.models.collections.VolunteerList;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamScheduleListDataSource;
import cs211.project.services.UserListFileDataSource;
import cs211.project.services.VolunteerListDataSource;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class TeamScheduleController {
    @FXML
    private TableView teamScheduleTableView;
    @FXML
    private Button createScheduleButton;
    private TeamScheduleListDataSource teamScheduleListDataSource;
    private TeamScheduleList teamScheduleList;
    private VolunteerList volunteerList;
    private UserListFileDataSource dataSource;
    private VolunteerListDataSource volunteerListDataSource;
    private UserList userList;
    private HashMap<String, Object> data;
    private User currentUser;
    String eventId;
    String userId;
    String teamId;
    Event event;

    @FXML Button memberButton;
    @FXML
    public void initialize(){
        dataSource = new UserListFileDataSource("data", "user-list.csv");
        volunteerListDataSource = new VolunteerListDataSource("data","volunteer-list.csv");
        userList = dataSource.readData();
        volunteerList = volunteerListDataSource.readData();
        teamScheduleListDataSource = new TeamScheduleListDataSource("data", "team-schedule-list.csv");
        teamScheduleList = teamScheduleListDataSource.readData();
        data = (HashMap<String, Object>) FXRouter.getData();
        event =  (Event) data.get("event");
        currentUser = (User) data.get("user");
        eventId =event.getId();
        userId = currentUser.getUserId();
        teamId = ((Team) data.get("team")).getTeamId();
        createScheduleButton.setVisible(false);
        memberButton.setVisible(false);
        showTable(teamScheduleList);

        Volunteer exist = volunteerList.findVolunteerByUserIdEventIdTeamId(currentUser.getUserId(),eventId,teamId);
        if(currentUser.getUserId().equals(event.getUserId()) || exist.isLeaderStatus()){
            createScheduleButton.setVisible(true);
            memberButton.setVisible(true);


            ContextMenu contextMenu = new ContextMenu();
            MenuItem option1 = new MenuItem("Done");
            MenuItem option2 = new MenuItem("Delete");
            contextMenu.getItems().addAll(option1,option2);
            teamScheduleTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
                if(newSelection != null){
                    teamScheduleTableView.setOnContextMenuRequested(mouseEvent -> {
                        teamScheduleTableView.setContextMenu(contextMenu);
                        contextMenu.show(teamScheduleTableView,mouseEvent.getScreenX(),mouseEvent.getScreenY());
                    });
                }
                option1.setOnAction(event1 -> {
                    ((TeamSchedule) newSelection).setProcess(false);
                    teamScheduleListDataSource.writeData(teamScheduleList);
                    showTable(teamScheduleList);
                });
                option2.setOnAction(event2 -> {
                    TeamSchedule teamExist = (TeamSchedule) newSelection;
                    teamScheduleList.deleteScheduleByEventIdAndTeamIdAndName(eventId, teamId, teamExist.getName());
                    teamScheduleListDataSource.writeData(teamScheduleList);
                    showTable(teamScheduleList);
                });
            });
        }

    }

    private void showTable(TeamScheduleList teamScheduleList) {
        TableColumn<TeamSchedule, String> nameColumn = new TableColumn<>("Activities Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TeamSchedule, String> detailColumn = new TableColumn<>("Details");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

        TableColumn<TeamSchedule, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> {
            TeamSchedule teamSchedule = cellData.getValue();
            return new ReadOnlyStringWrapper(teamSchedule.isProcess() ? "in process" : "ended");
        });

        teamScheduleTableView.getColumns().clear();
        teamScheduleTableView.getColumns().add(nameColumn);
        teamScheduleTableView.getColumns().add(detailColumn);
        teamScheduleTableView.getColumns().add(statusColumn);
        teamScheduleTableView.getItems().clear();

        for (TeamSchedule teamSchedule: teamScheduleList.getSchedules()) {
            if (teamSchedule.getEventId().equals(eventId) && teamSchedule.getTeamId().equals(teamId)) {
                teamScheduleTableView.getItems().add(teamSchedule);
            }
        }
    }
    @FXML
    public void backToTeam() {
        try {
            FXRouter.goTo("join-team");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToMemberPage() {
        try {
            FXRouter.goTo("member-leader",data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void goToComment() throws IOException {

        Stage chatStage = new Stage();
        chatStage.setTitle("comment");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/comment-page.fxml"));
        Parent chatRoot = loader.load();
        String username = currentUser.getUsername();
        if (currentUser.getUserId().equals(event.getUserId())){
            username += " (OWNER) ";
        }
        CommentController commentController = loader.getController();
        commentController.setData(eventId, teamId, username);

        Scene chatScene = new Scene(chatRoot, 500, 600);
        chatStage.setScene(chatScene);
        chatStage.show();
    }



    @FXML
    public void goToCreateSchedule() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("comment");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/create-team-schedule.fxml"));
        Parent root = loader.load();

        CreateTeamScheduleController createTeamScheduleController = loader.getController();
        createTeamScheduleController.setData(eventId, teamId, "schedule-team");

        Scene scene = new Scene(root, 300, 450);
        stage.setScene(scene);
        stage.show();
    }
}
