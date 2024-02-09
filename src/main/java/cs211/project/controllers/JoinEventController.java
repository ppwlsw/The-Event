package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.Participant;
import cs211.project.models.User;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.ParticipantList;
import cs211.project.models.collections.TeamList;
import cs211.project.models.collections.VolunteerList;
import cs211.project.services.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class JoinEventController {
    @FXML
    Label eventNameLabel;
    @FXML
    Label membersLabel;
    @FXML
    Label eventStartLabel;
    @FXML
    Label eventEndLabel;
    @FXML
    Button joinEventButton;
    @FXML
    Button joinEventAsVolunteerButton;
    @FXML
    Button backToMainPageButton;
    @FXML
    Button scheduleButton;
    @FXML
    ImageView eventImageView;
    @FXML
    private Label startJoinLabel;
    @FXML
    private Label endJoinLabel;
    @FXML
    private TextArea descriptionTextArea;

    private EventList eventList;
    private Event event;
    private ParticipantList participantList;
    private ParticipantListDataSource participantListDataSource;
    private VolunteerListDataSource volunteerListDataSource;
    private TeamListDataSource teamListDataSource;
    private TeamList teamList;
    private VolunteerList volunteerList;
    private HashMap<String, Object> data;
    private String userId;
    private String eventId;
    private Participant participant;
    private EventListDataSource dataSource;
    @FXML
    public void initialize(){
        scheduleButton.setVisible(false);
        dataSource = new EventListDataSource("data", "event-list.csv");
        eventList = dataSource.readData();

        participantListDataSource = new ParticipantListDataSource("data", "participant-list.csv");
        participantList = participantListDataSource.readData();

        volunteerListDataSource = new VolunteerListDataSource("data", "volunteer-list.csv");
        volunteerList = volunteerListDataSource.readData();

        teamListDataSource = new TeamListDataSource("data", "team-list.csv");
        teamList = teamListDataSource.readData();

        data = (HashMap<String, Object>) FXRouter.getData();
        eventId = ((Event) data.get("event")).getId();
        userId = ((User) data.get("user")).getUserId();
        participant = participantList.findParticipantByUserIdAndEventId(userId, eventId);
        event = eventList.findEventById(eventId);

        if (event.getUserId().equals(userId)) {
            joinEventButton.setVisible(false);
            scheduleButton.setVisible(true);
            joinEventAsVolunteerButton.setText("team");
        } else if (participant != null && participant.getBanStatus() == 0) {
            joinEventButton.setVisible(false);
            joinEventAsVolunteerButton.setVisible(false);
            scheduleButton.setVisible(true);
        } else if (volunteerList.isUserAlreadyInEvent(userId, eventId)) {
            joinEventButton.setVisible(false);
            joinEventAsVolunteerButton.setText("team");
        }
        if (!teamList.isAlreadyEventTeamExist(eventId)) {
            joinEventAsVolunteerButton.setVisible(false);
        }


        showEvent(event);
    }

    private void showEvent(Event event) {
        eventImageView.setImage(new Image("file:"+event.getEventImagePath(), true));
        eventNameLabel.setText(event.getEventName());
        eventStartLabel.setText(event.getDateStart());
        eventEndLabel.setText(event.getDateEnd());
        startJoinLabel.setText(event.getDateOpenJoin());
        endJoinLabel.setText(event.getDateCloseJoin());
        membersLabel.setText(event.getParticipants() + " / " + event.getMaxParticipants());
        descriptionTextArea.setText(event.getDescription());
    }

    public void onClickJoinButton() {
        Participant exist = participantList.findParticipantByUserIdAndEventId(userId, event.getId());
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        if (exist == null && currentTime.compareTo(event.getDateCloseJoin()) < 0 && !event.getUserId().equals(userId)  && eventList.addParticipantById(eventId) ) {
            participantList.addNewParticipant(userId, event.getId());
            participantListDataSource.writeData(participantList);
            dataSource.writeData(eventList);
            try {
                FXRouter.goTo("join-event", data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Can't join event");
            alert.setHeaderText("Join Event Error!");
            alert.showAndWait();
        }
    }

    public void onJoinAsVolunteerButtonClick(){
            try {
                FXRouter.goTo("join-team", data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    public void onBackToMainPageButton(){
        try {
            FXRouter.goTo("show-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void onClickScheduleButton(){
        try {
            FXRouter.goTo("event-schedule", data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
