package cs211.project.services;

import cs211.project.models.Volunteer;
import cs211.project.models.collections.VolunteerList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class VolunteerListDataSource implements DataSource<VolunteerList>{
    private String directoryName;
    private String fileName;
    public VolunteerListDataSource(String directoryName, String fileName) {
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
    public VolunteerList readData() {
        VolunteerList volunteerList = new VolunteerList();
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
                String teamId = data[4].trim();
                boolean leaderStatus = Boolean.parseBoolean(data[5].trim());
                volunteerList.addNewVolunteer(userId,eventId,banStatus,role,teamId,leaderStatus);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return volunteerList;
    }

    @Override
    public void writeData(VolunteerList volunteerList) {
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
            for (Volunteer volunteer : volunteerList.getVolunteers()) {
                String line = volunteer.toCSV();
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
