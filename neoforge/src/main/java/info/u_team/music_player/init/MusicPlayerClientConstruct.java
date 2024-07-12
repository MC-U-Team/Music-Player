package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.musicplayer.MusicPlayerInitManager;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig.Type;

@Construct(modid = MusicPlayerMod.MODID, client = true)
public class MusicPlayerClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		System.setProperty("http.agent", "Chrome");
		
		ModLoadingContext.get().getActiveContainer().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		
		DependencyManager.load();
		
		BusRegister.registerMod(MusicPlayerInitManager::registerMod);
		BusRegister.registerMod(MusicPlayerKeys::registerMod);
		
		BusRegister.registerNeoForge(MusicPlayerEventHandler::registerNeoForge);
	}
	
}
