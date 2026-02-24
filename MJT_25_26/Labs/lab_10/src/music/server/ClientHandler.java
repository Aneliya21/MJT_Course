package Labs.lab_10.src.music.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Labs.lab_10.src.music.server.repository.PlaylistRepository;

public class ClientHandler implements Runnable {

    private static final int PLAYLIST_NAME_INDEX = 1;
    private static final int SONG_TITLE_INDEX = 2;
    private static final int ARTIST_NAME_INDEX = 3;
    private static final int DURATION_INDEX = 4;
    private static final int MIN_ADD_SONG_TOKENS = 5;

    private final Socket clientSocket;
    private final PlaylistRepository repository;

    public ClientHandler(Socket socket, PlaylistRepository repository) {
        this.clientSocket = socket;
        this.repository = repository;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("disconnect".equalsIgnoreCase(inputLine.trim())) {
                    break;
                }

                String response = executeCommand(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private String executeCommand(String input) {
        String[] tokens = input.split("\\s+");
        String command = tokens[0];

        return switch (command) {
            case "create-playlist" -> handleCreatePlaylist(tokens);
            case "add-song" -> handleAddSong(tokens);
            default -> "{\"status\":\"ERROR\",\"message\":\"Unknown command\"}";
        };
    }

    private String handleCreatePlaylist(String[] tokens) {
        if (tokens.length < 2) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: create-playlist <name>\"}";
        }
        try {
            repository.createPlaylist(tokens[1]);
            return String.format("{\"status\":\"OK\",\"message\":\"Playlist %s created successfully.\"}", tokens[1]);
        } catch (Exception e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"%s\"}", e.getMessage());
        }
    }

    private void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    private String handleAddSong(String[] tokens) {
        if (tokens.length < MIN_ADD_SONG_TOKENS) {
            return "{\"status\":\"ERROR\",\"message\":\"" +
                "Usage: add-song <playlist_name> <song_title> <artist_name> <duration>\"}";
        }

        String playlistName = tokens[PLAYLIST_NAME_INDEX];
        String songTitle = tokens[SONG_TITLE_INDEX];
        String artistName = tokens[ARTIST_NAME_INDEX];

        try {
            int duration = Integer.parseInt(tokens[DURATION_INDEX]);

            if (duration <= 0) {
                return "{\"status\":\"ERROR\",\"message\":\"Duration must be a positive integer\"}";
            }

            repository.addSong(playlistName, songTitle, artistName, duration);

            return String.format(
                "{\"status\":\"OK\",\"message\":\"Song %s by %s added successfully.\"}",
                songTitle, artistName
            );

        } catch (NumberFormatException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Duration must be a positive integer\"}";
        } catch (Exception e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"%s\"}", e.getMessage());
        }
    }
}