package cs211.project.controllers;

import cs211.project.models.User;
import cs211.project.models.collections.UserList;
import cs211.project.services.DataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountSettingController {
    @FXML
    ImageView profileImageView;
    @FXML
    Label nameLabel;
    @FXML
    Label lastestLoginLabel;
    @FXML
    Label oldPasswordLabel;
    @FXML
    Label newPasswordLabel;
    @FXML
    Button chageProfileButton;
    @FXML
    Button goToUserHubButton;
    @FXML
    Button changePasswordButton;
    @FXML
    Button changeNewPasswordButton;
    @FXML
    Label usernameLabel;
    @FXML
    TextField newPasswordTextField;
    @FXML
    TextField oldPasswordTextField;
    @FXML
    Label changePasswordLabel;
    @FXML
    private boolean changePasswordVisible = false;
    private User currentUser;
    private UserList userList;
    DataSource<UserList> datasource;
    String userImagePath;
    Image image;

    @FXML
    void initialize(){
        datasource = new UserListFileDataSource("data", "user-list.csv");
        userList = datasource.readData();
        currentUser = (User) FXRouter.getData();
        nameLabel.setText(currentUser.getName());
        usernameLabel.setText(currentUser.getUsername());
        LocalDateTime userLoginTime = currentUser.getLoginDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        String dateTime = userLoginTime.format(formatter);
        lastestLoginLabel.setText(dateTime);
        userImagePath = "file:" + currentUser.getUserImageResource();
        image = new Image(userImagePath);
        profileImageView.setImage(image);

        newPasswordTextField.setVisible(false);
        oldPasswordTextField.setVisible(false);
        changePasswordLabel.setVisible(false);
        oldPasswordLabel.setVisible(false);
        newPasswordLabel.setVisible(false);
        changeNewPasswordButton.setVisible(false);
    }

    @FXML
    public void onChangePasswordButton() {
        changePasswordVisible = !changePasswordVisible;
        oldPasswordLabel.setVisible(changePasswordVisible);
        newPasswordLabel.setVisible(changePasswordVisible);
        changePasswordLabel.setVisible(changePasswordVisible);
        newPasswordTextField.setVisible(changePasswordVisible);
        oldPasswordTextField.setVisible(changePasswordVisible);
        changeNewPasswordButton.setVisible(changePasswordVisible);
    }

    @FXML
    public void goToUserHubClick(){
        try {
            FXRouter.goTo("user-hub", currentUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onChangeNewPasswordButton() {
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        userList.changePasswordById(currentUser.getUserId(), oldPassword, newPassword);
        datasource.writeData(userList);
        oldPasswordTextField.clear();
        newPasswordTextField.clear();
    }

    @FXML
    public void onChangeNewProfileImage(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null) {
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_" + System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath() + System.getProperty("file.separator") + filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                profileImageView.setImage(new Image(target.toUri().toString()));
                userImagePath = destDir + "/" + filename;
                currentUser.setUserImageResource(userImagePath);
                userList.changeProfileImageById(currentUser.getUserId(), userImagePath);
                datasource.writeData(userList);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}