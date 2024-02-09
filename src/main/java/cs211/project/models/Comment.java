package cs211.project.models;

public class Comment {
    private String eventId;
    private String teamId;
    private String userName;
    private String time;
    private String message;

    public Comment(String eventId, String teamId, String userName, String time, String message){
        this.eventId = eventId;
        this.teamId = teamId;
        this.userName = userName;
        this.time = time;
        this.message = message;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getEventId() {
        return eventId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEventId(String eventId) {
        return this.eventId.equals(eventId);
    }

    public boolean isTeamId(String teamId) {
        return this.teamId.equals(teamId);
    }


    @Override
    public String toString() {
        return this.getUserName()+ '\s' +this.getTime()+"\n"+this.getMessage();
    }
}