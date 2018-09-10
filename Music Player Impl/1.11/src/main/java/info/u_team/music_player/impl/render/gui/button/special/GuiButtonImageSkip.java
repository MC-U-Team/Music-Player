package info.u_team.music_player.impl.render.gui.button.special;

import static info.u_team.music_player.impl.render.MusicPlayerTextures.texture_button_skip;

import info.u_team.music_player.impl.render.gui.button.GuiButtonImageMusicPlayer;
import net.hycrafthd.basiclavamusicplayer.MusicPlayer;

public class GuiButtonImageSkip extends GuiButtonImageMusicPlayer {
	
	public GuiButtonImageSkip(MusicPlayer musicplayer, int id, int x, int y, int width, int height) {
		super(musicplayer, id, x, y, width, height, texture_button_skip);
	}
}
