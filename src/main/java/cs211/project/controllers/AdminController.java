package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDataSource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminController {
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ImageView userImageView;


    private UserList userList;

    private User selectedUser;
    private UserListFileDataSource dataSource;
    @FXML
    public void initialize() {
        clearUserInfo();
        dataSource = new UserListFileDataSource("data", "user-list.csv");
        userList = dataSource.readData();
        userList.sortUserByLogintime();
        showTable(userList);
        usersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                if (newValue == null) {
                    clearUserInfo();
                    selectedUser = null;
                } else {
                    showUserInfo(newValue);
                    selectedUser = newValue;
                }
            }
        });
    }

    @FXML
    private void backToHelloPage() {
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showUserInfo(User user) {
        LocalDateTime userLoginTimeLocalDateTime = (LocalDateTime) user.getLoginDateTime();
        idLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        timeLabel.setText(userLoginTimeLocalDateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")));
        userImageView.setImage(new Image("file:"+user.getUserImageResource()));
    }


    private void clearUserInfo() {
        idLabel.setText("");
        nameLabel.setText("");
        timeLabel.setText("");
        userImageView.setImage(null);
    }

    @FXML
    private void banUserButton() {
        if (selectedUser != null) {
            userList.banUserById(selectedUser.getUserId());
            dataSource.writeData(userList);
            showTable(userList);
        }
    }

    @FXML
    private void userUnbannedButton() {
        if (selectedUser != null) {
            userList.unbanUserById(selectedUser.getUserId());
            dataSource.writeData(userList);
            showTable(userList);
        }
    }
    private void showTable(UserList userList) {
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> banColumn = new TableColumn<>("Ban Status");
        banColumn.setCellValueFactory(new PropertyValueFactory<>("bannedStatus"));




        usersTableView.getColumns().clear();
        usersTableView.getColumns().add(usernameColumn);
        usersTableView.getColumns().add(nameColumn);
        usersTableView.getColumns().add(banColumn);


        usersTableView.getItems().clear();



        for(User user: userList.getUsers()) {
            if (!user.getRole().equals("admin"))
                usersTableView.getItems().add(user);
        }

    }

    @FXML
    public void goToAdminSetting() {
        try {
            FXRouter.goTo("admin-setting", FXRouter.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
