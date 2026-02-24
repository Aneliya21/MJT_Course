package Labs.lab_10.src.music.server.repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Labs.lab_10.src.music.server.model.Playlist;
import Labs.lab_10.src.music.server.model.Song;
import Labs.lab_10.src.music.server.repository.exception.PlaylistAlreadyExistsException;
import Labs.lab_10.src.music.server.repository.exception.PlaylistNotFoundException;
import Labs.lab_10.src.music.server.repository.exception.SongAlreadyExistsException;
import Labs.lab_10.src.music.server.repository.exception.SongNotFoundException;

public class InMemoryPlaylistRepository implements PlaylistRepository {

    private final Map<String, Playlist> playlists = new ConcurrentHashMap<>();

    public InMemoryPlaylistRepository() {

    }

    @Override
    public void createPlaylist(String playlistName) throws PlaylistAlreadyExistsException {
        if (playlists.containsKey(playlistName)) {
            throw new PlaylistAlreadyExistsException("Playlist " + playlistName + " already exists.");
        }
        playlists.put(playlistName, new Playlist(playlistName, new ConcurrentHashMap<>()));
    }

    @Override
    public Song addSong(String playlistName, String songTitle, String artistName, int duration)
        throws PlaylistNotFoundException, SongAlreadyExistsException {
        Playlist playlist = getPlaylist(playlistName);
        Song song = new Song(songTitle, artistName, duration);
        playlist.addSong(song);
        return song;
    }

    @Override
    public int likeSong(String playlistName, String songTitle, String artistName)
        throws PlaylistNotFoundException, SongNotFoundException {
        Playlist playlist = getPlaylist(playlistName);
        return playlist.likeSong(songTitle, artistName);
    }

    @Override
    public int unlikeSong(String playlistName, String songTitle, String artistName)
        throws PlaylistNotFoundException, SongNotFoundException {
        Playlist playlist = getPlaylist(playlistName);
        return playlist.unlikeSong(songTitle, artistName);
    }

    @Override
    public Collection<String> getAllPlaylists() {
        return playlists.keySet();
    }

    @Override
    public Playlist getPlaylist(String playlistName) throws PlaylistNotFoundException {
        Playlist playlist = playlists.get(playlistName);
        if (playlist == null) {
            throw new PlaylistNotFoundException("Playlist " + playlistName + " does not exist.");
        }
        return playlist;
    }

    private static void validateStringValue(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
}
