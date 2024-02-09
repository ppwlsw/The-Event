package cs211.project.services;


import cs211.project.models.Team;
import cs211.project.models.collections.TeamList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TeamListDataSource implements DataSource<TeamList>{
    private String directoryName;
    private String fileName;
    public TeamListDataSource(String directoryName, String fileName) {
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
    public TeamList readData() {
        TeamList teamList = new TeamList();
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
                String teamId = data[0].trim();
                String eventId = data[1].trim();
                String teamName = data[2].trim();
                String dateTimeOpenToJoin = data[3].trim();
                String dateTimeCloseToJoin = data[4].trim();
                int member = Integer.parseInt(data[5].trim());
                int maxMember = Integer.parseInt(data[6].trim());
                teamList.addNewTeam(teamId,eventId,teamName,dateTimeOpenToJoin,dateTimeCloseToJoin,member,maxMember);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return teamList;
    }

    @Override
    public void writeData(TeamList teamList) {
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
            for (Team team : teamList.getTeams()) {
                String line = team.getTeamId() + "," + team.getEventId() + "," + team.getTeamName() + "," + team.getDateTimeOpenToJoin()
                            + "," + team.getDateTimeCloseToJoin() +  "," + team.getMember() + "," + team.getMaxMember() ;
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
