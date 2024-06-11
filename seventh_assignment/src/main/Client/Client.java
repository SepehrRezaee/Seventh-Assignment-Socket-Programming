import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread to read messages from the server
            Thread readerThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

            // Main thread to send messages to the server
            String clientMessage;
            while ((clientMessage = consoleInput.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("LIST_FILES")) {
                    out.println("GET_FILE_LIST");
                    String serverMessage;
                    while (!(serverMessage = in.readLine()).equals("END_OF_LIST")) {
                        System.out.println(serverMessage);
                    }
                } else if (clientMessage.startsWith("DOWNLOAD_FILE")) {
                    out.println(clientMessage);
                    String fileName = clientMessage.substring("DOWNLOAD_FILE ".length());
                    saveFile(in, fileName);
                } else {
                    out.println(clientMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveFile(BufferedReader in, String fileName) {
        try (PrintWriter fileOut = new PrintWriter(new FileWriter("downloaded_" + fileName))) {
            String serverMessage;
            while (!(serverMessage = in.readLine()).equals("END_OF_FILE")) {
                fileOut.println(serverMessage);
            }
            System.out.println("File downloaded successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
