package utils;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.io.*;

public class FileTransferProgress {
    public static void sendFileWithProgress(File file, PrintWriter out, ProgressBar progressBar) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            long fileSize = file.length();
            long totalRead = 0;

            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
                totalRead += line.length();

                long finalTotalRead = totalRead;
                Platform.runLater(() -> progressBar.setProgress((double) finalTotalRead / fileSize));
            }
            out.println("END_OF_FILE");
            Platform.runLater(() -> progressBar.setProgress(1.0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
