package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.u_team_core.api.construct.Construct;
import info.u_team.u_team_core.api.construct.ModConstruct;
import info.u_team.u_team_core.util.registry.BusRegister;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Construct(modid = MusicPlayerMod.MODID, client = true)
public class MusicPlayerClientConstruct implements ModConstruct {
	
	@Override
	public void construct() {
		System.setProperty("http.agent", "Chrome");
		
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		
		DependencyManager.load();
		
		BusRegister.registerMod(bus -> bus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, event -> {
			MusicPlayerFiles.load();
			MusicPlayerManager.setup();
			BusRegister.registerForge(MusicPlayerEventHandler::registerForge);
		}));
		
		BusRegister.registerMod(MusicPlayerKeys::registerMod);
	}
	
}
