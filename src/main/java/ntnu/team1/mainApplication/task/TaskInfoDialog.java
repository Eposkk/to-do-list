package ntnu.team1.mainApplication.task;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ntnu.team1.application.MainRegister;
import ntnu.team1.application.task.MainTask;
import ntnu.team1.mainApplication.App;
import org.w3c.dom.Text;

import java.util.Objects;

/**
 * Dialog used for showing info of the task
 */

public class TaskInfoDialog extends Dialog<MainRegister> {

    /**
     * Task used for showing info in the dialog
     */
    private MainTask task;

    /**
     * Constructor for the class
     * @param task Task you want to show
     */

    public TaskInfoDialog(MainTask task) {
        super();
        this.task = task;
        // Create the content of the dialog
        showTask();
    }

    private void showTask() {
        setTitle(task.getName() + " - info");
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("dialog.css")).toExternalForm());


        GridPane mainGrid = new GridPane();

        Label name = new Label(task.getName());
        mainGrid.add(name, 1,1);
        Label description = new Label(task.getDescription());
        mainGrid.add(description, 1,2);
        Label priority = new Label("Priority: " + task.getPriority());
        mainGrid.add(priority,2,1);
        Label category = new Label(App.getRegister().getCategory(task.getCategoryId()).getName());
        mainGrid.add(category, 2,2);
       // Label startDate = new Label(task.getStartDate().toString());
        //Label endDate = new Label(task.getEndDate().toString());


        setGraphic(mainGrid);

    }

}
