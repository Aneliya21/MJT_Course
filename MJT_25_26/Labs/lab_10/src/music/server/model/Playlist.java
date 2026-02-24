package Labs.lab_10.src.music.server.model;

import java.util.Map;

import Labs.lab_10.src.music.server.repository.exception.SongAlreadyExistsException;
import Labs.lab_10.src.music.server.repository.exception.SongNotFoundException;

public class Playlist {

    private String name;
    private Map<Song, Integer> songs;

    public Playlist(String name, Map<Song, Integer> songs) {
        this.name = name;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public Map<Song, Integer> getSongs() {
        return songs;
    }

    public void addSong(Song song) throws SongAlreadyExistsException {
        if (songs.containsKey(song)) {
            throw new SongAlreadyExistsException("Song already exists");
        }
        songs.put(song, 0);
    }

    public int likeSong(String title, String artist) throws SongNotFoundException {
        Song key = new Song(title, artist, 1);
        if (!songs.containsKey(key)) throw new SongNotFoundException("Song not found");

        songs.computeIfPresent(key, (k, v) -> v + 1);
        return songs.get(key);
    }

    public int unlikeSong(String title, String artist) throws SongNotFoundException {
        Song key = new Song(title, artist, 1);
        if (!songs.containsKey(key)) throw new SongNotFoundException("Song not found");

        songs.computeIfPresent(key, (k, v) -> Math.max(0, v - 1));
        return songs.get(key);
    }
}
