package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;

public class ChatController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    @FXML
    private Button fileButton;
    @FXML
    private ProgressBar progressBar;

    private PrintWriter out;
    private BufferedReader in;

    public void initialize() {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Thread readerThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        Platform.runLater(() -> chatArea.appendText(serverMessage + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        sendButton.setOnAction(event -> sendMessage());
        fileButton.setOnAction(event -> chooseFile());
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            inputField.clear();
        }
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            sendFile(file);
        }
    }

    private void sendFile(File file) {
        long fileSize = file.length();
        long totalRead = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
                totalRead += line.length();

                // Update progress bar
                long finalTotalRead = totalRead;
                Platform.runLater(() -> progressBar.setProgress((double) finalTotalRead / fileSize));
            }
            out.println("END_OF_FILE");
            Platform.runLater(() -> progressBar.setProgress(1.0)); // Ensure progress is complete
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
