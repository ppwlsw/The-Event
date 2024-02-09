package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Team;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.TeamList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.Regex;
import cs211.project.services.TeamListDataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import java.time.format.DateTimeFormatter;

public class CreateTeamController {

    @FXML
    TableView teamTableView;
    @FXML
    TextField teamNameTextField;
    @FXML
    DatePicker openTeamDatePicker;
    @FXML
    DatePicker closeTeamDatePicker;
    @FXML
    TextField openTeamTimeTextField;
    @FXML
    TextField closeTeamTimeTextField;
    @FXML
    TextField maxTeamMemberTextField;
    @FXML
    Button confirmButton;
    @FXML
    ImageView eventImageView;

    private TeamListDataSource teamListDataSource;
    private TeamList teamList;
    private EventListDataSource eventListDataSource;
    private EventList eventList;
    private Event currentEvent;
    private String eventId;
    String eventImagePath;

    @FXML
    public void initialize() {
        eventId = (String) FXRouter.getData();
        teamListDataSource = new TeamListDataSource("data", "team-list.csv");
        teamList = teamListDataSource.readData();
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventList = eventListDataSource.readData();
        currentEvent = eventList.findEventById(eventId);
        eventImageView.setImage(new Image("file:" + currentEvent.getEventImagePath()));
        teamTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Team team = (Team) newSelection;

                try {
                    FXRouter.goTo("manage-team", team);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        showTable(teamList);
    }

    private void showTable(TeamList teamList) {
        // name column
        TableColumn<Team, String> nameColumn = new TableColumn<>("Team Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        // member column
        TableColumn<Team, Integer> membersColumn = new TableColumn<>("Members");
        membersColumn.setCellValueFactory(new PropertyValueFactory<>("member"));

        teamTableView.getColumns().clear();
        teamTableView.getColumns().add(nameColumn);
        teamTableView.getColumns().add(membersColumn);
        teamTableView.getItems().clear();

        for (Team team: teamList.getTeams()) {
            if (team.getEventId().equals(eventId)) {
                teamTableView.getItems().add(team);
            }
        }
        if (teamTableView.getItems().isEmpty()) {
            teamTableView.setVisible(false);
        }
    }
    private String setDatePickerFormat(DatePicker datePicker, String time) {
        LocalDate date = datePicker.getValue();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time;
        return formattedDate;
    }

    @FXML
    public void onConfirmButtonClick() {
        try {
            String teamName = teamNameTextField.getText().trim();
            String openTimeToJoin = openTeamTimeTextField.getText();
            String closeTimeToJoin = closeTeamTimeTextField.getText();
            String strMaxTeamMem = maxTeamMemberTextField.getText();
            boolean bool = true;
            if (!Regex.checkNumberFormat(strMaxTeamMem) || !Regex.checkTimeFormat(openTimeToJoin) || !Regex.checkTimeFormat(closeTimeToJoin)) {
                bool = false;
            }

            Team exist = teamList.findTeamByTeamNameAndEventId(teamName, eventId);

            if (bool && exist == null) {
                String openDateTime = setDatePickerFormat(openTeamDatePicker, openTimeToJoin);
                String closeDateTime = setDatePickerFormat(closeTeamDatePicker, closeTimeToJoin);
                int maxTeamMember = Integer.parseInt(maxTeamMemberTextField.getText());
                teamList.addNewTeam(eventId, teamName, openDateTime, closeDateTime, 0, maxTeamMember);
                teamListDataSource.writeData(teamList);
                FXRouter.goTo("create-team",eventId);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Can't create team");
                alert.setHeaderText("This Team already exist!");
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
