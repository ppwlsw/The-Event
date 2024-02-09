package cs211.project.models;

public class TeamSchedule extends Schedule{
    private String teamId;

    public TeamSchedule(String eventId, String name, String detail, boolean process, String teamId) {
        super(eventId, name, detail, process);
        this.teamId = teamId;
    }
    public TeamSchedule(String eventId, String name, String detail, String teamId) {
        this(eventId, name, detail, true, teamId);
    }

    public String getTeamId() {
        return teamId;
    }
    public boolean isTeamId(String id) {
        return this.teamId.equals(id);
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + this.getTeamId();
    }
}
