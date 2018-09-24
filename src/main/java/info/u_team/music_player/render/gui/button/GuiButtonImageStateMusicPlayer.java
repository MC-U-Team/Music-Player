package info.u_team.music_player.render.gui.button;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImageStateMusicPlayer extends GuiButtonImageState {
	
	protected IMusicPlayer musicplayer;
	
	public GuiButtonImageStateMusicPlayer(IMusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resourceFirst, ResourceLocation resourceSecond) {
		this(musicplayer, id, x, y, width, height, resourceFirst, resourceSecond, -1, -1);
	}
	
	public GuiButtonImageStateMusicPlayer(IMusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resourceFirst, ResourceLocation resourceSecond, int color, int hovercolor) {
		super(id, x, y, width, height, resourceFirst, resourceSecond, color, hovercolor);
		this.musicplayer = musicplayer;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		enabled = musicplayer.getTrackScheduler().getCurrentTrack() != null;
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	
}
