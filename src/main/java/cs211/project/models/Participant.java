package cs211.project.models;

public class Participant {
    private String userId;
    private String eventId;
    private int banStatus;
    private String role;

    public Participant(String userId, String eventId, int banStatus, String role) {
        this.userId = userId;
        this.eventId = eventId;
        this.banStatus = banStatus;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public int getBanStatus() {
        return banStatus;
    }


    public void setBannedStatus(int banned) {
        this.banStatus = banned;
    }


    public String getRole() {
        return role;
    }
    public boolean isUserId(String userId) {return this.userId.equals(userId);}
    public boolean isEventId(String eventId) {return this.eventId.equals(eventId);}

    public String toCSV(){
        return this.getUserId()+", "+this.getEventId()+", "+this.getBanStatus()+", "+this.getRole();
    }
}
