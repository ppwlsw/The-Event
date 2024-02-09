package cs211.project.models;

public class Schedule {
    private String eventId;
    private String name;
    private String detail;
    private boolean process;

    public Schedule(String eventId, String name, String detail, boolean process) {
        this.eventId = eventId;
        this.name = name;
        this.detail = detail;
        this.process = process;
    }

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isProcess() {
        return process;
    }
    public boolean isEventId(String id) {return this.eventId.equals(id);}
    public boolean isName(String name) {return this.name.equals(name);}

    public void setProcess(boolean process) {
        this.process = process;
    }

    public String toCsv() {
        return this.getEventId() + "," + this.getName() + "," + this.getDetail() + "," + this.isProcess();
    }
}
