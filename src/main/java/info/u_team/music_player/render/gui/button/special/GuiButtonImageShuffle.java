package info.u_team.music_player.render.gui.button.special;

import static info.u_team.music_player.render.MusicPlayerTextures.texture_button_shuffle;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.render.gui.button.GuiButtonImageActivatedMusicPlayer;
import net.minecraft.client.Minecraft;

public class GuiButtonImageShuffle extends GuiButtonImageActivatedMusicPlayer {
	
	public GuiButtonImageShuffle(IMusicPlayer musicplayer, int id, int x, int y, int width, int height) {
		super(musicplayer, id, x, y, width, height, texture_button_shuffle, 0x80FF00FF);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		setActivated(musicplayer.getTrackScheduler().isShuffle());
		super.drawButton(mc, mouseX, mouseY, partial);
	}
}
