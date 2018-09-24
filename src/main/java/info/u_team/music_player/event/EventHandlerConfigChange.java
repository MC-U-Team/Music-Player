package info.u_team.music_player.event;

import info.u_team.music_player.MusicPlayerConstants;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class EventHandlerConfigChange {
	
	@SubscribeEvent
	public static void onConfigChangedEvent(OnConfigChangedEvent event) {
		if (event.getModID().equals(MusicPlayerConstants.MODID)) {
			ConfigManager.sync(MusicPlayerConstants.MODID, Type.INSTANCE);
		}
	}
}
