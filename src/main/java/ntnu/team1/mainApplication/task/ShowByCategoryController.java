/**
 * Contains all files used for creating, editing, removing and viewing tasks
 */
package ntnu.team1.mainApplication.task;

import com.sun.tools.javac.Main;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import ntnu.team1.application.MainRegister;
import ntnu.team1.application.task.MainTask;
import ntnu.team1.mainApplication.App;
import ntnu.team1.mainApplication.RegisterModifiers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Class used for displaying tasks by catgeory
 */

public class ShowByCategoryController {

    /**
     * Pane used as the upper element
     */
    public AnchorPane Pane;
    /**
     * Button for deleting all tasks in a category
     */
    public Button deleteAllTool;
    /**
     * Button that launches the add new task dialog
     */
    @FXML
    private Button addNewTool;

    /**
     * Button that launches the edit task dialog
     */
    @FXML
    private Button editTool;

    /**
     * Table view used for showing all tasks
     */
    @FXML
    private javafx.scene.control.TableView<MainTask> tableView;

    /**
     * Column where you can mark tasks as done
     */

    @FXML
    private TableColumn<MainTask, Boolean> doneColumn;

    /**
     * Column for the task name
     */

    @FXML
    private TableColumn<MainTask, String> nameColumn;

    /**
     * Column for the task description
     */
    @FXML
    private TableColumn<MainTask, String> descriptionColumn;
    /**
     * Column for the task startDate
     */
    @FXML
    private TableColumn<MainTask, LocalDate> startDateColumn;
    /**
     * Column for the task endDate
     */
    @FXML
    private TableColumn<MainTask, LocalDate> endDateColumn;
    /**
     * Column for the task priority
     */
    @FXML
    private TableColumn<MainTask, Integer> priorityColumn;

    /**
     * Column for info button for each task
     */
    @FXML
    private TableColumn<MainTask, MainTask> infoButtonColumn;

    /**
     * Column for the delete button
     */
    @FXML
    private TableColumn<MainTask, MainTask> deleteButtonColumn;

    /**
     * Toggle group for which tasks you want to show
     */
    @FXML
    private ToggleGroup choice;

    /**
     * Header to indicate which
     */
    @FXML
    private Label header;


    /**
     * Initial method that is called at class loading
     * Sets and creates tableview, creates buttons and configures all other elements
     * @throws FileNotFoundException if file is not found
     */


    public void initialize() throws FileNotFoundException {
        choice.selectedToggleProperty().addListener((observableValue, toggle, t1) -> updateList());
        if(App.getChosenCategory() > -1){
            header.setText("Viewing all tasks in category " + App.getRegister().getCategories().get(App.getChosenCategory()).getName());

        }else{
            header.setText("Viewing all tasks without a given category");
        }
        columFactory();
        makeButtons();
        updateList();
        tableView.getColumns().forEach(this::addTooltipToColumnCells);
    }

    private <T> void addTooltipToColumnCells(TableColumn<MainTask, T> column) {

        Callback<TableColumn<MainTask, T>, TableCell<MainTask, T>> existingCellFactory
                = column.getCellFactory();

        column.setCellFactory(c -> {
            TableCell<MainTask, T> cell = existingCellFactory.call(c);

            Tooltip tooltip = new Tooltip();
            // can use arbitrary binding here to make text depend on cell
            // in any way you need:
            tooltip.textProperty().bind(cell.itemProperty().asString());

            cell.setTooltip(tooltip);
            return cell ;
        });
    }

    /**
     * Method for making buttons
     * @throws FileNotFoundException if file is not found
     */

    private void makeButtons() {
        addNewTool.setTooltip(new Tooltip("Add new task"));

        editTool.setTooltip(new Tooltip(("Edit task")));

        deleteAllTool.setTooltip(new Tooltip(("Delete all tasks in this category")));
    }


    /**
     * Method for adding tasks
     */

    @FXML
    private void addNewTask(){
        RegisterModifiers.addNewTask();
        updateList();
    }

    /**
     * Method for editing tasks
     */

    @FXML
    private void editTask(){
        RegisterModifiers.editTask(tableView.getSelectionModel().getSelectedItem());
        updateList();
    }

    /**
     * Method for removing tasks
     */

    @FXML
    private void removeAllTasks(){
        RegisterModifiers.removeAllTasksInCategory(App.getChosenCategory());
        updateList();
    }

    /**
     * Factory for creating the tableview and adding information
     */

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

        infoButtonColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        infoButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button infoButton = new Button("i");

            @Override
            protected void updateItem(MainTask task, boolean empty) {
                super.updateItem(task, empty);

                if (task == null) {
                    setGraphic(null);
                    return;
                }
                infoButton.setTooltip(new Tooltip("Info/Delete"));
                setGraphic(infoButton);
                infoButton.setOnAction(
                        event -> {
                            RegisterModifiers.editTask(task);
                            updateList();
                        }
                );
            }
        });

        deleteButtonColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(MainTask task, boolean empty) {
                super.updateItem(task, empty);

                if (task == null) {
                    setGraphic(null);
                    return;
                }
                try {
                    staticMethods.addImageToButton("src/main/resources/Images/deleteAll.png", deleteButton,20,20);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                deleteButton.setTooltip(new Tooltip("Delete"));
                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            RegisterModifiers.removeTask(task);
                            updateList();
                        }
                );
            }
        });
    }


    /**
     * Updates the list
     */

    private void updateList(){
        RadioButton r = (RadioButton) choice.getSelectedToggle();
        String selected = r.getText();
        ObservableList<MainTask> list = null;
        switch (selected) {
            case "all":
                list = FXCollections.observableList(App.getRegister().getAllTasks().stream()
                        .filter(MainTask -> MainTask.getCategoryId() == App.getChosenCategory())
                        .collect(Collectors.toList()));
                break;
            case "done":
                list = FXCollections.observableList(App.getRegister().getAllTasks().stream()
                        .filter(MainTask -> MainTask.getCategoryId() == App.getChosenCategory())
                        .filter(MainTask::isDone)
                        .collect(Collectors.toList()));
                break;
            case "active":
                list = FXCollections.observableList(App.getRegister().getAllTasks().stream()
                        .filter(MainTask -> MainTask.getCategoryId() == App.getChosenCategory())
                        .filter(MainTask -> !MainTask.isDone())
                        .collect(Collectors.toList()));
                break;
        }
        tableView.setItems(list);
    }
}
