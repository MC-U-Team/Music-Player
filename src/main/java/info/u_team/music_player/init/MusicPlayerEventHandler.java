package info.u_team.music_player.init;

import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.MusicPlayerUtils;
import info.u_team.music_player.musicplayer.SettingsManager;
import info.u_team.music_player.musicplayer.settings.IngameOverlayPosition;
import info.u_team.music_player.render.RenderOverlayMusicDisplay;
import info.u_team.u_team_core.gui.elements.ScrollingText;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenEvent.KeyboardKeyPressedEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;

public class MusicPlayerEventHandler {
	
	private static final SettingsManager SETTINGS_MANAGER = MusicPlayerManager.getSettingsManager();
	
	// Used to listen to keyboard events
	
	private static void onKeyInput(KeyInputEvent event) {
		handleKeyboard(false, -1, -1);
	}
	
	private static void onKeyboardPressed(KeyboardKeyPressedEvent.Post event) {
		if (SETTINGS_MANAGER.getSettings().isKeyWorkInGui()) {
			event.setCanceled(handleKeyboard(true, event.getKeyCode(), event.getScanCode()));
		}
	}
	
	private static boolean handleKeyboard(boolean gui, int keyCode, int scanCode) {
		final boolean handled;
		final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
		if (isKeyDown(MusicPlayerKeys.OPEN, gui, keyCode, scanCode)) {
			final Minecraft mc = Minecraft.getInstance();
			if (!(mc.screen instanceof GuiMusicPlayer)) {
				mc.setScreen(new GuiMusicPlayer());
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.PAUSE, gui, keyCode, scanCode)) {
			if (manager.getCurrentTrack() != null) {
				manager.setPaused(!manager.isPaused());
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.SKIP_FORWARD, gui, keyCode, scanCode)) {
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipForward();
			}
			handled = true;
		} else if (isKeyDown(MusicPlayerKeys.SKIP_BACK, gui, keyCode, scanCode)) {
			MusicPlayerUtils.skipBack();
			handled = true;
		} else {
			handled = false;
		}
		return handled;
	}
	
	private static boolean isKeyDown(KeyMapping binding, boolean gui, int keyCode, int scanCode) {
		if (gui) {
			return binding.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode));
		} else {
			return binding.consumeClick();
		}
	}
	
	private static RenderOverlayMusicDisplay overlayRender;
	
	// Render overlay
	
	private static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
		final Minecraft mc = Minecraft.getInstance();
		// if (event.getType() == ElementType.TEXT && !mc.gameSettings.showDebugInfo && mc.currentScreen == null) {
		if (event.getType() == ElementType.TEXT) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameOverlay()) {
				final IngameOverlayPosition position = SETTINGS_MANAGER.getSettings().getIngameOverlayPosition();
				
				if (overlayRender == null) {
					overlayRender = new RenderOverlayMusicDisplay();
				}
				
				final Window window = mc.getWindow();
				final int screenWidth = window.getGuiScaledWidth();
				final int screenHeight = window.getGuiScaledHeight();
				
				final int height = overlayRender.getHeight();
				final int width = overlayRender.getWidth();
				
				final int x;
				if (position.isLeft()) {
					x = 3;
				} else {
					x = screenWidth - 3 - width;
				}
				
				final int y;
				if (position.isUp()) {
					y = 3;
				} else {
					y = screenHeight - 3 - height;
				}
				
				final PoseStack matrixStack = event.getMatrixStack();
				
				matrixStack.pushPose();
				matrixStack.translate(x, y, 500);
				overlayRender.render(matrixStack, 0, 0, event.getPartialTicks());
				matrixStack.popPose();
			}
		}
		// }
	}
	
	// Used to add buttons and gui controls to main ingame gui
	
	private static ScrollingText titleRender, authorRender;
	
	private static void onInitGuiPre(ScreenEvent.InitScreenEvent.Pre event) {
		final Screen gui = event.getScreen();
		if (gui instanceof PauseScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
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
	
	private static void onInitGuiPost(ScreenEvent.InitScreenEvent.Post event) {
		final Screen gui = event.getScreen();
		if (gui instanceof PauseScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				final GuiControls controls = new GuiControls(gui, 3, gui.width);
				if (titleRender != null) {
					controls.copyTitleRendererState(titleRender);
					titleRender = null;
				}
				if (authorRender != null) {
					controls.copyAuthorRendererState(authorRender);
					authorRender = null;
				}
				@SuppressWarnings("unchecked")
				final List<GuiEventListener> list = (List<GuiEventListener>) gui.children();
				list.add(controls);
			}
		}
	}
	
	private static void onDrawScreenPost(ScreenEvent.DrawScreenEvent.Post event) {
		final Screen gui = event.getScreen();
		if (gui instanceof PauseScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				gui.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.render(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks()));
			}
		}
	}
	
	private static void onMouseReleasePre(ScreenEvent.MouseReleasedEvent.Pre event) {
		final Screen gui = event.getScreen();
		if (gui instanceof PauseScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				gui.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton()));
			}
		}
	}
	
	private static void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final Screen gui = Minecraft.getInstance().screen;
			if (gui instanceof PauseScreen) {
				if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
					gui.children().stream() //
							.filter(element -> element instanceof GuiControls) //
							.map(element -> ((GuiControls) element)).findAny() //
							.ifPresent(GuiControls::tick);
				}
			}
		}
	}
	
	public static void registerForge(IEventBus bus) {
		bus.addListener(MusicPlayerEventHandler::onKeyInput);
		bus.addListener(MusicPlayerEventHandler::onKeyboardPressed);
		
		bus.addListener(MusicPlayerEventHandler::onRenderGameOverlay);
		
		bus.addListener(MusicPlayerEventHandler::onInitGuiPre);
		bus.addListener(MusicPlayerEventHandler::onInitGuiPost);
		bus.addListener(MusicPlayerEventHandler::onDrawScreenPost);
		bus.addListener(MusicPlayerEventHandler::onMouseReleasePre);
		bus.addListener(MusicPlayerEventHandler::onClientTick);
	}
}
