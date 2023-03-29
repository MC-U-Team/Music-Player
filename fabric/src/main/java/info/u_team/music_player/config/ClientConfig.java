package info.u_team.music_player.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import info.u_team.music_player.MusicPlayerMod;
import net.fabricmc.loader.api.FabricLoader;

public class ClientConfig {
	
	private static transient ClientConfig INSTANCE = new ClientConfig();
	
	public static ClientConfig getInstance() {
		return INSTANCE;
	}
	
	public boolean internalPlaylists = false;
	
	public static void load() {
		final Path path = FabricLoader.getInstance().getConfigDir().resolve("musicplayer.json");
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		ClientConfig config = null;
		
		try {
			if (!Files.exists(path)) {
				config = new ClientConfig();
				try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
					gson.toJson(config, writer);
				}
			} else {
				try (final BufferedReader reader = Files.newBufferedReader(path)) {
					config = gson.fromJson(reader, ClientConfig.class);
				} catch (final IOException ex) {
					throw ex;
				}
			}
		} catch (final IOException ex) {
			MusicPlayerMod.LOGGER.error("Could not read musicplayer config file at " + path, ex);
		}
		
		if (config == null) {
			config = new ClientConfig();
			try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
				gson.toJson(config, writer);
			} catch (final IOException ex) {
			}
		}
		
		synchronized (INSTANCE) {
			INSTANCE = config;
		}
	}
}
