import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private Stage primaryStage;
    private BorderPane root;
    private StackPane currentContent;
    private ChatApplication chatApplication;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Tạo menu item cho ChatApplication
        MenuItem chatMenuItem = new MenuItem("Chat Application");

        // Xử lý sự kiện khi người dùng chọn ChatApplication
        chatMenuItem.setOnAction(event -> {
            // Hiển thị trang ChatApplication
            showChatApplication();
        });

        // Tạo menu gồm một menu item
        Menu problemMenu = new Menu("Bài toán");
        problemMenu.getItems().add(chatMenuItem);

        // Tạo thanh menu bar và thêm menu vào đó
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(problemMenu);

        // Tạo layout chính
        root = new BorderPane();
        root.setTop(menuBar);

        // Tạo khu vực hiển thị nội dung
        currentContent = new StackPane();
        root.setCenter(currentContent);

        // Tạo scene và hiển thị ứng dụng
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Ứng dụng chính");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showChatApplication() {
        chatApplication = new ChatApplication();
        Scene scene = new Scene(chatApplication.getUIRoot(), 1000, 700);
        primaryStage.setTitle("Chat Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}