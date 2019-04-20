package info.u_team.music_player.musicplayer;

import java.io.IOException;
import java.nio.file.*;

import org.apache.logging.log4j.*;

import com.google.gson.*;

import info.u_team.music_player.init.MusicPlayerFiles;

public class PlaylistManager {
	
	private static final Logger logger = LogManager.getLogger();
	
	private final Gson gson;
	
	private final Path path;
	
	private Playlists playlists;
	
	public PlaylistManager() {
		gson = new GsonBuilder().setPrettyPrinting().create();
		path = MusicPlayerFiles.playlist.resolve("playlist.json");
	}
	
	public void loadFromFile() {
		try {
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
			playlists = gson.fromJson(Files.newBufferedReader(path), Playlists.class);
			if (playlists == null) {
				playlists = new Playlists();
			}
		} catch (IOException ex) {
			logger.error("Could not ready playlist file at " + path, ex);
		}
	}
	
	public void writeToFile() {
		try {
			gson.toJson(playlists, Files.newBufferedWriter(path));
		} catch (IOException ex) {
			logger.error("Could not write playlist file at " + path, ex);
		}
	}
	
	public Playlists getPlaylists() {
		return playlists;
	}
}
