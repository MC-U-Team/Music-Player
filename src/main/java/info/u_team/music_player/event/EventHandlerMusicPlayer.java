package info.u_team.music_player.event;

import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.key.MusicPlayerKeys;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.render.gui.*;
import info.u_team.music_player.render.overlay.RenderOverlayMusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class EventHandlerMusicPlayer {
	
	private IMusicPlayer musicplayer;
	
	private RenderOverlayMusicPlayer overlayrender;
	private GuiMenuButtonAddition guibuttons;
	
	public EventHandlerMusicPlayer(IMusicPlayer musicplayer) {
		this.musicplayer = musicplayer;
		
		overlayrender = new RenderOverlayMusicPlayer(musicplayer);
		overlayrender.setVisible(ClientConfig.settings.game_overlay);
		
		guibuttons = new GuiMenuButtonAddition(musicplayer, overlayrender);
	}
	
	@SubscribeEvent
	public void on(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.TEXT) {
			overlayrender.draw();
		}
	}
	
	@SubscribeEvent
	public void on(InitGuiEvent.Post event) {
		if (event.getGui() instanceof GuiIngameMenu) {
			guibuttons.initGui(event.getGui(), event.getButtonList());
		}
		
	}
	
	@SubscribeEvent
	public void on(ActionPerformedEvent.Pre event) {
		if (event.getGui() instanceof GuiIngameMenu) {
			guibuttons.actionPerformed(event.getButton());
		}
	}
	
	@SubscribeEvent
	public void on(KeyInputEvent event) {
		if (MusicPlayerKeys.key_openmusicplayer.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMusicPlayer(musicplayer));
		}
	}
}
