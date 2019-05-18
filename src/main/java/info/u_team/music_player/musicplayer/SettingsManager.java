package info.u_team.music_player.musicplayer;

import java.io.*;
import java.nio.file.*;

import org.apache.logging.log4j.*;

import com.google.gson.Gson;

import info.u_team.music_player.init.MusicPlayerFiles;
import info.u_team.music_player.musicplayer.settings.Settings;

public class SettingsManager implements IGsonLoadable {
	
	private final Logger logger = LogManager.getLogger();
	
	private final Gson gson;
	
	private final Path path;
	
	private Settings settings;
	
	public SettingsManager(Gson gson) {
		this.gson = gson;
		path = MusicPlayerFiles.getDirectory().resolve("settings.json");
	}
	
	public void loadFromFile() {
		try {
			if (!Files.exists(path)) {
				settings = new Settings();
				writeToFile();
			} else {
				try (BufferedReader reader = Files.newBufferedReader(path)) {
					settings = gson.fromJson(reader, Settings.class);
					if (settings == null) {
						settings = new Settings();
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
	
	public void writeToFile() {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			gson.toJson(settings, writer);
		} catch (IOException ex) {
			logger.error("Could not write playlist file at " + path, ex);
		}
	}
	
	public Settings getSettings() {
		return settings;
	}
	
}
