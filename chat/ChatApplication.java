import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class ChatApplication extends Application {
    private List<UserWindow> userWindows;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Application");

        userWindows = new ArrayList<>();

        UserWindow userWindow1 = new UserWindow("User 1", this);
        UserWindow userWindow2 = new UserWindow("User 2", this);
        UserWindow userWindow3 = new UserWindow("User 3", this);
        UserWindow userWindow4 = new UserWindow("User 4", this);

        userWindows.add(userWindow1);
        userWindows.add(userWindow2);
        userWindows.add(userWindow3);
        userWindows.add(userWindow4);

        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));
        root.add(userWindow1.getRoot(), 0, 0);
        root.add(userWindow2.getRoot(), 1, 0);
        root.add(userWindow3.getRoot(), 0, 1);
        root.add(userWindow4.getRoot(), 1, 1);

        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    public void sendMessageToAll(String message) {
        for (UserWindow userWindow : userWindows) {
            userWindow.appendMessage(message);
        }
    }

    public GridPane getUIRoot() {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(10));
        root.add(userWindows.get(0).getRoot(), 0, 0);
        root.add(userWindows.get(1).getRoot(), 1, 0);
        root.add(userWindows.get(2).getRoot(), 0, 1);
        root.add(userWindows.get(3).getRoot(), 1, 1);
        return root;
    }
}