package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDataSource;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminSettingsController {
    @FXML
    TextField passwordTextField;
    @FXML
    TextField newPasswordTextField;
    @FXML
    TextField confirmPasswordTextField;
    User adminAccount;
    UserListFileDataSource userListFileDataSource;
    UserList userList;
    public void initialize() {
        userListFileDataSource = new UserListFileDataSource("data", "user-list.csv");
        userList = userListFileDataSource.readData();
        adminAccount = (User) FXRouter.getData();
    }

    public void onConfirmButton() {
        String oldPassword = passwordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        if (adminAccount.validatePassword(oldPassword) && !newPassword.equals("") && newPassword.equals(confirmPassword)) {
            User exist = userList.findUserByID(adminAccount.getUserId());
            if (exist != null) {
                exist.setPassword(newPassword);
                userListFileDataSource.writeData(userList);
                backToAdminPage();
            }
        }
    }
    @FXML
    public void backToAdminPage() {
        try {
            FXRouter.goTo("admin-page");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
