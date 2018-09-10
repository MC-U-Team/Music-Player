package info.u_team.music_player.impl.render.gui.button;

import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImageMusicPlayer extends GuiButtonImage {
	
	protected MusicPlayer musicplayer;
	
	public GuiButtonImageMusicPlayer(MusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resource) {
		this(musicplayer, id, x, y, width, height, resource, -1, -1);
	}
	
	public GuiButtonImageMusicPlayer(MusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resource, int color, int hovercolor) {
		super(id, x, y, width, height, resource, color, hovercolor);
		this.musicplayer = musicplayer;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		enabled = musicplayer.getTrackScheduler().getCurrentTrack() != null;
		super.drawButton(mc, mouseX, mouseY);
	}
}
