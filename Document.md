# Chat Application

## Overview

This project is a simple chat application with file transfer capabilities, enhanced with a graphical user interface (
GUI) using JavaFX. The server supports multiple clients, maintains a chat history, and allows clients to send and
receive text files. The application also includes progress tracking for file transfers.

## Features

- **Group Chat Functionality**: Multiple clients can connect to the server and participate in a group chat.
- **Chat History**: New clients receive the last few messages that were sent before they joined.
- **File Transfer**: Clients can send text files to the server, which are then broadcasted to all other clients.
- **Graphical User Interface (GUI)**: The client application uses JavaFX to provide a user-friendly interface.
- **Progress Tracking**: Clients can see the progress of file transfers using a progress bar.

## Project Structure

### Server

The server-side component is responsible for:

- Accepting client connections.
- Broadcasting messages from one client to all other connected clients.
- Managing a chat history buffer to provide recent messages to new clients.
- Handling file transfer requests and broadcasting file contents to all clients.

### Client

The client-side component includes:

- A JavaFX-based GUI for sending and receiving messages.
- Functionality to choose and send text files to the server.
- Display of chat messages in real-time.
- Progress tracking for file transfers using a progress bar.

### Utilities

- Utility classes to support various functionalities like file transfer with progress tracking.

## Getting Started

- ChatApp
    - src
        - server
            - Server.java
        - client
            - Client.java
            - ChatController.java
            - ChatView.fxml
            - ChatApp.java
        - utils
            - FileTransferProgress.java
    - README.md

### Prerequisites

- **Java Development Kit (JDK) 8 or later**: The project is built using Java 8, but any later version should work.
- **JavaFX**: Ensure JavaFX is installed and configured with your JDK.

### Building the Project

1. **Clone the repository**:
    ```sh
    git clone https://github.com/SepehrRezaee/Seventh-Assignment-Socket-Programming.git
    cd ChatApp
    ```
2. **Open the project in your IDE**: Import the project as a Maven or Gradle project to manage dependencies and build
   configurations easily.
3. **Ensure JavaFX is set up**:
    - For IntelliJ IDEA, configure JavaFX in the project settings.
    - For other IDEs, refer to their respective documentation for configuring JavaFX.

### Running the Server

1. Navigate to the `src/server` directory.
2. Run the `Server.java` file.
3. The server will start and listen for client connections on port `12345`.

### Running the Client

1. Navigate to the `src/client` directory.
2. Run the `ChatApp.java` file.
3. A GUI window will appear, allowing you to join the chat.

### Usage

1. **Start the server**: Ensure the server is running before starting any clients.
2. **Start the client**: Run the `ChatApp.java` file to open the client GUI.
3. **Send messages**: Type a message in the input field and click the "Send" button or press Enter to send the message.
4. **Send files**: Click the "Send File" button to open a file chooser dialog, select a text file, and the file will be
   sent to the server. The progress bar will display the transfer progress.
5. **View chat history**: New clients will automatically receive the last few messages sent in the chat.

## File Transfer Progress

- The progress bar in the client GUI shows the progress of file transfers.
- As a file is being sent, the progress bar updates to reflect the portion of the file that has been sent.
- The progress bar ensures users are informed about the status of their file transfers.

## Error Handling

- The application handles various I/O and network-related exceptions.
- Errors during file transfers are caught and appropriate messages are displayed in the console.

## Troubleshooting

- **Server Not Starting**: Ensure no other application is using the port `12345`.
- **Client Cannot Connect**: Verify the server is running and the correct IP address and port are being used.
- **JavaFX Issues**: Ensure JavaFX is correctly installed and configured with your JDK.

## Contributing

1. **Fork the repository**.
2. **Create a new branch**:
    ```sh
    git checkout -b feature/YourFeatureName
    ```
3. **Commit your changes**:
    ```sh
    git commit -m 'Add your feature'
    ```
4. **Push to the branch**:
    ```sh
    git push origin feature/YourFeatureName
    ```
5. **Open a pull request**.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Acknowledgements

- JavaFX Documentation: [JavaFX Docs](https://openjfx.io/).
- Open source libraries and frameworks that contributed to this project.
