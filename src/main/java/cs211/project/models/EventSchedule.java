package cs211.project.models;

public class EventSchedule extends Schedule {
    private String startTime;
    private String endTime;

    public EventSchedule(String eventId, String name, String detail, boolean process, String startTime, String endTime) {
        super(eventId, name, detail, process);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventSchedule(String eventId, String name, String detail, String startTime, String endTime) {
        this(eventId, name, detail, true, startTime, endTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + this.getStartTime() + "," + this.getEndTime();
    }
}
