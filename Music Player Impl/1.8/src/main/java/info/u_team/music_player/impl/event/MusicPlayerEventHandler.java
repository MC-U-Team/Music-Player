package info.u_team.music_player.impl.event;

import info.u_team.music_player.config.Config;
import info.u_team.music_player.impl.key.MusicPlayerKeys;
import info.u_team.music_player.impl.render.gui.*;
import info.u_team.music_player.impl.render.overlay.RenderOverlayMusicPlayer;
import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class MusicPlayerEventHandler {
	
	private MusicPlayer musicplayer;
	
	private RenderOverlayMusicPlayer overlayrender;
	private GuiMenuButtonAddition guibuttons;
	
	public MusicPlayerEventHandler(MusicPlayer musicplayer) {
		this.musicplayer = musicplayer;
		
		overlayrender = new RenderOverlayMusicPlayer(musicplayer);
		overlayrender.setVisible(Config.getOverlayDisplay());
		
		guibuttons = new GuiMenuButtonAddition(musicplayer, overlayrender);
	}
	
	public void on(RenderGameOverlayEvent event) {
		if (event.type == ElementType.TEXT) {
			overlayrender.draw();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void on(InitGuiEvent.Post event) {
		if (event.gui instanceof GuiIngameMenu) {
			guibuttons.initGui(event.gui, event.buttonList);
		}
		
	}
	
	public void on(ActionPerformedEvent.Pre event) {
		if (event.gui instanceof GuiIngameMenu) {
			guibuttons.actionPerformed(event.button);
		}
	}
	
	public void on(KeyInputEvent event) {
		if (MusicPlayerKeys.key_openmusicplayer.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMusicPlayer(musicplayer));
		}
	}
}
