package info.u_team.music_player.event;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.init.MusicPlayerKeys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlerMusicPlayer {
	
	@SubscribeEvent
	public static void on(KeyInputEvent event) {
		if (MusicPlayerKeys.open.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new GuiMusicPlayer());
		}
	}
	
}
