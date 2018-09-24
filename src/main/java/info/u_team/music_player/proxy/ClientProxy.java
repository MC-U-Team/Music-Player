package info.u_team.music_player.proxy;

import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.key.MusicPlayerKeys;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy extends CommonProxy {
	
	private IMusicPlayer musicplayer;
	
	public void preinit(FMLPreInitializationEvent event) {
		super.preinit(event);
		DependencyManager.init();
		musicplayer = DependencyManager.getMusicPlayer();
	}
	
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MusicPlayerKeys.init();
		musicplayer.setVolume(ClientConfig.settings.music_volume);
	}
	
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
		musicplayer.startAudioOutput();
	}
	
}
