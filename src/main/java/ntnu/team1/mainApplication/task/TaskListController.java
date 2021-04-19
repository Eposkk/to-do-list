package ntnu.team1.mainApplication.task;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ntnu.team1.application.MainRegister;
import ntnu.team1.application.task.MainTask;
import ntnu.team1.mainApplication.App;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskListController {

    public AnchorPane Pane;
    @FXML
    private javafx.scene.control.TableView<MainTask> tableView;

    @FXML
    private TableColumn<MainTask, Boolean> doneColumn;

    @FXML
    private TableColumn<MainTask, String> nameColumn;

    @FXML
    private TableColumn<MainTask, String> descriptionColumn;

    @FXML
    private TableColumn<MainTask, LocalDate> startDateColumn;

    @FXML
    private TableColumn<MainTask, LocalDate> endDateColumn;

    @FXML
    private TableColumn<MainTask, Integer> priorityColumn;

    @FXML
    private TableColumn<MainTask, Integer> categoryColumn;

    @FXML
    private TableColumn<MainTask, Button> deleteButtonColumn;

    @FXML
    private ToggleGroup choice;

    @FXML
    private Label header;




    public void initialize(){
        choice.selectedToggleProperty().addListener((observableValue, toggle, t1) -> updateList());
        header.setText("Viewing all tasks");
        columFactory();
        updateList();
    }

    @FXML
    private void addNewTask() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource( "task/newTask.fxml"));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent, 800, 600);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Add new task");
        stage.getIcons().add(new Image(new FileInputStream("src/main/resources/Images/Plus.png")));
        stage.showAndWait();
        updateList();
    }

    @FXML
    private void editTask() throws IOException{
        App.getRegister().setSelectedTask(tableView.getSelectionModel().getSelectedItem());

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("task/editTask.fxml"));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent, 800, 600);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("Edit task");
        stage.getIcons().add(new Image(new FileInputStream("src/main/resources/Images/edit.png")));
        stage.showAndWait();
        tableView.getItems().clear();
        initialize();
    }

    @FXML
    private void removeTask(){
        MainRegister result = App.getRegister();
        result.removeMainTask(tableView.getSelectionModel().getSelectedItem().getID());
        App.setRegister(result);
        updateList();
    }

    private void columFactory(){
        doneColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        doneColumn.setCellValueFactory(cellData -> {
            MainTask task = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(task.isDone());

            property.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) ->{
                MainRegister register = App.getRegister();
                register.getMainTask(task.getID()).setDone(newValue);
                App.setRegister(register);
                updateList();
            });
            return property;
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        deleteButtonColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
    }

    void updateList(){
        RadioButton r = (RadioButton) choice.getSelectedToggle();
        String selected = r.getText();
        ObservableList<MainTask> list = null;
        switch (selected) {
            case "all":
                list = FXCollections.observableList(new ArrayList<>(App.getRegister().getAllTasks()));
                break;
            case "done":
                list = FXCollections.observableList(App.getRegister().getAllTasks().stream()
                        .filter(MainTask::isDone)
                        .collect(Collectors.toList()));
                break;
            case "active":
                list = FXCollections.observableList(App.getRegister().getAllTasks().stream()
                        .filter(MainTask -> !MainTask.isDone())
                        .collect(Collectors.toList()));
                break;
        }
        tableView.setItems(list);
    }


}
