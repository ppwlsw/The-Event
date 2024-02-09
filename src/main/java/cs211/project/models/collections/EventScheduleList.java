package cs211.project.models.collections;

import cs211.project.models.EventSchedule;

import java.util.ArrayList;

public class EventScheduleList {
    private ArrayList<EventSchedule> eventSchedules;
    public EventScheduleList() {
        eventSchedules = new ArrayList<>();}
    public void addNewEventSchedule(String eventId, String name, String detail, boolean process, String startTime, String endTime) {
        eventId = eventId.trim();
        startTime = startTime.trim();
        endTime = endTime.trim();
        name = name.trim();
        detail = detail.trim();
        EventSchedule exist = findScheduleByEventIdAndName(eventId, name);
        if (exist == null && !eventId.equals("") && !startTime.equals("") && ! endTime.equals("") && !name.equals("") && !detail.equals("")) {
            eventSchedules.add(new EventSchedule(eventId, name, detail, process, startTime, endTime));
        }
    }
    public void addNewEventSchedule(String eventId, String name, String detail, String startTime, String endTime) {
        eventId = eventId.trim();
        startTime = startTime.trim();
        endTime = endTime.trim();
        name = name.trim();
        detail = detail.trim();
        EventSchedule exist = findScheduleByEventIdAndName(eventId, name);
        if (exist == null && !eventId.equals("") && !startTime.equals("") && ! endTime.equals("") && !name.equals("") && !detail.equals("")) {
            eventSchedules.add(new EventSchedule(eventId, name, detail, startTime, endTime));
        }
    }
    public EventSchedule findScheduleByEventIdAndName(String eventId, String name) {
        for (EventSchedule eventSchedule : eventSchedules) {
            if (eventSchedule.isEventId(eventId) && eventSchedule.isName(name)) {
                return eventSchedule;
            }
        } return null;
    }

    public boolean deleteScheduleByEventIdAndName(String eventId, String name) {
        EventSchedule exist = findScheduleByEventIdAndName(eventId, name);
        if (exist != null) {
            eventSchedules.remove(exist);
            return true;
        }
        return false;
    }

    public ArrayList<EventSchedule> getSchedules() {
        return eventSchedules;
    }
}
