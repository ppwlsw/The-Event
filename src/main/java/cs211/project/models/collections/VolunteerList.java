package cs211.project.models.collections;

import cs211.project.models.Volunteer;

import java.util.ArrayList;

public class VolunteerList {

    private ArrayList<Volunteer> volunteers;

    public VolunteerList() {
        volunteers = new ArrayList<>();
    }

    public void addNewVolunteer(String userId, String eventId, int banStatus, String role, String teamId, boolean leaderStatus){
        Volunteer exist = findVolunteerByUserIdEventIdTeamId(userId , eventId, teamId);
        if (exist == null) {
            volunteers.add(new Volunteer(userId, eventId, banStatus, role, teamId, leaderStatus));
        }

    }

    public void addNewVolunteer(String userId, String eventId, String teamId){
        Volunteer exist = findVolunteerByUserIdEventIdTeamId(userId , eventId, teamId);
        if (exist == null) {
            volunteers.add(new Volunteer(userId, eventId, teamId));
        }
    }

    public boolean isUserAlreadyInTeam(String userId , String eventId, String teamId){
        for(Volunteer volunteer : volunteers){
            if(volunteer.isTeamId(teamId) && volunteer.isEventId(eventId) && volunteer.isUserId(userId)){
                return true;
            }
        }
        return false;
    }
    public Volunteer findVolunteerByUserIdEventIdTeamId(String userId , String eventId , String teamId){
        for(Volunteer volunteer : volunteers){
            if(volunteer.isUserId(userId) && volunteer.isEventId(eventId) && volunteer.isTeamId(teamId)){
                return volunteer;
            }
        }
        return null;
    }

    public void banMemberById(String userId,String eventId,String teamId) {
        Volunteer exist = findVolunteerByUserIdEventIdTeamId(userId,eventId,teamId);
        if (exist != null) {
            exist.setBannedStatus(1);
        }
    }

    public void unbannedMemberById(String userId,String eventId,String teamId) {
        Volunteer exist = findVolunteerByUserIdEventIdTeamId(userId,eventId,teamId);
        if (exist != null) {
            exist.setBannedStatus(0);
        }
    }

    public boolean isUserAlreadyInEvent(String userId, String eventId) {
        for (Volunteer volunteer: volunteers) {
            if (volunteer.getUserId().equals(userId) && volunteer.getEventId().equals(eventId)) {
                return true;
            }
        }
        return false;
    }

    public void setLeaderByUserIdAndEventIdAndTeamIdAndSetFalseAnother(String userId, String eventId, String teamId, boolean status) {
        for (Volunteer volunteer: volunteers) {
            if (volunteer.isLeaderStatus()) {
                volunteer.setLeaderStatus(!status);
            }
        }
        setLeaderByUserIdAndEventIdAndTeamId(userId, eventId,teamId, status);
    }

    public void setLeaderByUserIdAndEventIdAndTeamId(String userId, String eventId, String teamId, boolean status) {
        Volunteer exist = findVolunteerByUserIdEventIdTeamId(userId, eventId, teamId);
        if (exist != null) {
            exist.setLeaderStatus(status);
        }
    }

    public ArrayList<Volunteer> getVolunteers() {
        return volunteers;
    }
}
