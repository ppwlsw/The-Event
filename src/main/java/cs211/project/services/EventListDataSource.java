package cs211.project.services;

import cs211.project.models.Event;
import cs211.project.models.collections.EventList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EventListDataSource implements DataSource<EventList> {
    private String directoryName;
    private String fileName;

    public EventListDataSource(String directoryName, String fileName) {
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
    public EventList readData() {
        EventList events = new EventList();
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
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;
                String[] data = line.split(",");
                String id = data[0].trim();
                String name = data[1].trim();
                String description = data[2].trim();
                String startingDate = data[3].trim();
                String endingDate = data[4].trim();
                String imageString = data[5].trim();
                String userId = data[6].trim();
                int participants = Integer.parseInt(data[7].trim());
                int maxParticipants = Integer.parseInt(data[8].trim());
                String dateOpen = data[9].trim();
                String dateEnd = data[10].trim();
                events.addNewEvent(id, name, description, startingDate, endingDate, imageString, userId, participants, maxParticipants, dateOpen, dateEnd);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    @Override
    public void writeData(EventList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
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
            // สร้าง csv ของ Student และเขียนลงในไฟล์ทีละบรรทัด
            for (Event event : data.getEvents()) {
                String line =
                        event.getId() + ", " + event.getEventName() + ", " + event.getDescription() + ", " +
                        event.getDateStart() + ", " + event.getDateEnd() + ", " + event.getEventImagePath() + ", " +
                        event.getUserId() + ", " + event.getParticipants() + ", " + event.getMaxParticipants() + ", " +
                        event.getDateOpenJoin() + ", " + event.getDateCloseJoin();

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
