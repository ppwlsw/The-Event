package cs211.project.controllers;

import cs211.project.models.TeamSchedule;
import cs211.project.models.collections.TeamScheduleList;
import cs211.project.services.FXRouter;
import cs211.project.services.TeamScheduleListDataSource;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateTeamScheduleController {
    @FXML
    TextField nameTextField;
    @FXML
    TextArea detailTextArea;
    private TeamScheduleListDataSource teamScheduleListDataSource;
    TeamScheduleList teamScheduleList;
    private String currentEventId;
    private String currentTeamId;
    private String pagePath;
    public void setData(String eventId, String teamId, String beforePage) {
        pagePath = beforePage;
        currentEventId = eventId;
        currentTeamId = teamId;
        teamScheduleListDataSource = new TeamScheduleListDataSource("data", "team-schedule-list.csv");
        teamScheduleList = teamScheduleListDataSource.readData();

    }
    public void onCreateButton() throws IOException {
        String name = nameTextField.getText();
        String detail = detailTextArea.getText();
        if (!name.equals("") && !detail.equals("")) {
            TeamSchedule exist = teamScheduleList.findScheduleByEventIdAndTeamIdAndName(currentEventId, currentTeamId, name);
            if (exist == null) {
                teamScheduleList.addNewTeamSchedule(currentEventId, name, detail, currentTeamId);
                teamScheduleListDataSource.writeData(teamScheduleList);
                FXRouter.goTo(pagePath);

                Stage stage = (Stage) nameTextField.getScene().getWindow();
                stage.close();

            }
        }
    }
}
