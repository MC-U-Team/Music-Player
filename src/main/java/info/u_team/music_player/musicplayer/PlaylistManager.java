package info.u_team.music_player.musicplayer;

import java.io.*;
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
				playlists = new Playlists();
				writeToFile();
			} else {
				try (BufferedReader reader = Files.newBufferedReader(path)) {
					playlists = gson.fromJson(reader, Playlists.class);
				} catch (IOException ex) {
					throw ex;
				}
			}
		} catch (IOException ex) {
			logger.error("Could not ready playlist file at " + path, ex);
		}
	}
	
	public void writeToFile() {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			gson.toJson(playlists, writer);
		} catch (IOException ex) {
			logger.error("Could not write playlist file at " + path, ex);
		}
	}
	
	public Playlists getPlaylists() {
		return playlists;
	}
}
