package cs211.project.services;

import cs211.project.models.TeamSchedule;
import cs211.project.models.collections.TeamScheduleList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamScheduleListDataSource implements DataSource<TeamScheduleList> {
    private  String directoryName;
    private String fileName;

    public TeamScheduleListDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if(!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TeamScheduleList readData() {
        TeamScheduleList teamScheduleList = new TeamScheduleList();
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
            while((line = buffer.readLine()) != null) {
                if(line.equals("")) continue;
                String[] data = line.split(",");
                String eventId = data[0].trim();
                String name = data[1].trim();
                String detail = data[2].trim();
                boolean process = Boolean.parseBoolean(data[3].trim());
                String teamId = data[4].trim();
                teamScheduleList.addNewTeamSchedule(eventId, name, detail, process, teamId);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return teamScheduleList;
    }

    @Override
    public void writeData(TeamScheduleList teamScheduleList) {
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
            for(TeamSchedule teamSchedule : teamScheduleList.getSchedules()) {
                String line = teamSchedule.toCsv();
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
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
