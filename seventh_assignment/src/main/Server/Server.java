// Server Class
public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private Map<String, List<String>> chatHistory;
    private Map<String, File> availableFiles;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            chatHistory = new HashMap<>();
            availableFiles = new HashMap<>();
            
            // Load available files from the specified directory
            loadAvailableFiles("seventh_assignment/src/main/Server/data");
            
            System.out.println("Server started on port " + port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeServer();
        }
    }
    
    private void loadAvailableFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    availableFiles.put(file.getName(), file);
                }
            }
        }
    }
    
    private void closeServer() {
        try {
            for (ClientHandler client : clients) {
                client.closeConnection();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;
        private String username;

        public ClientHandler(Socket socket) {
            try {
                clientSocket = socket;
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new DataOutputStream(clientSocket.getOutputStream());
                username = inputStream.readUTF();
                sendMessage("Welcome, " + username + "!");
                sendMessage("Available commands:");
                sendMessage("1. /files - View available files");
                sendMessage("2. /download <filename> - Download a file");
                sendMessage("3. /history <count> - View chat history (optional count)");
                broadcastMessage(username + " joined the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            try {
                String message;
                while ((message = inputStream.readUTF()) != null) {
                    if (message.startsWith("/")) {
                        handleCommand(message);
                    } else {
                        broadcastMessage(username + ": " + message);
                        addToChatHistory(username, message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                removeClient();
            }
        }
        
        private void handleCommand(String command) {
            String[] parts = command.split(" ");
            String commandName = parts[0];
            
            switch (commandName) {
                case "/files":
                    sendAvailableFiles();
                    break;
                case "/download":
                    if (parts.length == 2) {
                        String filename = parts[1];
                        sendFile(filename);
                    } else {
                        sendMessage("Invalid command. Usage: /download <filename>");
                    }
                    break;
                case "/history":
                    int count = 10; // Default count
                    if (parts.length == 2) {
                        try {
                            count = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e) {
                            sendMessage("Invalid count. Using default count of 10.");
                        }
                    }
                    sendChatHistory(count);
                    break;
                default:
                    sendMessage("Unknown command: " + commandName);
            }
        }
        
        private void sendAvailableFiles() {
            StringBuilder response = new StringBuilder("Available files:\n");
            for (String filename : availableFiles.keySet()) {
                response.append(filename).append("\n");
            }
            sendMessage(response.toString());
        }
        
        private void sendFile(String filename) {
            File file = availableFiles.get(filename);
            if (file != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    fileInputStream.close();
                    sendMessage("File downloaded successfully.");
                } catch (IOException e) {
                    sendMessage("Error occurred while downloading the file.");
                }
            } else {
                sendMessage("File not found: " + filename);
            }
        }
        
        private void sendChatHistory(int count) {
            List<String> history = chatHistory.get(username);
            if (history != null) {
                int startIndex = Math.max(0, history.size() - count);
                StringBuilder response = new StringBuilder("Chat history:\n");
                for (int i = startIndex; i < history.size(); i++) {
                    response.append(history.get(i)).append("\n");
                }
                sendMessage(response.toString());
            } else {
                sendMessage("No chat history found.");
            }
        }
        
        private void addToChatHistory(String username, String message) {
            List<String> history = chatHistory.getOrDefault(username, new ArrayList<>());
            history.add(message);
            chatHistory.put(username, history);
        }
        
        private void sendMessage(String message) {
            try {
                outputStream.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
        
        private void removeClient() {
            clients.remove(this);
            broadcastMessage(username + " left the chat.");
            closeConnection();
        }
        
        private void closeConnection() {
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        int port = 8888;
        new Server(port);
    }
}
