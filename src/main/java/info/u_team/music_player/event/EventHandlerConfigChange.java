package info.u_team.music_player.event;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class EventHandlerConfigChange {
	
	private IMusicPlayer musicplayer;
	
	public EventHandlerConfigChange(IMusicPlayer musicplayer) {
		this.musicplayer = musicplayer;
	}
	
	@SubscribeEvent
	public void on(OnConfigChangedEvent event) {
		if (event.getModID().equals(MusicPlayerConstants.MODID)) {
			ConfigManager.sync(MusicPlayerConstants.MODID, Type.INSTANCE);
			musicplayer.setVolume(ClientConfig.settings.music_volume);
		}
	}
}
