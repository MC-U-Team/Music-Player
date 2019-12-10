package info.u_team.music_player.event;

import java.util.List;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerKeys;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.*;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.music_player.render.RenderOverlayMusicDisplay;
import info.u_team.u_team_core.gui.render.ScrollingTextRender;
import net.minecraft.client.*;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandlerMusicPlayer {
	
	private final Settings settings;
	
	public EventHandlerMusicPlayer(Settings settings) {
		this.settings = settings;
	}
	
	// Used to listen to keyboard events
	
	@SubscribeEvent
	public void on(KeyInputEvent event) {
		handleKeyboard(false, -1, -1);
	}
	
	@SubscribeEvent
	public void on(KeyboardKeyPressedEvent.Post event) {
		if (settings.isKeyWorkInGui()) {
			event.setCanceled(handleKeyboard(true, event.getKeyCode(), event.getScanCode()));
		}
	}
	
	private boolean handleKeyboard(boolean gui, int keyCode, int scanCode) {
		final boolean handled;
		final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
		if (isKeyDown(MusicPlayerKeys.open, gui, keyCode, scanCode)) {
			final Minecraft mc = Minecraft.getInstance();
			if (!(mc.currentScreen instanceof GuiMusicPlayer)) {
				mc.displayGuiScreen(new GuiMusicPlayer());
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.pause, gui, keyCode, scanCode)) {
			if (manager.getCurrentTrack() != null) {
				manager.setPaused(!manager.isPaused());
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.skipForward, gui, keyCode, scanCode)) {
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipForward();
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.skipBack, gui, keyCode, scanCode)) {
			MusicPlayerUtils.skipBack();
			handled = true;
		} else {
			handled = false;
		}
		return handled;
	}
	
	private boolean isKeyDown(KeyBinding binding, boolean gui, int keyCode, int scanCode) {
		if (gui) {
			return binding.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode));
		} else {
			return binding.isPressed();
		}
	}
	
	private RenderOverlayMusicDisplay overlayRender;
	
	// Render overlay
	
	@SubscribeEvent
	public void on(RenderGameOverlayEvent.Pre event) {
		final Minecraft mc = Minecraft.getInstance();
		if (event.getType() == ElementType.TEXT && !mc.gameSettings.showDebugInfo && mc.currentScreen == null) {
			if (settings.isShowIngameOverlay()) {
				if (overlayRender == null) {
					overlayRender = new RenderOverlayMusicDisplay();
				}
				IngameOverlayPosition position = settings.getIngameOverlayPosition();
				
				final MainWindow window = mc.mainWindow;
				final int width = window.getScaledWidth();
				final int height = window.getScaledHeight();
				
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
	
	// Used to add buttons and gui controls to main ingame gui
	
	private ScrollingTextRender titleRender, authorRender;
	
	@SubscribeEvent
	public void on(GuiScreenEvent.InitGuiEvent.Pre event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			if (settings.isShowIngameMenueOverlay()) {
				gui.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> {
							titleRender = controls.getTitleRender();
							authorRender = controls.getAuthorRender();
						});
			}
		}
	}
	
	@SubscribeEvent
	public void on(GuiScreenEvent.InitGuiEvent.Post event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			if (settings.isShowIngameMenueOverlay()) {
				final GuiControls controls = new GuiControls(gui, 3, gui.width);
				if (titleRender != null) {
					controls.setTitleRender(titleRender);
					titleRender = null;
				}
				if (authorRender != null) {
					controls.setAuthorRender(authorRender);
					authorRender = null;
				}
				@SuppressWarnings("unchecked")
				List<IGuiEventListener> list = (List<IGuiEventListener>) gui.children();
				list.add(controls);
			}
		}
	}
	
	@SubscribeEvent
	public void on(GuiScreenEvent.DrawScreenEvent event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			if (settings.isShowIngameMenueOverlay()) {
				gui.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks()));
			}
		}
	}
	
	@SubscribeEvent
	public void on(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final Screen gui = Minecraft.getInstance().currentScreen;
			if (gui instanceof IngameMenuScreen) {
				if (settings.isShowIngameMenueOverlay()) {
					gui.children().stream() //
							.filter(element -> element instanceof GuiControls) //
							.map(element -> ((GuiControls) element)).findAny() //
							.ifPresent(GuiControls::tick);
				}
			}
		}
	}
}
