module cs211.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;

    opens cs211.project.cs211661project to javafx.fxml;
    exports cs211.project.cs211661project;
    exports cs211.project.controllers;
    opens cs211.project.controllers to javafx.fxml;
    opens cs211.project.models to javafx.base;

}