package cs211.project.models.collections;

import cs211.project.models.Team;

import java.util.ArrayList;
import java.util.UUID;

public class TeamList {

    private ArrayList<Team> teams ;

    public TeamList() {
        teams = new ArrayList<>();
    }

    public void addNewTeam(String teamId, String eventId ,String teamName , String dateTimeOpenToJoin , String dateTimeCloseToJoin , int member , int maxMember){
        Team exist = findTeamByTeamIdAndEventId(teamId, eventId);
        if (exist == null && !eventId.equals("") && !teamName.equals("") ) {
            teams.add(new Team(teamId,eventId, teamName,dateTimeOpenToJoin,dateTimeCloseToJoin,member, maxMember));
        }
    }

    public void addNewTeam( String eventId ,String teamName , String dateTimeOpenToJoin , String dateTimeCloseToJoin , int member , int maxMember){
        String teamId = UUID.randomUUID().toString();
        Team exist = findTeamByTeamIdAndEventId(teamId,eventId);
        if (exist == null && !eventId.equals("") && !teamName.equals("") && !dateTimeOpenToJoin.equals("") && !dateTimeCloseToJoin.equals("")) {
            teams.add(new Team(teamId,eventId, teamName, dateTimeOpenToJoin, dateTimeCloseToJoin, member, maxMember));
        }
    }

    public Team findTeamByTeamNameAndEventId(String teamName, String eventId){
        for (Team team : teams){
            if(team.isEventId(eventId) && team.isTeamName(teamName)){
                return team;
            }
        }
        return null;
    }
    public Team findTeamByTeamIdAndEventId(String teamId, String eventId){
        for (Team team : teams){
            if(team.isEventId(eventId) && team.isTeamId(teamId)){
                return team;
            }
        }
        return null;
    }

    public boolean isAlreadyEventTeamExist(String eventId) {
        for (Team team: teams) {
            if (team.isEventId(eventId)) {
                return true;
            }
        } return false;
    }

    public boolean addMember(String teamId, String eventId) {
        Team exist = findTeamByTeamIdAndEventId(teamId, eventId);
        if (exist != null && exist.getMember() < exist.getMaxMember()) {
            exist.addMember();
            return true;
        }
        return false;
    }

    public boolean removeMember(String teamId, String eventId) {
        Team exist = findTeamByTeamIdAndEventId(teamId, eventId);
        if (exist != null && exist.getMember() >= 0) {
            exist.removeMember();
            return true;
        }
        return false;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }
}
