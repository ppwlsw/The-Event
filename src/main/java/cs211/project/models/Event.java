package cs211.project.models;

public class Event {
    private String id;
    private String eventName;
    private String description;
    private String dateStart;
    private String dateEnd;
    private String eventImagePath;
    private String userId;
    private int participants;
    private int maxParticipants;
    private String dateOpenJoin;
    private String dateCloseJoin;

    public Event(String id, String eventName, String description, String dateStart, String dateEnd, String imagePath, String userId, int participants, int maxParticipants, String dateOpenJoin, String dateCloseJoin) {
        this.id = id;
        this.eventImagePath = imagePath;
        this.eventName = eventName;
        this.description = description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userId = userId;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
        this.dateOpenJoin = dateOpenJoin;
        this.dateCloseJoin = dateCloseJoin;
    }

    public String getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getEventImagePath() {
        return eventImagePath;
    }

    public int getParticipants() {return participants;}

    public int getMaxParticipants() {return maxParticipants;}

    public void setEventImagePath(String eventImagePath) {
        this.eventImagePath = eventImagePath;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setDateOpenJoin(String dateOpenJoin) {
        this.dateOpenJoin = dateOpenJoin;
    }

    public void setDateCloseJoin(String dateCloseJoin) {
        this.dateCloseJoin = dateCloseJoin;
    }

    public String getUserId() {
        return userId;
    }

    public String getDateOpenJoin() {return dateOpenJoin;}

    public String getDateCloseJoin() {return dateCloseJoin;}

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
    public void addParticipant() {
        this.participants += 1;
    }

    public boolean isId(String id) {return this.id.equals(id);}
    public boolean isName(String name) {return this.eventName.equals(name);}

}
