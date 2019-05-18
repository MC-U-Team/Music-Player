package info.u_team.music_player.config;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class ClientConfig {
	
	public static final ForgeConfigSpec config;
	private static final ClientConfig instance;
	
	static {
		Pair<ClientConfig, ForgeConfigSpec> pair = new Builder().configure(ClientConfig::new);
		config = pair.getRight();
		instance = pair.getLeft();
	}
	
	public static ClientConfig getInstance() {
		return instance;
	}
	
	public final BooleanValue internalPlaylists;
	
	private ClientConfig(Builder builder) {
		builder.comment("Client configuration settings").push("client");
		internalPlaylists = builder.comment("Should we use instead of the global appdata dir a local dir in configs for storing and loading our playlist").translation(MusicPlayerMod.modid + ":configgui.internalPlaylists").define("internalPlaylists", false);
		builder.pop();
	}
	
}
