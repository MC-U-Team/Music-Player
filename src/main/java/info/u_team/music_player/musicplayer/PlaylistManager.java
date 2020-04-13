package info.u_team.music_player.musicplayer;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

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
		path = MusicPlayerFiles.getDirectory().resolve("playlist.json.gz");
	}
	
	@Override
	public void loadFromFile() {
		try {
			if (!Files.exists(path)) {
				playlists = new Playlists();
				writeToFile();
			} else {
				try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(path)), "UTF-8"))) {
					playlists = gson.fromJson(reader, Playlists.class);
				} catch (IOException ex) {
					throw ex;
				}
			}
		} catch (ZipException ex) {
			logger.warn("The playlist file is corrupted and will be deleted at " + path, ex);
			playlists = new Playlists();
		} catch (IOException ex) {
			logger.error("Could not read playlist file at " + path, ex);
		}
	}
	
	@Override
	public void writeToFile() {
		try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(Files.newOutputStream(path)), "UTF-8"))) {
			gson.toJson(playlists, writer);
		} catch (IOException ex) {
			logger.error("Could not write playlist file at " + path, ex);
		}
	}
	
	public Playlists getPlaylists() {
		return playlists;
	}
}
