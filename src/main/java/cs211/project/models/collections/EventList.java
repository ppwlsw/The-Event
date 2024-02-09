package cs211.project.models.collections;

import cs211.project.models.Event;
import cs211.project.models.Participant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class EventList {
    private ArrayList<Event> events;

    public EventList() {
        events = new ArrayList<>();
    }
    public void addNewEvent(String id, String eventName, String description, String startDate, String endDate, String imageResource, String userId, int paci, int maxipaci, String dateOpen, String dateClose) {
        id = id.trim();
        eventName = eventName.trim();
        description = description.trim();
        startDate = startDate.trim();
        endDate = endDate.trim();
        userId = userId.trim();
        imageResource = imageResource.trim();
        dateOpen = dateOpen.trim();
        dateClose = dateClose.trim();
        if (!id.equals("") && !eventName.equals("") && !description.equals("") && !startDate.equals("") && !endDate.equals("") && !userId.equals("") && !imageResource.equals("") && !dateOpen.equals("") && !dateClose.equals("")) {
            Event exist = findEventById(eventName);
            if (exist == null) {
                events.add(new Event(id, eventName, description, startDate, endDate, imageResource , userId, paci, maxipaci, dateOpen, dateClose));
            }
        }
    }
    public void addNewEvent(String eventName, String description, String startDate, String endDate, String imageResource, String userId, int maxpaci) {
        String id = UUID.randomUUID().toString();
        imageResource = imageResource.trim();
        eventName = eventName.trim();
        description = description.trim();
        startDate = startDate.trim();
        endDate = endDate.trim();
        userId = userId.trim();
        String dateStart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String dateClose = startDate;
        if (!eventName.equals("") && !description.equals("") && !startDate.equals("") && !endDate.equals("") && !userId.equals("")) {
            Event exist = findEventByEventName(eventName);
            if (exist == null) {
                events.add(new Event(id,eventName, description, startDate, endDate, imageResource, userId, 0, maxpaci, dateStart, dateClose));
            }
        }
    }


    public Event findEventById(String id) {
        for (Event event: events) {
            if(event.isId(id)) {
                return event;
            }
        }
        return null;
    }

    public Event findEventByEventName(String name) {
        for (Event event: events) {
            if (event.isName(name)) {
                return event;
            }
        }
        return null;
    }

    public void changeEventImageById(String id, String newImagePath){
        Event exist = findEventById(id);
        if (exist != null) {
            exist.setEventImagePath(newImagePath);
        }
    }

    public void changeEventNameById(String id, String newEventName){
        Event exist = findEventById(id);
        if (exist != null) {
            exist.setEventName(newEventName);
        }
    }

    public void changeEventDescriptionById(String id, String newDescription){
        Event exist = findEventById(id);
        if (exist != null) {
            exist.setDescription(newDescription);
        }
    }

    public void changeEventStartDateTimeById(String id, String newEventStartDateTime){
        Event exist = findEventById(id);
        if (exist != null) {
            exist.setDateStart(newEventStartDateTime);
        }
    }

    public void changeMaxParticipatorById(String id , int maxParti){
        Event exist = findEventById(id);
        if (exist != null) {
            exist.setMaxParticipants(maxParti);
        }
    }

    public void changeEventEndDateTimeById(String id, String newEventEndDateTime){
        Event exist = findEventById(id);
        if (exist != null) {
            exist.setDateEnd(newEventEndDateTime);
        }
    }

    public void updateParticipant(ParticipantList participantList) {
        for (Event event: events) {
            int i = 0;
            for (Participant participant: participantList.getParticipants()) {
                if (event.getId().equals(participant.getEventId()) && participant.getBanStatus() == 0) {
                    i++;
                }
            }
            event.setParticipants(i);
        }
    }

    public boolean addParticipantById(String id) {
        Event exist = findEventById(id);
        if (exist != null && exist.getParticipants() < exist.getMaxParticipants()) {
            exist.addParticipant();
            return true;
        }
        return false;
    }

    public ArrayList<Event> getEvents() {return events;}
}
