package info.u_team.music_player.config;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class ClientConfig {
	
	public static final ModConfigSpec CONFIG;
	private static final ClientConfig INSTANCE;
	
	static {
		final Pair<ClientConfig, ModConfigSpec> pair = new Builder().configure(ClientConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static ClientConfig getInstance() {
		return INSTANCE;
	}
	
	public final BooleanValue internalPlaylists;
	
	private ClientConfig(Builder builder) {
		builder.comment("Client configuration settings").push("client");
		internalPlaylists = builder.comment("Should we use instead of the global appdata dir a local dir in configs for storing and loading our playlist").worldRestart().define("internalPlaylists", false);
		builder.pop();
	}
	
}
