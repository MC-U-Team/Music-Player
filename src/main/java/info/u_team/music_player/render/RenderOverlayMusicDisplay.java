package info.u_team.music_player.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerColors;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.u_team_core.gui.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class RenderOverlayMusicDisplay implements IRenderable {
	
	private final ITrackManager manager;
	
	private final int width;
	private final int height;
	
	private final ScrollingTextRenderer title;
	private final ScrollingTextRenderer author;
	
	private final ScalingTextRenderer position;
	private final ScalingTextRenderer duration;
	
	public RenderOverlayMusicDisplay() {
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		height = 35;
		width = 120;
		
		final FontRenderer fontRender = Minecraft.getInstance().fontRenderer;
		
		title = new ScrollingTextRenderer(fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedTitle()), 3, 2);
		title.setStepSize(0.5F);
		title.setColor(MusicPlayerColors.YELLOW);
		title.setWidth(114);
		title.setSpeedTime(35);
		
		author = new ScrollingTextRenderer(fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedAuthor()), 3, 12);
		author.setStepSize(0.5F);
		author.setColor(MusicPlayerColors.YELLOW);
		author.setScale(0.75F);
		author.setWidth(114);
		author.setSpeedTime(35);
		
		position = new ScalingTextRenderer(fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(GuiTrackUtils::getFormattedPosition), 6, 28);
		position.setColor(MusicPlayerColors.YELLOW);
		position.setScale(0.5F);
		
		duration = new ScalingTextRenderer(fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(GuiTrackUtils::getFormattedDuration), width - 6, 28);
		duration.setTextChanged(renderer -> {
			duration.setX(width - 6 - renderer.getTextWidth());
		});
		duration.setColor(MusicPlayerColors.YELLOW);
		duration.setScale(0.5F);
		
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track == null) {
			return;
		}
		// Background
		AbstractGui.fill(matrixStack, 0, 0, width, height, 0xFF212121);
		
		// Progressbar
		final double progress;
		if (track.getInfo().isStream()) {
			progress = 0.5;
		} else {
			progress = (double) track.getPosition() / track.getDuration();
		}
		
		AbstractGui.fill(matrixStack, 6, 23, width - 6, 26, 0xFF555555);
		AbstractGui.fill(matrixStack, 6, 23, 6 + (int) ((width - 12) * progress), 26, 0xFF3E9100);
		
		// Draw strings
		title.render(matrixStack, mouseX, mouseY, partialTicks);
		author.render(matrixStack, mouseX, mouseY, partialTicks);
		
		position.render(matrixStack, mouseX, mouseY, partialTicks);
		duration.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
