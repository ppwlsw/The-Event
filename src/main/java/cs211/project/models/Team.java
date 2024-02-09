package cs211.project.models;
public class Team {
    private String teamId;
    private String eventId;
    private String teamName;
    private String dateOpenToJoin;
    private String dateTimeOpenToJoin;
    private String dateCloseToJoin;
    private String dateTimeCloseToJoin;
    private String timeOpenToJoin;
    private String timeCloseToJoin;
    private int member;
    private int maxMember;

    public Team(String teamId ,String eventId, String teamName, String dateOpenToJoin, String dateCloseToJoin, String timeOpenToJoin, String timeCloseToJoin, int member, int maxMember) {
        this.teamId = teamId;
        this.eventId = eventId;
        this.teamName = teamName;
        this.dateOpenToJoin = dateOpenToJoin;
        this.dateCloseToJoin = dateCloseToJoin;
        this.timeOpenToJoin = timeOpenToJoin;
        this.timeCloseToJoin = timeCloseToJoin;
        this.member = member;
        this.maxMember = maxMember;
    }

    public Team(String teamId,String eventId, String teamName, String dateTimeOpenToJoin, String dateTimeCloseToJoin, int member, int maxMember) {
        this.teamId = teamId;
        this.eventId = eventId;
        this.teamName = teamName;
        this.dateTimeOpenToJoin = dateTimeOpenToJoin;
        this.dateTimeCloseToJoin = dateTimeCloseToJoin;
        this.member = member;
        this.maxMember = maxMember;
    }

    public boolean isTeamName(String teamName){
        return this.teamName.equals(teamName);
    }

    public boolean isEventId(String eventId){return this.eventId.equals(eventId);}
    public boolean isTeamId(String teamId){
        return this.teamId.equals(teamId);
    }

    public String getTeamId() {
        return teamId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getDateTimeOpenToJoin() {
        return dateTimeOpenToJoin;
    }

    public String getDateTimeCloseToJoin() {
        return dateTimeCloseToJoin;
    }

    public int getMember() {
        return member;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void addMember() {
        this.member++;
    }

    public void removeMember(){
        this.member--;
    }
}
