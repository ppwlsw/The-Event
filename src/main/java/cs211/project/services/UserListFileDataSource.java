package cs211.project.services;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UserListFileDataSource implements DataSource<UserList>{
    private String directoryName;
    private String fileName;
    public UserListFileDataSource(String directoryName, String fileName) {
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
    public UserList readData() {
        UserList userList = new UserList();
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
                String id = data[0].trim();
                String name = data[1].trim();
                String username = data[2].trim();
                String password = data[3].trim();
                String role = data[4].trim();
                String userImageResource = data[5].trim();
                LocalDateTime loginDateTime = LocalDateTime.parse(data[6].trim());
                boolean bannedStatus = Boolean.parseBoolean(data[7].trim());
                userList.addNewUser(id, name, username, password,role,userImageResource,loginDateTime,bannedStatus) ;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userList;

    }

    @Override
    public void writeData(UserList userList) {
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
            for (User user : userList.getUsers()) {
                String line = user.getUserId() + "," + user.getName() + "," + user.getUsername()+ ","+user.getPassword()+ ","+user.getRole()+ ","+user.getUserImageResource()+ "," + user.getLoginDateTime().toString()+ ","+ user.isBannedStatus();
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