package info.u_team.music_player.impl.render.gui.button;

import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiButtonImageStateMusicPlayer extends GuiButtonImageState {
	
	protected MusicPlayer musicplayer;
	
	public GuiButtonImageStateMusicPlayer(MusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resourceFirst, ResourceLocation resourceSecond) {
		this(musicplayer, id, x, y, width, height, resourceFirst, resourceSecond, -1, -1);
	}
	
	public GuiButtonImageStateMusicPlayer(MusicPlayer musicplayer, int id, int x, int y, int width, int height, ResourceLocation resourceFirst, ResourceLocation resourceSecond, int color, int hovercolor) {
		super(id, x, y, width, height, resourceFirst, resourceSecond, color, hovercolor);
		this.musicplayer = musicplayer;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
		enabled = musicplayer.getTrackScheduler().getCurrentTrack() != null;
		super.drawButton(mc, mouseX, mouseY, partial);
	}
	
}
