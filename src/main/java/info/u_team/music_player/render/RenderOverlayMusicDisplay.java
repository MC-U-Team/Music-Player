package info.u_team.music_player.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerColors;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.IngameOverlayPosition;
import info.u_team.u_team_core.gui.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.math.MathHelper;

public class RenderOverlayMusicDisplay implements IRenderable {
	
	private final IngameOverlayPosition overlayPosition;
	
	private final ITrackManager manager;
	
	private float x;
	private float y;
	
	private final int width;
	private final int height;
	
	private final ScrollingTextRenderer title;
	private final ScrollingTextRenderer author;
	
	private final ScalingTextRenderer position;
	private final ScalingTextRenderer duration;
	
	public RenderOverlayMusicDisplay(IngameOverlayPosition overlayPosition, float x, float y) {
		this.overlayPosition = overlayPosition;
		this.x = x;
		this.y = y;
		
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		height = 35;
		width = 120;
		
		final FontRenderer fontRender = Minecraft.getInstance().fontRenderer;
		
		title = new ScrollingTextRenderer(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedTitle()), x + 3, y + 2);
		title.setStepSize(0.5F);
		title.setColor(MusicPlayerColors.YELLOW);
		title.setWidth(114);
		title.setSpeedTime(35);
		
		author = new ScrollingTextRenderer(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(track -> track.getInfo().getFixedAuthor()), x + 3, y + 12);
		author.setStepSize(0.5F);
		author.setColor(MusicPlayerColors.YELLOW);
		author.setScale(0.75F);
		author.setWidth(114);
		author.setSpeedTime(35);
		
		position = new ScalingTextRenderer(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(GuiTrackUtils::getFormattedPosition), x + 6, y + 28);
		position.setColor(MusicPlayerColors.YELLOW);
		position.setScale(0.5F);
		
		duration = new ScalingTextRenderer(() -> fontRender, () -> GuiTrackUtils.getValueOfPlayingTrack(GuiTrackUtils::getFormattedDuration), x + width - 6, y + 28);
		duration.setTextChanged(renderer -> {
			duration.setX(x + width - 6 - renderer.getTextWidth());
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
		final int intX = MathHelper.ceil(x);
		final int intY = MathHelper.ceil(y);
		// Background
		AbstractGui.fill(matrixStack, intX, intY, intX + width, intY + height, 0xFF212121);
		
		// Progressbar
		final double progress;
		if (track.getInfo().isStream()) {
			progress = 0.5;
		} else {
			progress = (double) track.getPosition() / track.getDuration();
		}
		
		AbstractGui.fill(matrixStack, intX + 6, intY + 23, intX + width - 6, intY + 26, 0xFF555555);
		AbstractGui.fill(matrixStack, intX + 6, intY + 23, intX + 6 + (int) ((width - 12) * progress), intY + 26, 0xFF3e9100);
		
		// Draw strings
		title.render(matrixStack, mouseX, mouseY, partialTicks);
		author.render(matrixStack, mouseX, mouseY, partialTicks);
		
		position.render(matrixStack, mouseX, mouseY, partialTicks);
		duration.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public IngameOverlayPosition getOverlayPosition() {
		return overlayPosition;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
}
