package cs211.project.services;

import cs211.project.models.Comment;
import cs211.project.models.collections.CommentList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CommentListDataSource implements DataSource<CommentList>{
    private String directoryName;
    private String fileName;
    public CommentListDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CommentList readData() {
        CommentList commentList = new CommentList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.equals("")) continue;
                String[] data = line.split(",");
                String eventId = data[0].trim();
                String teamName = data[1].trim();
                String userNameId = data[2].trim();
                String time = data[3].trim();
                String message = data[4].trim();
                commentList.addNewComment(eventId, teamName, userNameId, time, message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return commentList;
    }

    @Override
    public void writeData(CommentList commentList) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            for (Comment comment : commentList.getComments()) {
                String line = comment.getEventId() + ", " + comment.getTeamId() + ", " + comment.getUserName() + ", " + comment.getTime() + ", " + comment.getMessage();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
