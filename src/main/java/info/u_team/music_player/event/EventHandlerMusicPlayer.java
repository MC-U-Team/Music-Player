package info.u_team.music_player.event;

import java.util.List;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.playing.GuiControls;
import info.u_team.music_player.init.MusicPlayerKeys;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;

public class EventHandlerMusicPlayer {

	private static final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();

	@SubscribeEvent
	public static void on(KeyInputEvent event) {
		if (MusicPlayerKeys.open.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new GuiMusicPlayer());
		}
	}

	@SubscribeEvent
	public static void on(GuiScreenEvent.InitGuiEvent.Post event) {
		final GuiScreen gui = event.getGui();
		if (gui instanceof GuiIngameMenu) {
			if (settings.isShowIngameMenueOverlay()) {
				@SuppressWarnings("unchecked")
				List<IGuiEventListener> list = (List<IGuiEventListener>) gui.getChildren();
				list.add(new GuiControls(-3, gui.width));
			}
		}
	}

	@SubscribeEvent
	public static void on(GuiScreenEvent.DrawScreenEvent event) {
		if (settings.isShowIngameMenueOverlay()) {
			final GuiScreen gui = event.getGui();
			if (gui instanceof GuiIngameMenu) {
				gui.getChildren().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks()));
			}
		}
	}

	@SubscribeEvent
	public static void on(ClientTickEvent event) {
		if (settings.isShowIngameMenueOverlay()) {
			if (event.phase == Phase.END) {
				final GuiScreen gui = Minecraft.getInstance().currentScreen;
				if (gui instanceof GuiIngameMenu) {
					gui.getChildren().stream() //
							.filter(element -> element instanceof GuiControls) //
							.map(element -> ((GuiControls) element)).findAny() //
							.ifPresent(GuiControls::tick);
				}
			}
		}
	}
}
