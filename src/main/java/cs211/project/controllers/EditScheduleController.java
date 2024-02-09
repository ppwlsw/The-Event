package cs211.project.controllers;

import cs211.project.models.Event;
import cs211.project.models.EventSchedule;
import cs211.project.models.collections.EventList;
import cs211.project.models.collections.EventScheduleList;
import cs211.project.services.EventListDataSource;
import cs211.project.services.EventScheduleListDataSource;
import cs211.project.services.FXRouter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class EditScheduleController {
    @FXML
    private TableView<EventSchedule> scheduleTableView;
    @FXML
    ImageView eventImageView;
    private EventListDataSource eventListDataSource;
    private EventList eventList;
    private Event currentEvent;
    private String eventId;
    String eventImagePath;
    private EventScheduleList eventScheduleList;
    private EventScheduleListDataSource dataSource;

    @FXML
    public void initialize() {
        eventId = (String) FXRouter.getData();
        dataSource = new EventScheduleListDataSource("data", "schedule-list.csv");
        eventScheduleList = dataSource.readData();
        eventListDataSource = new EventListDataSource("data", "event-list.csv");
        eventList = eventListDataSource.readData();
        currentEvent = eventList.findEventById(eventId);
        eventImageView.setImage(new Image("file:" + currentEvent.getEventImagePath()));
        enableDragAndDropSorting();
        scheduleTableView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                EventSchedule selectedEventSchedule = scheduleTableView.getSelectionModel().getSelectedItem();
                if (selectedEventSchedule != null) {
                    Dialog dialog = new Dialog();
                    dialog.setTitle("Delete");
                    dialog.getDialogPane().setContent(deleteForm(selectedEventSchedule, dialog));
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
                    closeButton.managedProperty().bind(closeButton.visibleProperty());
                    closeButton.setVisible(false);
                    // Set Enter key to close the dialog
                    dialog.getDialogPane().getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if (keyEvent.getCode() == KeyCode.ENTER) {
                                dialog.setResult(ButtonType.CLOSE);
                                keyEvent.consume();
                            }
                        }
                    });
                    dialog.showAndWait();
                }
            }
        });
        showTable(eventScheduleList);
    }


    private Node deleteForm(EventSchedule selectedEventSchedule, Dialog dialog) {
        GridPane gridPane = new GridPane();
        Label confirmationLabel = new Label("Are you sure you want to delete this schedule?");
        Button deleteButton = new Button("Delete");

        // Apply styling to the Delete button
        deleteButton.setStyle("-fx-background-color: #D70040; -fx-text-fill: white;");

        deleteButton.setOnAction(event -> {
            if (selectedEventSchedule != null) {
                // Delete the selected student
                eventScheduleList.deleteScheduleByEventIdAndName(eventId, selectedEventSchedule.getName());

                // Update the TableView
                scheduleTableView.getItems().remove(selectedEventSchedule);

                // Clear the selectedEventSchedule field
                dataSource.writeData(eventScheduleList);

                dialog.setResult(ButtonType.CLOSE);
            }
        });

        // Add elements to the GridPane
        gridPane.add(confirmationLabel, 0, 0);
        gridPane.add(deleteButton, 1, 1);

        // Add spacing and alignment if needed
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);


        return gridPane;
    }

    private void enableDragAndDropSorting() {
        scheduleTableView.setRowFactory(tableView -> {
            TableRow<EventSchedule> row = new TableRow<>();
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(index.toString());
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    int draggedIndex = Integer.parseInt(db.getString());
                    if (row.isEmpty() || row.getIndex() != draggedIndex) {
                        event.acceptTransferModes(TransferMode.MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    int draggedIndex = Integer.parseInt(db.getString());
                    ObservableList<EventSchedule> items = scheduleTableView.getItems();
                    EventSchedule draggedEventSchedule = items.remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = items.size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    items.add(dropIndex, draggedEventSchedule);
                    event.setDropCompleted(true);
                    scheduleTableView.getSelectionModel().select(dropIndex);
                    event.consume();

                    ObservableList<EventSchedule> currentOrder = FXCollections.observableArrayList(scheduleTableView.getItems());


                    eventScheduleList.getSchedules().removeIf(schedule -> schedule.getEventId().equals(eventId));
                    eventScheduleList.getSchedules().addAll(currentOrder);

                    dataSource.writeData(eventScheduleList);

                }
            });

            return row;
        });
    }


    private void showTable(EventScheduleList eventScheduleList) {
        // start time column
        TableColumn<EventSchedule, String> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        // end time column
        TableColumn<EventSchedule, String> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        // name column
        TableColumn<EventSchedule, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // detail column
        TableColumn<EventSchedule, String> detailColumn = new TableColumn<>("Detail");
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

        scheduleTableView.getColumns().clear();
        scheduleTableView.getColumns().add(startTimeColumn);
        scheduleTableView.getColumns().add(endTimeColumn);
        scheduleTableView.getColumns().add(nameColumn);
        scheduleTableView.getColumns().add(detailColumn);
        scheduleTableView.getItems().clear();

        for (EventSchedule eventSchedule : eventScheduleList.getSchedules()) {
            if (eventSchedule.getEventId().equals(eventId))
                scheduleTableView.getItems().add(eventSchedule);
        }
    }
    @FXML
    public void onChangeImageButtonClick(ActionEvent event) {
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
                eventImageView.setImage(new Image(target.toUri().toString()));
                eventImagePath = destDir + "/" + filename;
                eventList.changeEventImageById(currentEvent.getId(), eventImagePath);
                eventListDataSource.writeData(eventList);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void onBackToMyEventPageButton() {
        try {
            FXRouter.goTo("my-event");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onScheduleCilck() {
        try {
            FXRouter.goTo("edit-schedule", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onVolunteerButtonCLick(){
        try{
            FXRouter.goTo("create-team", eventId);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onParticipatorButtonClick() {
        try {
            FXRouter.goTo("participator", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onDetailButtonClick() {
        try {
            FXRouter.goTo("manage-event" , eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onClickCreateSchedule() {
        try {
            FXRouter.goTo("create-schedule", eventId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
