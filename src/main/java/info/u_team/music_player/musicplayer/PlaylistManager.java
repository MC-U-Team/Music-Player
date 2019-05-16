package info.u_team.music_player.musicplayer;

import java.io.*;
import java.nio.file.*;

import org.apache.logging.log4j.*;

import com.google.gson.Gson;

import info.u_team.music_player.init.MusicPlayerFiles;
import info.u_team.music_player.musicplayer.playlist.Playlists;

public class PlaylistManager implements IGsonLoadable {

	private final Logger logger = LogManager.getLogger();

	private final Gson gson;

	private final Path path;

	private Playlists playlists;

	public PlaylistManager(Gson gson) {
		this.gson = gson;
		path = MusicPlayerFiles.playlist.resolve("playlist.json");
	}

	@Override
	public void loadFromFile() {
		try {
			if (!Files.exists(path)) {
				playlists = new Playlists();
				writeToFile();
			} else {
				try (BufferedReader reader = Files.newBufferedReader(path)) {
					playlists = gson.fromJson(reader, Playlists.class);
					if (playlists == null) {
						playlists = new Playlists();
						writeToFile();
					}
				} catch (IOException ex) {
					throw ex;
				}
			}
		} catch (IOException ex) {
			logger.error("Could not ready playlist file at " + path, ex);
		}
	}

	@Override
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
