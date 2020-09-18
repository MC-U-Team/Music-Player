package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.event.MusicPlayerEventHandler;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.u_team_core.api.construct.*;
import info.u_team.u_team_core.util.registry.BusRegister;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@Construct(modid = MusicPlayerMod.MODID, client = true)
public class MusicPlayerClientConstruct implements IModConstruct {
	
	@Override
	public void construct() {
		System.setProperty("http.agent", "Chrome");
		
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		
		DependencyManager.load();
		
		MusicPlayerFiles.load();
		
		BusRegister.registerMod(MusicPlayerManager::registerMod);
		
		BusRegister.registerForge(MusicPlayerEventHandler::registerForge);
	}
	
}
