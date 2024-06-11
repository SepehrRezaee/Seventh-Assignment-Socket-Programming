package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Chat Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
