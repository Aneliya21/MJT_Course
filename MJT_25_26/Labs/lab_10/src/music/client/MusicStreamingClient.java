package Labs.lab_10.src.music.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MusicStreamingClient {
    private static final String SERVER_HOST = "localhost";
    private final int serverPort;

    public MusicStreamingClient(int serverPort) {
        this.serverPort = serverPort;
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_HOST, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to the Music Streaming Server.");
            System.out.println("Enter commands (type 'disconnect' to exit):");
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input == null || input.isBlank()) {
                    continue;
                }
                out.println(input);

                if ("disconnect".equalsIgnoreCase(input.trim())) {
                    System.out.println("Disconnected from server.");
                    break;
                }
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }
                System.out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }
}
