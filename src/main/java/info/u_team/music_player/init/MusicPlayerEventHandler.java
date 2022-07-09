package info.u_team.music_player.init;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.MusicPlayerUtils;
import info.u_team.music_player.musicplayer.SettingsManager;
import info.u_team.music_player.musicplayer.settings.IngameOverlayPosition;
import info.u_team.music_player.render.RenderOverlayMusicDisplay;
import info.u_team.u_team_core.gui.renderer.ScrollingTextRenderer;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
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
			if (!(mc.currentScreen instanceof GuiMusicPlayer)) {
				mc.displayGuiScreen(new GuiMusicPlayer());
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
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipBack();
			}
			handled = true;
		} else {
			handled = false;
		}
		return handled;
	}
	
	private static boolean isKeyDown(KeyBinding binding, boolean gui, int keyCode, int scanCode) {
		if (gui) {
			return binding.isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode));
		} else {
			return binding.isPressed();
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
				
				final MainWindow window = mc.getMainWindow();
				final int screenWidth = window.getScaledWidth();
				final int screenHeight = window.getScaledHeight();
				
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
				
				final MatrixStack matrixStack = event.getMatrixStack();
				
				matrixStack.push();
				matrixStack.translate(x, y, 500);
				overlayRender.render(matrixStack, 0, 0, event.getPartialTicks());
				matrixStack.pop();
			}
		}
		// }
	}
	
	// Used to add buttons and gui controls to main ingame gui
	
	private static ScrollingTextRenderer titleRender, authorRender;
	
	private static void onInitGuiPre(GuiScreenEvent.InitGuiEvent.Pre event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				gui.getEventListeners().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> {
							titleRender = controls.getTitleRender();
							authorRender = controls.getAuthorRender();
						});
			}
		}
	}
	
	private static void onInitGuiPost(GuiScreenEvent.InitGuiEvent.Post event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
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
				final List<IGuiEventListener> list = (List<IGuiEventListener>) gui.getEventListeners();
				list.add(controls);
			}
		}
	}
	
	private static void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				gui.getEventListeners().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.render(event.getMatrixStack(), event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks()));
			}
		}
	}
	
	private static void onMouseReleasePre(GuiScreenEvent.MouseReleasedEvent.Pre event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				gui.getEventListeners().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton()));
			}
		}
	}
	
	private static void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final Screen gui = Minecraft.getInstance().currentScreen;
			if (gui instanceof IngameMenuScreen) {
				if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
					gui.getEventListeners().stream() //
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
