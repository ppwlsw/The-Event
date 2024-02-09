package cs211.project.controllers;

import cs211.project.models.Comment;
import cs211.project.models.collections.CommentList;
import cs211.project.services.CommentListDataSource;
import cs211.project.services.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentController{
    @FXML ListView commentListView;
    @FXML Comment comment;
    @FXML TextArea taComment;
    @FXML private CommentList commentList;
    @FXML private DataSource<CommentList> datasource;
    private String eventId, teamId, userName;

    public void setData(String eventid, String teamid, String username) {
        eventId = eventid;
        teamId = teamid;
        userName = username;
        datasource = new CommentListDataSource("data", "comment-list.csv");

        commentList = datasource.readData();
        commentListView.setCellFactory(param -> new WrappedTextCell());
        for (Comment cm: commentList.getComments()) {
            if (cm.isEventId(eventId) && cm.isTeamId(teamId)) {
                commentListView.getItems().add(cm.toString());
            }
        }
        pressEnter();
    }
    @FXML
    public void sendButton() throws IOException {
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
        String message = taComment.getText().trim();
        if (!message.isEmpty()) {
            comment = new Comment(eventId, teamId, userName, timeNow, message);
            commentList.addNewComment(eventId, teamId, userName, timeNow, message);
            datasource.writeData(commentList);
            taComment.clear();
            commentListView.getItems().add(comment.toString());
        }
    }

    @FXML
    private void pressEnter() {

        taComment.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    sendButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private static class WrappedTextCell extends ListCell<String> {
        private final TextFlow textFlow;

        public WrappedTextCell() {
            textFlow = new TextFlow();
            textFlow.setPrefWidth(400);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                Text text = new Text(item);
                text.setWrappingWidth(400);

                textFlow.getChildren().setAll(text);
                setGraphic(textFlow);
            }
        }
    }

}