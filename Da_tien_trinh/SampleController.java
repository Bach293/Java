import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

public class SampleController {
    @FXML
    private ListView<String> processListView;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    private ExecutorService executorService;

    public void init() {
        executorService = Executors.newFixedThreadPool(2);

        startButton.setOnAction(event -> startProcess());
        stopButton.setOnAction(event -> stopProcess());
    }

    private void startProcess() {
        processListView.getItems().add("Multithreaded process started");

        // Thực thi tiến trình đa tiến trình
        executorService.submit(() -> {
            try {
                // Mã logic của đa tiến trình 
                for (int i = 0; i < 10; i++) {
                    final int count = i;
                    Random random = new Random();
                    int randomNumber = random.nextInt(2001) + 1000;
                    float time = randomNumber/1000f;
                    String timeRun = String.format("%.4f", time);
                    Platform.runLater(() -> processListView.getItems().add("Processing " + count +" - "+ timeRun+"s"));
                    
                    Thread.sleep(randomNumber);
                }

                Platform.runLater(() -> processListView.getItems().add("Process completed"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void stopProcess() {
        // Dừng tiến trình đa tiến trình
        executorService.shutdownNow();
        processListView.getItems().clear();
    }
}