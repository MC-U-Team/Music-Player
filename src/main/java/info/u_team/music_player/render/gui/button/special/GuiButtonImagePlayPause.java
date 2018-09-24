package info.u_team.music_player.render.gui.button.special;

import static info.u_team.music_player.render.MusicPlayerTextures.*;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.render.gui.button.GuiButtonImageStateMusicPlayer;
import net.minecraft.client.Minecraft;

public class GuiButtonImagePlayPause extends GuiButtonImageStateMusicPlayer {
	
	public GuiButtonImagePlayPause(IMusicPlayer musicplayer, int id, int x, int y, int width, int height) {
		super(musicplayer, id, x, y, width, height, texture_button_play, texture_button_pause);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		if (musicplayer.getTrackScheduler().isPaused()) {
			setTexture1();
		} else {
			setTexture2();
		}
		super.drawButton(mc, mouseX, mouseY, partial);
	}
}
