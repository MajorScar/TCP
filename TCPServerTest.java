import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TCPServerTest {
    @Test
    void testServer() throws IOException {
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                TCPServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Wait for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Connect to the server
        Socket socket = new Socket("localhost", 6789);

        // Send a message to the server
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        outToServer.writeBytes("Hello, server!\n");

        // Receive the reply from the server
        String reply = inFromServer.readLine();
        assertEquals("Total messages received: 1", reply);

        // Send another message to the server
        outToServer.writeBytes("How are you doing?\n");

        // Receive the reply from the server
        reply = inFromServer.readLine();
        assertEquals("Total messages received: 2", reply);

        // Send "exit" to the server to terminate the connection
        outToServer.writeBytes("exit\n");

        // Close the socket
        socket.close();

        // Stop the server thread
        serverThread.interrupt();
    }
}
