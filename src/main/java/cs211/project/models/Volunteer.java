package cs211.project.models;

public class Volunteer extends Participant{
    private String teamId;
    private boolean leaderStatus;

    public Volunteer(String userId, String eventId, String teamId) {
        this(userId, eventId, 0, "volunteer", teamId, false);
    }

    public Volunteer(String userId, String eventId, int banStatus, String role, String teamId, boolean leaderStatus) {
        super(userId, eventId, banStatus, role);
        this.teamId = teamId;
        this.leaderStatus = leaderStatus;
    }

    public String getTeamId() {
        return teamId;
    }

    public boolean isLeaderStatus() {
        return leaderStatus;
    }

    public boolean isTeamId(String teamId){
        return this.teamId.equals(teamId);
    }

    public void setLeaderStatus(boolean leaderStatus) {
        this.leaderStatus = leaderStatus;
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," +  this.getTeamId() + "," + this.isLeaderStatus();
    }

}
