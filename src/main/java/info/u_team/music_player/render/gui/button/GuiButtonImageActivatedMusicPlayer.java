package info.u_team.music_player.render.gui.button;

import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImageActivatedMusicPlayer extends GuiButtonImageActivated {
	
	protected IMusicPlayer musicplayer;
	
	public GuiButtonImageActivatedMusicPlayer(IMusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resource, int activecolor) {
		this(musicplayer, id, x, y, width, height, resource, -1, -1, activecolor);
	}
	
	public GuiButtonImageActivatedMusicPlayer(IMusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resource, int color, int hovercolor, int activecolor) {
		super(id, x, y, width, height, resource, color, hovercolor, activecolor);
		this.musicplayer = musicplayer;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		enabled = musicplayer.getTrackScheduler().getCurrentTrack() != null;
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	
}
