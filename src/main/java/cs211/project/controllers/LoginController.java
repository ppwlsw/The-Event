package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;
import cs211.project.services.DataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDataSource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.time.LocalDateTime;


public class LoginController {
    private UserList userList;

    @FXML
    private Rectangle rectangle;
    @FXML
    public void goToHelloPage() {
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Label errorLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passWordTextField;

    DataSource<UserList> datasource;
    @FXML
    public void initialize() {
        rectangle.setArcHeight(40);
        rectangle.setArcWidth(40);
        datasource = new UserListFileDataSource("data", "user-list.csv");

        errorLabel.setText("");
        userList = datasource.readData();
        onKey();
    }

    private void onKey() {
        userNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passWordTextField.requestFocus();
            }
        });
        passWordTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton();
            }
        });
    }

    @FXML
    public void loginButton() {
        int check = 0;
        String username = userNameTextField.getText().trim();
        String password = passWordTextField.getText().trim();
        try {
            User user = userList.findUserByUserName(username);
            if (user != null) {
                if (user.isValidUsername(username) && user.validatePassword(password) && !user.isBannedStatus()) {
                    userList.setLoginTimeById(user.getUserId(), LocalDateTime.now());
                    datasource.writeData(userList);
                    if (user.getRole().equals("admin")) {
                        FXRouter.goTo("admin-page", user);
                    } else {
                        FXRouter.goTo("show-event",user);
                    }
                    check = 1;
                }
                if(user.isBannedStatus()){
                    check = 2;
                }
            }
            if (check == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login is Failed.");
                alert.setHeaderText("Login Error!");
                alert.showAndWait();
                System.err.println("Can't go to the main page");
                userNameTextField.clear();
                passWordTextField.clear();
            }
            if (check == 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("You have been banned!.");
                alert.setHeaderText("Your account have been banned from this app\nPlease contact admin ");
                alert.showAndWait();
                System.err.println("Can't go to the main page");
                userNameTextField.clear();
                passWordTextField.clear();
            }

        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }



}
