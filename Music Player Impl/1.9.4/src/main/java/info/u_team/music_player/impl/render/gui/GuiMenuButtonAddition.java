package info.u_team.music_player.impl.render.gui;

import java.util.List;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.config.Config;
import info.u_team.music_player.impl.render.gui.button.*;
import info.u_team.music_player.impl.render.gui.button.special.*;
import info.u_team.music_player.impl.render.overlay.RenderOverlayMusicPlayer;
import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiMenuButtonAddition {
	
	private MusicPlayer musicplayer;
	private RenderOverlayMusicPlayer overlayrender;
	
	public GuiMenuButtonAddition(MusicPlayer musicplayer, RenderOverlayMusicPlayer overlayrender) {
		this.musicplayer = musicplayer;
		this.overlayrender = overlayrender;
	}
	
	public void initGui(GuiScreen gui, List<GuiButton> buttonList) {
		GuiButtonImage open = new GuiButtonImageOpen(20, gui.width - 17, gui.height - 17, 16, 16);
		GuiButtonImage playpause = new GuiButtonImagePlayPause(musicplayer, 21, gui.width - 34, gui.height - 17, 16, 16);
		GuiButtonImage clear = new GuiButtonImageClear(musicplayer, 22, gui.width - 51, gui.height - 17, 16, 16);
		GuiButtonImage skip = new GuiButtonImageSkip(musicplayer, 23, gui.width - 68, gui.height - 17, 16, 16);
		GuiButtonImageActivated repeat = new GuiButtonImageRepeat(musicplayer, 24, gui.width - 85, gui.height - 17, 16, 16);
		GuiButtonImage shuffle = new GuiButtonImageShuffle(musicplayer, 25, gui.width - 102, gui.height - 17, 16, 16);
		GuiSlider volume = new GuiButtonSliderVolume(musicplayer, 26, gui.width - 173, gui.height - 17, 70, 16);
		
		GuiButtonExt toggle = new GuiButtonExt(27, gui.width - 151, 1, 150, 16, I18n.format(MusicPlayerConstants.MODID + ":button.toggle"));
		
		buttonList.add(open);
		buttonList.add(playpause);
		buttonList.add(clear);
		buttonList.add(skip);
		buttonList.add(repeat);
		buttonList.add(shuffle);
		buttonList.add(volume);
		buttonList.add(toggle);
	}
	
	public void actionPerformed(GuiButton button) {
		if (button.id == 20) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMusicPlayer(musicplayer));
		} else if (button.id == 21) {
			musicplayer.getTrackScheduler().setPaused(!musicplayer.getTrackScheduler().isPaused());
		} else if (button.id == 22) {
			musicplayer.getTrackScheduler().stop();
		} else if (button.id == 23) {
			musicplayer.getTrackScheduler().skip();
		} else if (button.id == 24) {
			musicplayer.getTrackScheduler().setRepeat(!musicplayer.getTrackScheduler().isRepeat());
		} else if (button.id == 25) {
			musicplayer.getTrackScheduler().setShuffle(!musicplayer.getTrackScheduler().isShuffle());
		} else if (button.id == 27) {
			boolean toggle = !Config.getOverlayDisplay();
			overlayrender.setVisible(toggle);
			Config.setOverlayDisplay(toggle);
		}
	}
}
