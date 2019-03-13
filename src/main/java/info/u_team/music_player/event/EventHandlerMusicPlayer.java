package info.u_team.music_player.event;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.init.MusicPlayerKeys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandlerMusicPlayer {
	
	// @SubscribeEvent
	// public static void on(KeyInputEvent event) {
	// System.out.println("TEST");
	// if (MusicPlayerKeys.open.isPressed()) {
	// Minecraft.getInstance().displayGuiScreen(new GuiMusicPlayer());
	// }
	// }
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		// System.out.println("TSET)");
		if (MusicPlayerKeys.open.isPressed()) {
			System.out.println("OPENING GUI");
			Minecraft.getInstance().displayGuiScreen(new GuiMusicPlayer());
		}
	}
	
}
