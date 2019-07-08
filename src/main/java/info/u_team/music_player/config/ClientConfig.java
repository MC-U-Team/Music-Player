package info.u_team.music_player.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ClientConfig {
	
	private static Configuration configuration;
	
	public static void setupConfig(File file) {
		configuration = new Configuration(new File(file, "musicplayer-client" + ".cfg"));
		configuration.load();
		
		configuration.getCategory("client").setComment("Client configuration settings");
		client.internalPlaylists = configuration.getBoolean("internalPlaylists", "client", false, "Should we use instead of the global appdata dir a local dir in configs for storing and loading our playlist");
		
		configuration.save();
	}
	
	public static Client client = new Client();
	
	public static class Client {
		
		public boolean internalPlaylists = false;
	}
	
}