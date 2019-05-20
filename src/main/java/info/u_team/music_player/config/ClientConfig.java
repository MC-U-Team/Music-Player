package info.u_team.music_player.config;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = MusicPlayerMod.modid, name = MusicPlayerMod.modid + "-client", category = "")
public class ClientConfig {
	
	@Comment("Client configuration settings")
	public static Client client = new Client();
	
	public static class Client {
		
		@Comment("Should we use instead of the global appdata dir a local dir in configs for storing and loading our playlist")
		@Name("internalPlaylists")
		@RequiresMcRestart
		public boolean internalPlaylists = false;
		
	}
}