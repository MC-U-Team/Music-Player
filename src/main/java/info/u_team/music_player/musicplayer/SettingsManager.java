package info.u_team.music_player.musicplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import info.u_team.music_player.init.MusicPlayerFiles;
import info.u_team.music_player.musicplayer.settings.Settings;

public class SettingsManager implements IGsonLoadable {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Gson gson;
	
	private final Path path;
	
	private Settings settings;
	
	public SettingsManager(Gson gson) {
		this.gson = gson;
		path = MusicPlayerFiles.getDirectory().resolve("settings.json");
	}
	
	@Override
	public void loadFromFile() {
		try {
			if (!Files.exists(path)) {
				settings = new Settings();
				writeToFile();
			} else {
				try (final BufferedReader reader = Files.newBufferedReader(path)) {
					settings = gson.fromJson(reader, Settings.class);
					if (settings == null) {
						settings = new Settings();
						writeToFile();
					}
				} catch (final IOException ex) {
					throw ex;
				}
			}
		} catch (final IOException ex) {
			LOGGER.error("Could not ready playlist file at " + path, ex);
		}
	}
	
	@Override
	public void writeToFile() {
		try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
			gson.toJson(settings, writer);
		} catch (final IOException ex) {
			LOGGER.error("Could not write playlist file at " + path, ex);
		}
	}
	
	public Settings getSettings() {
		return settings;
	}
	
}
