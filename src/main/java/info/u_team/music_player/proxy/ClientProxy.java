package info.u_team.music_player.proxy;

import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.event.EventHandlerMusicPlayer;
import info.u_team.music_player.init.*;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.u_team_core.api.IModProxy;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy implements IModProxy {
	
	@Override
	public void construct() {
		super.construct();
		System.setProperty("http.agent", "Chrome");
		
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		
		DependencyManager.load();
	}
	
	@Override
	public void setup() {
		super.setup();
		MusicPlayerFiles.setup();
		MusicPlayerManager.setup();
		MinecraftForge.EVENT_BUS.register(new EventHandlerMusicPlayer(MusicPlayerManager.getSettingsManager().getSettings()));
	}
	
	@Override
	public void complete() {
		super.complete();
	}
}
