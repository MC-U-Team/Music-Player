package info.u_team.music_player.render.gui.button.special;

import static info.u_team.music_player.render.MusicPlayerTextures.texture_button_skip;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.render.gui.button.GuiButtonImageMusicPlayer;

public class GuiButtonImageSkip extends GuiButtonImageMusicPlayer {
	
	public GuiButtonImageSkip(IMusicPlayer musicplayer, int id, int x, int y, int width, int height) {
		super(musicplayer, id, x, y, width, height, texture_button_skip);
	}
}
