import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final Set<PrintWriter> clientWriters = new HashSet<>();
    private static final List<String> chatHistory = new ArrayList<>();
    private static final int CHAT_HISTORY_LIMIT = 10;
    private static final String FILES_DIR = "seventh_assignment/src/main/Server/data";

    public static void main(String[] args) {
        System.out.println("The chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(out);
                    sendChatHistory(out);
                }

                String clientRequest;
                while ((clientRequest = in.readLine()) != null) {
                    if (clientRequest.startsWith("GET_FILE_LIST")) {
                        sendFileList(out);
                    } else if (clientRequest.startsWith("DOWNLOAD_FILE")) {
                        String fileName = clientRequest.substring("DOWNLOAD_FILE ".length());
                        sendFile(out, fileName);
                    } else {
                        synchronized (clientWriters) {
                            chatHistory.add(clientRequest);
                            if (chatHistory.size() > CHAT_HISTORY_LIMIT) {
                                chatHistory.remove(0);
                            }
                            for (PrintWriter writer : clientWriters) {
                                writer.println(clientRequest);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }

        private void sendChatHistory(PrintWriter out) {
            for (String msg : chatHistory) {
                out.println(msg);
            }
        }

        private void sendFileList(PrintWriter out) {
            File folder = new File(FILES_DIR);
            File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    out.println(file.getName());
                }
            }
            out.println("END_OF_LIST");
        }

        private void sendFile(PrintWriter out, String fileName) {
            File file = new File(FILES_DIR, fileName);
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    out.println(line);
                }
            } catch (IOException e) {
                out.println("ERROR: File not found");
            }
            out.println("END_OF_FILE");
        }
    }
}
