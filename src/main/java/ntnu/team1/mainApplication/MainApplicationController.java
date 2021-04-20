package ntnu.team1.mainApplication;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ntnu.team1.application.task.Category;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import ntnu.team1.application.task.Category;

/**
 * Class that handles the main view of the application
 */

public class MainApplicationController {

    public MenuItem menuEditAdd;
    @FXML
    private MenuItem menuHelpAbout;

    @FXML
    private AnchorPane view;

    @FXML
    private VBox categoryButtonList;

    private String currentView="todo";

    @FXML
    private void update(){
        generateCategoryList();
    }


    public void generateCategoryList(){
        ObservableList<Category> list  = FXCollections.observableList(new ArrayList<>(App.getRegister().getCategories().values()));
        categoryButtonList.getChildren().clear();
        if(App.getRegister().getAllTasks().stream().anyMatch(MainTask -> !MainTask.hasCategory())){
            Button noCategory = new Button("No category");
            noCategory.setOnAction(actionEvent ->  {
                try {
                    showByCategory(-1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            categoryButtonList.getChildren().add(noCategory);
        }

        for(Category c : list){
            Button button = new Button(c.getName());
            button.setId(c.getName());
            button.setOnAction(actionEvent ->  {
                try {
                    showByCategory(c.getID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            categoryButtonList.getChildren().add(button);
        }
    }

    public void showByCategory(int id) throws IOException {
        view.getChildren().clear();
        App.setChosenCategory(id);
        Pane newLoadedPane = FXMLLoader.load(MainApplicationController.class.getResource("task/showByCategory.fxml"));
        view.getChildren().add(newLoadedPane);
    }

    public void initialize() throws IOException {
        view.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("task/taskList.fxml"));
        view.getChildren().add(newLoadedPane);
        menuHelpAbout.setOnAction(showAbout());
        generateCategoryList();
        Platform.runLater(this::generateCategoryList);
    }

    @FXML
    public void switchToTasks() throws IOException {
        view.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("task/taskList.fxml"));
        view.getChildren().add(newLoadedPane);
    }

    @FXML
    public void switchToCategory() throws IOException {
        view.getChildren().clear();
        Pane categoryPane = FXMLLoader.load(getClass().getResource("category/categoryList.fxml"));
        currentView="category";
        view.getChildren().add(categoryPane);
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
    }

    @FXML
    public void addNewCategory() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource( "category/newCategory.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 400,364);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        generateCategoryList();
    }

    @FXML
    public void edit(){
    }

    @FXML
    private EventHandler<ActionEvent> showAbout() {
        return event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("About");
            alert.setHeaderText("To-Do-List Application");
            alert.setContentText("Version 1.0\n" +
                    "Made by Team 1.1" +
                    "\n2021 \u00A9");
            alert.showAndWait();
        };
    }

    @FXML
    public void close(){
        Platform.exit();
    }
}
