package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.EventSchedule;
import cs211.project.models.User;
import cs211.project.models.collections.EventScheduleList;
import cs211.project.services.EventScheduleListDataSource;
import cs211.project.services.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.HashMap;

public class ScheduleEventController {
    @FXML
    private TableView scheduleTableView;
    private EventScheduleList eventScheduleList;
    private EventScheduleListDataSource dataSource;
    private HashMap<String, Object> data;
    private String userId;
    private String eventId;

    @FXML
    public void initialize() {
        dataSource = new EventScheduleListDataSource("data", "schedule-list.csv");
        eventScheduleList = dataSource.readData();
        data = (HashMap<String, Object>) FXRouter.getData();
        eventId = ((Event) data.get("event")).getId();
        userId = ((User) data.get("user")).getUserId();
        showTable(eventScheduleList);
    }
    private void showTable(EventScheduleList eventScheduleList) {
        // start time column
        TableColumn<EventSchedule, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        // end time column
        TableColumn<EventSchedule, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        // name column
        TableColumn<EventSchedule, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // detail column
        TableColumn<EventSchedule, String> detailColumn = new TableColumn<>("Detail");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

        scheduleTableView.getColumns().clear();
        scheduleTableView.getColumns().add(startTimeColumn);
        scheduleTableView.getColumns().add(endTimeColumn);
        scheduleTableView.getColumns().add(nameColumn);
        scheduleTableView.getColumns().add(detailColumn);
        scheduleTableView.getItems().clear();

        for (EventSchedule eventSchedule : eventScheduleList.getSchedules()) {
            if (eventSchedule.getEventId().equals(eventId))
                scheduleTableView.getItems().add(eventSchedule);
        }
    }
    @FXML
    public void onBackButton() {
        try {
            FXRouter.goTo("join-event", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
