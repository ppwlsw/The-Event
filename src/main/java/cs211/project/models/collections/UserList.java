package cs211.project.models.collections;

import cs211.project.models.User;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class UserList {
    private ArrayList<User> users;
    public UserList() {
        users = new ArrayList<>();
    }


    public UserList(ArrayList<User> users) {
        this.users = users;
    }

    public void addNewUser(String id, String name, String username, String password,String role,String userImageResource,LocalDateTime loginDateTime, boolean bannedStatus){
        // use in readData()
        name = name.trim();
        username = username.trim();
        password = password.trim();
        role = role.trim();
        userImageResource = userImageResource.trim();
        if(!name.equals("") && !username.equals("") && !password.equals("") && !role.equals("") && !userImageResource.equals("")){
            User exist = findUserByUserName(username);
            if(exist == null){
                users.add(new User(id,name,username,password,role,userImageResource,loginDateTime,bannedStatus));
            }
        }
    }
    public void addNewUser(String name, String username, String password, String userImageResource){
        // register as a common user
        String id = UUID.randomUUID().toString();
        name = name.trim();
        username = username.trim();
        password = password.trim();
        userImageResource = userImageResource.trim();
        if(!name.equals("") && !username.equals("") && !password.equals("") && !userImageResource.equals("")){
            User exist = findUserByUserName(username);
            if(exist == null){
                users.add(new User(id,name,username,password,userImageResource,LocalDateTime.now()));
            }
        }
    }

    public void banUserById(String id) {
        User exist = findUserByID(id);
        if (exist != null) {
            exist.setBannedStatus(true);
        }
    }

    public void unbanUserById(String id) {
        User exist = findUserByID(id);
        if (exist != null) {
            exist.setBannedStatus(false);
        }
    }



    public void setLoginTimeById(String id, LocalDateTime loginTime){
        User exist = findUserByID(id);
        if(exist != null){
            exist.setLoginDateTime(loginTime);
        }
    }
    public void changePasswordById(String id , String oldPassword , String newPassword){
        User exist = findUserByID(id);
        if(exist != null && exist.validatePassword(oldPassword)){
            exist.setPassword(newPassword);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Change password successfully");
            alert.setHeaderText("Your password has change successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Unable to change your password");
            alert.setHeaderText("Your old password doesn't match");
            alert.showAndWait();

        }

    }

    public void changeProfileImageById(String id , String imagePath){
        User exist = findUserByID(id);
        imagePath = imagePath.trim();
        if (exist != null) {
            exist.setUserImageResource(imagePath);
        }
    }
    public User findUserByUserName(String username){
        for(User user: users){
            if(user.isValidUsername(username)){
                return user;
            }
        }
        return null;
    }
    public User findUserByID(String id){
        for(User user : users){
            if(user.isValidId(id)){
                return user;
            }
        }
        return null;
    }
    public void sortUserByLogintime() {
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return -o1.getLoginDateTime().compareTo(o2.getLoginDateTime());
            }
        });

    }
    public ArrayList<User> getUsers(){
        return users;
    }


}