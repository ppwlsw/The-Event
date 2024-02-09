package cs211.project.models.collections;

import cs211.project.models.TeamSchedule;

import java.util.ArrayList;

public class TeamScheduleList {
    private ArrayList<TeamSchedule> teamSchedules;
    public TeamScheduleList() {
        teamSchedules = new ArrayList<>();
    }
    public void addNewTeamSchedule(String eventId, String name, String detail, boolean process, String teamId) {
        eventId = eventId.trim();
        teamId = teamId.trim();
        name = name.trim();
        detail = detail.trim();
        TeamSchedule exist = findScheduleByEventIdAndTeamIdAndName(eventId, teamId, name);
        if(exist == null && !eventId.equals("") && !teamId.equals("") && !name.equals("") && !detail.equals("")) {
            teamSchedules.add(new TeamSchedule(eventId, name, detail, process, teamId));
        }
    }
    public void addNewTeamSchedule(String eventId, String name, String detail, String teamId) {
        eventId = eventId.trim();
        name = name.trim();
        detail = detail.trim();
        teamId = teamId.trim();
        TeamSchedule exist = findScheduleByEventIdAndTeamIdAndName(eventId, teamId, name);
        if(exist == null && !eventId.equals("") && !name.equals("") && !detail.equals("") && !teamId.equals("")) {
            teamSchedules.add(new TeamSchedule(eventId, name, detail, teamId));
        }

    }
    public TeamSchedule findScheduleByEventIdAndTeamIdAndName(String eventId, String teamId, String name) {
        for(TeamSchedule teamSchedule : teamSchedules) {
            if(teamSchedule.isEventId(eventId) && teamSchedule.isTeamId(teamId) && teamSchedule.isName(name)) {
                return teamSchedule;
            }
        } return null;
    }
    public boolean deleteScheduleByEventIdAndTeamIdAndName(String eventId, String teamId, String name) {
        TeamSchedule exist = findScheduleByEventIdAndTeamIdAndName(eventId, teamId, name);
        if(exist != null) {
            teamSchedules.remove(exist);
            return true;
        }
        return false;
    }

    public ArrayList<TeamSchedule> getSchedules() {
        return teamSchedules;
    }
}
