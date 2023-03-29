package info.u_team.music_player.init;

import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
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
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;

public class MusicPlayerEventHandler {
	
	private static final SettingsManager SETTINGS_MANAGER = MusicPlayerManager.getSettingsManager();
	
	// Used to listen to keyboard events
	
	public static void onKeyInput() {
		handleKeyboard(false, -1, -1);
	}
	
	public static boolean onKeyboardPressed(int keyCode, int scanCode) {
		if (SETTINGS_MANAGER.getSettings().isKeyWorkInGui()) {
			return handleKeyboard(true, keyCode, scanCode);
		}
		return false;
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
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipBack();
			}
			handled = true;
		} else {
			handled = false;
		}
		return handled;
	}
	
	private static boolean isKeyDown(KeyMapping binding, boolean gui, int keyCode, int scanCode) {
		if (gui) {
			final Key key = InputConstants.getKey(keyCode, scanCode);
			return key != InputConstants.UNKNOWN && key.equals(KeyBindingHelper.getBoundKeyOf(binding));
		} else {
			return binding.consumeClick();
		}
	}
	
	private static RenderOverlayMusicDisplay overlayRender;
	
	// Render overlay
	
	public static void onRenderGameOverlay(PoseStack poseStack, float partialTick) {
		final Minecraft mc = Minecraft.getInstance();
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
			
			poseStack.pushPose();
			poseStack.translate(x, y, 500);
			overlayRender.render(poseStack, 0, 0, partialTick);
			poseStack.popPose();
		}
	}
	
	// Used to add buttons and gui controls to main ingame gui
	
	private static ScrollingText titleRender, authorRender;
	
	private static void onPreInitScreen(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
		if (screen instanceof PauseScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				screen.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> {
							titleRender = controls.getTitleRender();
							authorRender = controls.getAuthorRender();
						});
			}
		}
	}
	
	private static void onPostInitScreen(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
		if (screen instanceof PauseScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				final GuiControls controls = new GuiControls(screen, 3, screen.width);
				if (titleRender != null) {
					controls.copyTitleRendererState(titleRender);
					titleRender = null;
				}
				if (authorRender != null) {
					controls.copyAuthorRendererState(authorRender);
					authorRender = null;
				}
				@SuppressWarnings("unchecked")
				final List<GuiEventListener> list = (List<GuiEventListener>) screen.children();
				list.add(controls);
			}
		}
	}
	
	private static void onDrawScreenPost(Screen screen, PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
			screen.children().stream() //
					.filter(element -> element instanceof GuiControls) //
					.map(element -> ((GuiControls) element)).findAny() //
					.ifPresent(controls -> controls.render(poseStack, mouseX, mouseY, partialTick));
		}
	}
	
	private static void onMouseReleasePre(Screen screen, double mouseX, double mouseY, int button) {
		if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
			screen.children().stream() //
					.filter(element -> element instanceof GuiControls) //
					.map(element -> ((GuiControls) element)).findAny() //
					.ifPresent(controls -> controls.mouseReleased(mouseX, mouseY, button));
		}
	}
	
	private static void onClientTick(Screen screen) {
		if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
			screen.children().stream() //
					.filter(element -> element instanceof GuiControls) //
					.map(element -> ((GuiControls) element)).findAny() //
					.ifPresent(GuiControls::tick);
		}
	}
	
	public static void register() {
		ScreenEvents.BEFORE_INIT.register(MusicPlayerEventHandler::onPreInitScreen);
		ScreenEvents.AFTER_INIT.register(MusicPlayerEventHandler::onPostInitScreen);
		
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof PauseScreen) {
				ScreenEvents.afterRender(screen).register(MusicPlayerEventHandler::onDrawScreenPost);
				ScreenMouseEvents.beforeMouseRelease(screen).register(MusicPlayerEventHandler::onMouseReleasePre);
				ScreenEvents.afterTick(screen).register(MusicPlayerEventHandler::onClientTick);
			}
		});
	}
	
}
