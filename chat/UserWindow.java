import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserWindow {
    private BorderPane root;
    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;
    private ChatApplication chatApplication;

    
    
    public UserWindow(String username, ChatApplication chatApp) {
        chatApplication = chatApp;

        root = new BorderPane();
        messageArea = new TextArea();
        inputField = new TextField();
        sendButton = new Button("Send");

        root.setPrefHeight(330);
        root.setPrefWidth(500);

        messageArea.setEditable(false); // Tắt chế độ chỉnh sửa
        messageArea.setPrefHeight(220);
        messageArea.setPrefWidth(450);
        messageArea.getStyleClass().add("message-area");

        inputField.setPrefWidth(400);

        sendButton.getStyleClass().add("send-button");
        sendButton.setOnAction(event -> sendMessage(username));

        HBox inputBox = new HBox(inputField, sendButton);
        inputBox.setSpacing(10);
        inputBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(messageArea, inputBox);
        contentBox.setSpacing(10);
        contentBox.setPadding(new Insets(10));

        root.setCenter(new ScrollPane(contentBox));

        root.setTop(new HBox());
        BorderPane.setAlignment(root.getTop(), Pos.CENTER);
        BorderPane.setMargin(root.getTop(), new Insets(10));
        root.getTop().setStyle("-fx-font-size: 18px;");
        ((HBox) root.getTop()).getChildren().add(new javafx.scene.control.Label(username));

        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage(username);
            }
        });

        root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    public BorderPane getRoot() {
        return root;
    }

    public void appendMessage(String message) {
        messageArea.appendText(message);
    }

    private void sendMessage(String username) {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            LocalDateTime currentTime = LocalDateTime.now();
            String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String fullMessage = username + " (" + formattedTime + "):\n" + message + "\n\n";

            chatApplication.sendMessageToAll(fullMessage);

            inputField.clear();
        }
    }
}