package info.u_team.music_player.event;

import java.util.List;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerKeys;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.music_player.render.RenderOverlayMusicDisplay;
import info.u_team.to_u_team_core.export.RenderScrollingText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;

public class EventHandlerMusicPlayer {
	
	private final Settings settings;
	
	public EventHandlerMusicPlayer(Settings settings) {
		this.settings = settings;
	}
	
	// Used to listen to keyboard events
	
	@SubscribeEvent
	public void on(KeyInputEvent event) {
		if (MusicPlayerKeys.open.isPressed()) {
			Minecraft.getInstance().displayGuiScreen(new GuiMusicPlayer());
		}
	}
	
	private RenderOverlayMusicDisplay overlayRender;
	
	// Render overlay
	
	@SubscribeEvent
	public void on(RenderGameOverlayEvent.Pre event) {
		final Minecraft mc = Minecraft.getInstance();
		if (event.getType() == ElementType.TEXT && !mc.gameSettings.showDebugInfo /* && mc.currentScreen == null */) {
			if (settings.isShowIngameOverlay()) {
				if (overlayRender == null) {
					overlayRender = new RenderOverlayMusicDisplay();
				}
				overlayRender.draw(5, 5);
			}
		}
	}
	
	// Used to add buttons and gui controls to main ingame gui
	
	private RenderScrollingText titleRender, authorRender;
	
	@SubscribeEvent
	public void on(GuiScreenEvent.InitGuiEvent.Pre event) {
		final GuiScreen gui = event.getGui();
		if (gui instanceof GuiIngameMenu) {
			if (settings.isShowIngameMenueOverlay()) {
				gui.getChildren().stream() //
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
		final GuiScreen gui = event.getGui();
		if (gui instanceof GuiIngameMenu) {
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
				List<IGuiEventListener> list = (List<IGuiEventListener>) gui.getChildren();
				list.add(controls);
			}
		}
	}
	
	@SubscribeEvent
	public void on(GuiScreenEvent.DrawScreenEvent event) {
		final GuiScreen gui = event.getGui();
		if (gui instanceof GuiIngameMenu) {
			if (settings.isShowIngameMenueOverlay()) {
				gui.getChildren().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.drawScreen(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks()));
			}
		}
	}
	
	@SubscribeEvent
	public void on(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			final GuiScreen gui = Minecraft.getInstance().currentScreen;
			if (gui instanceof GuiIngameMenu) {
				if (settings.isShowIngameMenueOverlay()) {
					gui.getChildren().stream() //
							.filter(element -> element instanceof GuiControls) //
							.map(element -> ((GuiControls) element)).findAny() //
							.ifPresent(GuiControls::tick);
				}
			}
		}
	}
}
