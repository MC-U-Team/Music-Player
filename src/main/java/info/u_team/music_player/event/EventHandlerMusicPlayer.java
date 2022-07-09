package info.u_team.music_player.event;

import org.lwjgl.input.Keyboard;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.ingame.GuiIngameMenuCustom;
import info.u_team.music_player.init.MusicPlayerKeys;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.*;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.music_player.render.RenderOverlayMusicDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class EventHandlerMusicPlayer {
	
	private final Settings settings;
	
	public EventHandlerMusicPlayer(Settings settings) {
		this.settings = settings;
	}
	
	// Used to listen to keyboard events
	
	@SubscribeEvent
	public void on(KeyInputEvent event) {
		handleKeyboard(false, -1);
	}
	
	@SubscribeEvent
	public void on(KeyboardInputEvent.Post event) {
		if (settings.isKeyWorkInGui()) {
			if (Keyboard.getEventKeyState())
				event.setCanceled(handleKeyboard(true, Keyboard.getEventKey()));
		}
	}
	
	private boolean handleKeyboard(boolean gui, int keyCode) {
		final boolean handled;
		final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
		final Minecraft mc = Minecraft.getMinecraft();
		if (isKeyDown(MusicPlayerKeys.open, gui, keyCode)) {
			if (!(mc.currentScreen instanceof GuiMusicPlayer)) {
				mc.displayGuiScreen(new GuiMusicPlayer());
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.pause, gui, keyCode)) {
			if (manager.getCurrentTrack() != null) {
				manager.setPaused(!manager.isPaused());
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.skipForward, gui, keyCode)) {
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipForward();
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.skipBack, gui, keyCode)) {
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipBack();
			}
			handled = true;
		} else {
			handled = false;
		}
		if (handled) {
			mc.dispatchKeypresses();
		}
		return handled;
	}
	
	private boolean isKeyDown(KeyBinding binding, boolean gui, int keyCode) {
		if (gui) {
			return binding.isActiveAndMatches(keyCode);
		} else {
			return binding.isPressed();
		}
	}
	
	private RenderOverlayMusicDisplay overlayRender;
	
	// Render overlay
	
	@SubscribeEvent
	public void on(RenderGameOverlayEvent.Pre event) {
		final Minecraft mc = Minecraft.getMinecraft();
		if (event.getType() == ElementType.TEXT && !mc.gameSettings.showDebugInfo && mc.currentScreen == null) {
			if (settings.isShowIngameOverlay()) {
				if (overlayRender == null) {
					overlayRender = new RenderOverlayMusicDisplay();
				}
				IngameOverlayPosition position = settings.getIngameOverlayPosition();
				
				final ScaledResolution scaledResolution = new ScaledResolution(mc);
				final int width = scaledResolution.getScaledWidth();
				final int height = scaledResolution.getScaledHeight();
				
				final int x;
				if (position.isLeft()) {
					x = 3;
				} else {
					x = width - 3 - overlayRender.getWidth();
				}
				
				final int y;
				if (position.isUp()) {
					y = 3;
				} else {
					y = height - 3 - overlayRender.getHeight();
				}
				
				overlayRender.draw(x, y);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void on(GuiScreenEvent.InitGuiEvent.Pre event) {
		final GuiScreen gui = event.getGui();
		if (gui instanceof GuiIngameMenu) {
			if (settings.isShowIngameMenueOverlay()) {
				gui.mc.displayGuiScreen(new GuiIngameMenuCustom());
			}
		}
	}
}