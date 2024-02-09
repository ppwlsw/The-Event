package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.TeamSchedule;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.TeamScheduleList;
import cs211.project.models.collections.UserList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamScheduleListDataSource;
import cs211.project.services.UserListFileDataSource;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class ManageTeamController {

    @FXML
    ImageView eventImageView;
    @FXML
    Label teamNameLabel;
    @FXML
    TableView teamScheduleTableView;
    EventListDataSource eventListDataSource;
    TeamScheduleListDataSource teamScheduleListDataSource;
    TeamScheduleList teamScheduleList;
    EventList eventList;
    Event event;
    String eventImagePath;
    Team currentTeam;
    String eventId;
    UserListFileDataSource userListFileDataSource;
    UserList userList;

    public void initialize() {
        currentTeam = (Team) FXRouter.getData();
        teamNameLabel.setText(currentTeam.getTeamName());
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventList = eventListDataSource.readData();
        teamScheduleListDataSource = new TeamScheduleListDataSource("data", "team-schedule-list.csv");
        teamScheduleList = teamScheduleListDataSource.readData();
        userListFileDataSource = new UserListFileDataSource("data","user-list.csv");
        userList = userListFileDataSource.readData();
        eventId = currentTeam.getEventId();
        event = eventList.findEventById(eventId);
        eventImagePath = event.getEventImagePath();
        eventImageView.setImage(new Image("file:" + eventImagePath));
        showTable(teamScheduleList);
        teamScheduleTableView.getSelectionModel().clearSelection();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem option1 = new MenuItem("Done");
        MenuItem option2 = new MenuItem("Delete");
        contextMenu.getItems().addAll(option1, option2);
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
                TeamSchedule exist = (TeamSchedule) newSelection;
                teamScheduleList.deleteScheduleByEventIdAndTeamIdAndName(eventId, currentTeam.getTeamId(), exist.getName());
                teamScheduleListDataSource.writeData(teamScheduleList);
                showTable(teamScheduleList);
            });
        });

    }

    public void showTable(TeamScheduleList teamScheduleList) {
        TableColumn<TeamSchedule, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TeamSchedule, String> detailColumn = new TableColumn<>("Detail");
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

        for (TeamSchedule teamSchedule:teamScheduleList.getSchedules()) {
            if(teamSchedule.getEventId().equals(eventId) && teamSchedule.getTeamId().equals(currentTeam.getTeamId())) {
                teamScheduleTableView.getItems().add(teamSchedule);
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

    public void onCreateButton() throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/create-team-schedule.fxml"));
        Parent root = loader.load();

        CreateTeamScheduleController createTeamScheduleController = loader.getController();
        createTeamScheduleController.setData(eventId, currentTeam.getTeamId(), "manage-team");

        Scene scene = new Scene(root, 300, 450);
        stage.setScene(scene);
        stage.show();
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
    @FXML
    public void onMemberButton(){
        try{
            FXRouter.goTo("team-member", currentTeam);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onCommentButton()throws IOException{
        Stage chatStage = new Stage();
        chatStage.setTitle("comment");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs211/project/views/comment-page.fxml"));
        Parent chatRoot = loader.load();
        User user = userList.findUserByID(event.getUserId());

        CommentController commentController = loader.getController();
        commentController.setData(eventId, currentTeam.getTeamId(), user.getUsername()+" (OWNER) ");

        Scene chatScene = new Scene(chatRoot, 500, 600);
        chatStage.setScene(chatScene);
        chatStage.show();
    }
}
