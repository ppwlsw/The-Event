package cs211.project.cs211661project;

import cs211.project.services.FXRouter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        configRoute();
        FXRouter.bind(this, stage, "CS211 661 Project");
        FXRouter.goTo("hello");

        String cssPath = getClass().getResource("/design/design.css").toExternalForm();
        Scene scene = stage.getScene();
        scene.getStylesheets().add(cssPath);
    }

    private static void configRoute() {
        String resourcesPath = "cs211/project/views/";
        FXRouter.when("hello", resourcesPath + "hello-view.fxml");
        FXRouter.when("credit", resourcesPath + "credit-page.fxml");
        FXRouter.when("guide", resourcesPath + "guide-page.fxml");
        FXRouter.when("login", resourcesPath + "login-page.fxml");
        FXRouter.when("register", resourcesPath + "register-page.fxml");
        FXRouter.when("user-hub", resourcesPath + "user-hub-page.fxml");
        FXRouter.when("join-event",resourcesPath + "join-event-page.fxml");
        FXRouter.when("manage-event", resourcesPath + "your-event-page.fxml");
        FXRouter.when("history-event", resourcesPath + "history-event-page.fxml");
        FXRouter.when("account-setting" , resourcesPath + "account-setting-page.fxml");
        FXRouter.when("create-event", resourcesPath + "create-event-page.fxml");
        FXRouter.when("admin-page", resourcesPath + "admin-page.fxml");
        FXRouter.when("admin-setting", resourcesPath + "admin-settings-page.fxml");
        FXRouter.when("my-event",resourcesPath + "your-event-page.fxml");
        FXRouter.when("manage-event", resourcesPath + "manage-event-page.fxml");
        FXRouter.when("participator", resourcesPath + "participator-page.fxml");
        FXRouter.when("event-schedule", resourcesPath + "event-schedule.fxml");
        FXRouter.when("edit-schedule", resourcesPath + "edit-schedule.fxml");
        FXRouter.when("create-schedule", resourcesPath + "create-schedule.fxml");
        FXRouter.when("create-team",resourcesPath + "create-team-page.fxml");
        FXRouter.when("join-team", resourcesPath + "team-joining-page.fxml");
        FXRouter.when("show-event",resourcesPath + "show-all-event.fxml");
        FXRouter.when("schedule-team",resourcesPath + "team-schedule.fxml");
        FXRouter.when("comment",resourcesPath + "comment-page.fxml");
        FXRouter.when("manage-team",resourcesPath + "manage-team-page.fxml");
        FXRouter.when("team-member",resourcesPath + "team-member-page.fxml");
        FXRouter.when("member-leader",resourcesPath + "main-team-page.fxml");
        FXRouter.when("show-member", resourcesPath + "event-member-page.fxml");
    }


    public static void main(String[] args) {
        launch();
    }
}