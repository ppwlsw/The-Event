package cs211.project.controllers;

import cs211.project.models.collections.UserList;
import cs211.project.services.DataSource;
import cs211.project.services.FXRouter;
import cs211.project.services.UserListFileDataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;


public class RegisterController{

    @FXML private TextField nameTextField;
    @FXML private TextField usernameTextField;
    @FXML private Label errorLabel;
    @FXML private PasswordField confirmTextFields;
    @FXML private PasswordField passwordTextFields;
    @FXML private Circle circle;
    @FXML
    ActionEvent event;
    @FXML
    private DataSource<UserList> datasource;

    @FXML
    private UserList userList;
    @FXML
    private String imagePath;


    @FXML
    public void initialize() {
        errorLabel.setText("");
        datasource = new UserListFileDataSource("data", "user-list.csv");
        userList = datasource.readData();
        imagePath = "images/user-default-image.png";
        Image image = new Image("file:"+ imagePath);
        circle.setFill(new ImagePattern(image));
    }
    @FXML
    public void goToHelloPage() {
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void registerButton(ActionEvent event) {
        this.event = event;
        String password = passwordTextFields.getText().trim();
        String confirmPassword = confirmTextFields.getText().trim();
        String name = nameTextField.getText().trim();
        String username = usernameTextField.getText().trim();

        if(userList.findUserByUserName(username) != null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR !");
            alert.setHeaderText("ERROR USERNAME");
            alert.setContentText("This username already exist.");
            alert.showAndWait();
        }
        else {

            if (name.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR !");
                alert.setHeaderText("ERROR NAME");
                alert.setContentText("Please enter your name.");
                alert.showAndWait();
            } else if (username.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR !");
                alert.setHeaderText("ERROR USERNAME");
                alert.setContentText("Invalid your username.");
                alert.showAndWait();
            } else if (password.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR !");
                alert.setHeaderText("ERROR PASSWORD");
                alert.setContentText("Type your password.");
                alert.showAndWait();
            } else if (confirmPassword.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR !");
                alert.setHeaderText("ERROR CONFIRM PASSWORD");
                alert.setContentText("Check your confirm password.");
                alert.showAndWait();
            } else if (!password.equals(confirmPassword)) {
                String errorMessage = "Password doesn't match";
                errorLabel.setText(errorMessage);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR !");
                alert.setHeaderText("ERROR CHECK");
                alert.setContentText("Check your Password or ID");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Already Registered");
                alert.setHeaderText("Register Successfully!");
                alert.showAndWait();

                try {
                    userList.addNewUser(name, username, password, imagePath);

                    datasource.writeData(userList);
                    FXRouter.goTo("hello");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @FXML
    public void changeImageButton(ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath()+System.getProperty("file.separator")+filename
                );
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                imagePath = destDir + "/" + filename;
                Image img = new Image(target.toUri().toString());
                circle.setFill(new ImagePattern(img));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
