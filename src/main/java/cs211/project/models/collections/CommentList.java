package cs211.project.models.collections;

import cs211.project.models.Comment;

import java.util.ArrayList;

public class CommentList {
    private ArrayList<Comment> comments;
    public CommentList() {
        comments = new ArrayList<>();
    }
    public void addNewComment(String eventId, String teamId, String userNameId, String time,String message) {
        eventId = eventId.trim();
        teamId = teamId.trim();
        userNameId = userNameId.trim();
        time = time.trim();
        message = message.trim();
        if(!eventId.equals("") && !teamId.equals("") && !userNameId.equals("") && !time.equals("") && !message.equals("")){
                comments.add(new Comment(eventId,teamId,userNameId,time,message));
        }
    }

    public ArrayList<Comment> getComments() {return comments;}
}
