package info.u_team.music_player.render;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.math.MathHelper;

public class RenderOverlayMusicDisplay {
	
	private final ITrackManager manager;
	
	private int width;
	private int height;
	
	private final RenderScrollingText title;
	private final RenderScrollingText author;
	
	private final RenderScalingText position;
	private final RenderScalingText duration;
	
	public RenderOverlayMusicDisplay() {
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		height = 35;
		width = 120;
		
		final FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
		
		title = new RenderScrollingText(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedTitle()));
		title.setStepSize(0.5F);
		title.setColor(0xFFFF00);
		title.setWidth(114);
		title.setSpeedTime(35);
		
		author = new RenderScrollingText(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedAuthor()));
		author.setStepSize(0.5F);
		author.setColor(0xFFFF00);
		author.setScale(0.75F);
		author.setWidth(114);
		author.setSpeedTime(35);
		
		position = new RenderScalingText(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(GuiTrackUtils::getFormattedPosition));
		position.setColor(0xFFFF00);
		position.setScale(0.5F);
		
		duration = new RenderScalingText(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(GuiTrackUtils::getFormattedDuration));
		duration.setColor(0xFFFF00);
		duration.setScale(0.5F);
		
	}
	
	public void draw(float x, float y) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track == null) {
			return;
		}
		final int intX = MathHelper.ceil(x);
		final int intY = MathHelper.ceil(y);
		// Background
		Gui.drawRect(intX, intY, intX + width, intY + height, 0xFF212121);
		
		// Progressbar
		final double progress;
		if (track.getInfo().isStream()) {
			progress = 0.5;
		} else {
			progress = (double) track.getPosition() / track.getDuration();
		}
		
		Gui.drawRect(intX + 6, intY + 23, intX + width - 6, intY + 26, 0xFF555555);
		Gui.drawRect(intX + 6, intY + 23, intX + 6 + (int) ((width - 12) * progress), intY + 26, 0xFF3e9100);
		
		// Draw strings
		title.draw(x + 3, y + 2);
		author.draw(x + 3, y + 12);
		
		position.draw(x + 6, y + 28);
		duration.draw(x + width - 6 - duration.getTextWidth(), y + 28);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
