package info.u_team.music_player.musicplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import info.u_team.music_player.init.MusicPlayerFiles;
import info.u_team.music_player.musicplayer.playlist.Playlists;

public class PlaylistManager implements IGsonLoadable {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
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
				} catch (final IOException ex) {
					throw ex;
				}
			}
		} catch (final ZipException ex) {
			LOGGER.warn("The playlist file is corrupted and will be deleted at " + path, ex);
			playlists = new Playlists();
		} catch (final IOException ex) {
			LOGGER.error("Could not read playlist file at " + path, ex);
		}
	}
	
	@Override
	public void writeToFile() {
		try (final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(Files.newOutputStream(path)), "UTF-8"))) {
			gson.toJson(playlists, writer);
		} catch (final IOException ex) {
			LOGGER.error("Could not write playlist file at " + path, ex);
		}
	}
	
	public Playlists getPlaylists() {
		return playlists;
	}
}
