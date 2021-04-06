package ntnu.team1;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import ntnu.team1.application.task.Category;
import ntnu.team1.application.task.MainTask;
import java.io.IOException;
import java.security.cert.PolicyNode;
import java.time.LocalDate;
import java.util.ArrayList;


public class MainController {

    @FXML
    public ImageView todayImage;
    @FXML
    public ImageView upcomingImage;
    @FXML
    public GridPane gridPane;
    @FXML
    public Label testLabel;
    @FXML
    public TextField newDescriptionTextField;
    public Label todayCategoryText;
    public HBox upcomingCategoryHBox;
    public Label todayCategoryText1;
    public TextArea taskName;
    public TextArea description;
    public DatePicker endDate;
    public Button submitTask;
    public DatePicker startDate;
    public RadioButton pri3;
    public RadioButton pri2;
    public RadioButton pri1;
    public ChoiceBox choiceBox;
    public ToggleGroup priority;
    public VBox showTasks;
    public HBox addNewTaskHBox;
    @FXML
    private VBox mainVBox;

    @FXML
    private Button textGenerateButton;
    @FXML
    private Button addNewTaskButton;
    @FXML
    private Text addNewTaskText;
    @FXML
    private Label todayTitleLabel;
    @FXML
    private Text upcomingCategoryText;
    @FXML
    private HBox todayCategoryHBox;

    MainRegister register = new MainRegister();
    private VBox vBoxPriority;
    ArrayList<Category> categories;
    ArrayList<String> namesOfCategories;


    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    @FXML
    private void addNewTask(){

    }

    @FXML
    private void start(){
        register.addCategory("Test",null);
        ArrayList<Category> categories = new ArrayList<Category>(register.getCategories().values());
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(categories));
        vBoxPriority.getChildren().add(cb);
    }

    @FXML
        private void updateChoiceBox(){
        register.addCategory("Test",null);
        register.addCategory("Skole", null);
        categories =  new ArrayList<>(register.getCategories().values());
        namesOfCategories = new ArrayList<>();


        for (Category c: categories){
            namesOfCategories.add(c.getName());
        }
        choiceBox.setItems(FXCollections.observableArrayList(namesOfCategories));
    }

    @FXML
    private void generateRow() {
        MainTask mainTask = new MainTask(1, LocalDate.now(), LocalDate.of(2021, 03, 26), "Test", "Dette er en test", 2);
        Label newDescription = new Label(newDescriptionTextField.getText());

        this.gridPane.addRow(9,newDescription);
    }

    @FXML
    private void switchToUpcoming() throws IOException {
        App.setRoot("upcoming");
    }
    @FXML
    public void switchToTest(ActionEvent actionEvent) throws IOException {
        App.setRoot("test");
    }

    @FXML
    private void submitTask(){
        RadioButton r =(RadioButton) priority.getSelectedToggle();
        Category category1 = null;
        for (Category category: categories){
            if(category.getName().equals(choiceBox.getValue().toString())){
                category1=category;
            }
        }
        assert category1 != null;
        register.addMainTask(startDate.getValue(),endDate.getValue(),taskName.getText(),description.getText(),Integer.parseInt(r.getText()),category1.getID());
        updateTasks();
    }

    private void edit(Label l, String t){
        l.addEventHandler(MouseEvent.MOUSE_ENTERED,
                event -> l.setText("Edit"));
        l.addEventHandler(MouseEvent.MOUSE_EXITED,
                event -> l.setText(t));
    }

    private void updateTasks(){
        showTasks.getChildren().clear();
        showTasks.setPrefWidth(969);
        for (MainTask t: register.getAllTasks() ){

            if(!t.isDone()){
                HBox hBox = new HBox();
                hBox.setId("taskBox");

                AnchorPane anchorPane = new AnchorPane();
                ImageView imageView = new ImageView();

                CheckBox checkBox = new CheckBox();
                checkBox.setId(String.valueOf(t.getID()));
                checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                    t.setDone(true);
                    updateTasks();
                });

                Label taskName=new Label(t.getName());
                edit(taskName, t.getName());

                Label startDate=new Label(String.valueOf(t.getStartDate()));
                edit(startDate, String.valueOf(t.getStartDate()));

                Label endDate=new Label(String.valueOf(t.getEndDate()));
                edit(endDate, String.valueOf(t.getEndDate()));

                Label description = new Label(t.getDescription());
                edit(description, t.getDescription());

                Label category = new Label(register.getCategory(t.getCategoryId()).getName());
                edit(category, register.getCategory(t.getCategoryId()).getName());

                Label priority = new Label(String.valueOf(t.getPriority()));
                edit(priority, String.valueOf(t.getPriority()));

                Separator separator = new Separator();

                hBox.getChildren().add(checkBox);
                hBox.getChildren().add(taskName);
                hBox.getChildren().add(description);
                hBox.getChildren().add(startDate);
                hBox.getChildren().add(endDate);
                hBox.getChildren().add(priority);
                hBox.getChildren().add(category);

                separator.setOrientation(Orientation.HORIZONTAL);
                separator.setPrefWidth(900);
                separator.setLayoutX(95);
                separator.setLayoutY(108);
                anchorPane.getChildren().add(separator);
                anchorPane.setPrefHeight(3);
                AnchorPane.setLeftAnchor(separator, 80.0);
                AnchorPane.setBottomAnchor(separator, 0.0);
                AnchorPane.setTopAnchor(separator, 0.0);


                checkBox.setPrefWidth(80);
                checkBox.setPrefHeight(54);
                checkBox.setPadding(new Insets(0, 0, 0, 30));
                checkBox.setAlignment(Pos.CENTER);

                imageView.setFitHeight(29);
                imageView.setFitWidth(30);

                showTasks.getChildren().add(hBox);
                showTasks.getChildren().add(anchorPane);
            }
        }
    }
}
