package cs211.project.services;

import cs211.project.models.Participant;
import cs211.project.models.collections.ParticipantList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ParticipantListDataSource implements DataSource<ParticipantList>{
    private String directoryName;
    private String fileName;
    public ParticipantListDataSource(String directoryName, String fileName) {
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
    public ParticipantList readData() {
        ParticipantList participantList = new ParticipantList();
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
                String userId = data[0].trim();
                String eventId = data[1].trim();
                int banStatus = Integer.parseInt(data[2].trim());
                String role = data[3].trim();
                participantList.addNewParticipant(userId, eventId, banStatus, role);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return participantList;
    }

    @Override
    public void writeData(ParticipantList participantList) {
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
            for (Participant participant : participantList.getParticipants()) {
                String line = participant.getUserId()+", "+participant.getEventId()+", "+participant.getBanStatus()+", "+participant.getRole();
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
