package info.u_team.music_player.musicplayer;

import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class MusicPlayerInitManager {
	
	private static void setup(FMLClientSetupEvent event) {
		MusicPlayerManager.setup(DependencyManager.MUSICPLAYER_CLASSLOADER, ClientConfig.getInstance().internalPlaylists.get());
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(MusicPlayerInitManager::setup);
	}
	
}
