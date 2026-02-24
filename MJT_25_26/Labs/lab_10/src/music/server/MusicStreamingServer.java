package Labs.lab_10.src.music.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Labs.lab_10.src.music.server.repository.PlaylistRepository;

public class MusicStreamingServer {

    private final int port;
    private final PlaylistRepository playlistRepository;
    private final ExecutorService executor;
    private boolean isRunning;

    public MusicStreamingServer(int port, PlaylistRepository playlistRepository) {
        this.port = port;
        this.playlistRepository = playlistRepository;
        this.executor = Executors.newCachedThreadPool();
    }

    public void start() {
        isRunning = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket, playlistRepository));
            }
        } catch (IOException e) {
            if (isRunning) System.err.println("Server error: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
        executor.shutdownNow();
    }
}
