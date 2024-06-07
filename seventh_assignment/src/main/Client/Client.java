// Client Class
public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private BufferedReader reader;
    private String username;

    public Client(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(System.in));
            
            // Prompt for username
            System.out.print("Enter your username: ");
            username = reader.readLine();
            
            // Send username to server
            outputStream.writeUTF(username);
            
            // Start receiving messages from server
            new Thread(new ServerHandler()).start();
            
            // Start sending messages to server
            String message;
            while ((message = reader.readLine()) != null) {
                outputStream.writeUTF(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    
    private void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class ServerHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = inputStream.readUTF()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 8888;
        new Client(serverAddress, serverPort);
    }
}
