package cs211.project.models.collections;

import cs211.project.models.Participant;

import java.util.ArrayList;

public class ParticipantList {
    private ArrayList<Participant> participants;
    public ParticipantList() {
        participants = new ArrayList<>();
    }
    public void addNewParticipant(String userId, String eventId, int banStatus, String role) {
        userId = userId.trim();
        eventId = eventId.trim();
        role = role.trim();
        Participant exist = findParticipantByUserIdAndEventId(userId, eventId);
        if (exist == null && !userId.equals("") && !eventId.equals("") && !role.equals("")) {
            participants.add(new Participant(userId, eventId, banStatus, role));
        }
    }
    public void addNewParticipant(String userId, String eventId) {
        userId = userId.trim();
        eventId = eventId.trim();
        Participant exist = findParticipantByUserIdAndEventId(userId, eventId);
        if (exist == null && !userId.equals("") && !eventId.equals("")) {
            participants.add(new Participant(userId, eventId, 0, "participator"));
        }
    }

    public Participant findParticipantByUserIdAndEventId(String userId, String eventId) {
        for (Participant participant: participants) {
            if (participant.isUserId(userId) && participant.isEventId(eventId)) {
                return participant;
            }
        }
        return null;
    }

    public void setBannedStatusParticipantByUserIdAndEventId(String userId, String eventId, int status) {
        Participant exist = findParticipantByUserIdAndEventId(userId, eventId);
        if (exist != null) {
            exist.setBannedStatus(status);
        }
    }


    public ArrayList<Participant> getParticipants() {
        return participants;
    }
}
