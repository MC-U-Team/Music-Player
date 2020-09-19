package info.u_team.music_player.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.u_team_core.gui.elements.ProgressBar;
import info.u_team.u_team_core.gui.render.ScalingTextRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class GuiMusicProgressBar extends ProgressBar {
	
	private final ScalingTextRender positionRender;
	private final ScalingTextRender durationRender;
	
	public GuiMusicProgressBar(ITrackManager manager, int x, int y, int width, int height, float scale) {
		super(x, y, width, height, 0xFF555555, 0xFF3e9100, () -> getProgress(manager), (value) -> updateProgress(manager, value));
		final FontRenderer fontRender = Minecraft.getInstance().fontRenderer;
		positionRender = new ScalingTextRender(() -> fontRender, () -> GuiTrackUtils.getFormattedPosition(manager.getCurrentTrack()));
		positionRender.setScale(scale);
		positionRender.setColor(0xFFFF00);
		durationRender = new ScalingTextRender(() -> fontRender, () -> GuiTrackUtils.getFormattedDuration(manager.getCurrentTrack()));
		durationRender.setScale(scale);
		durationRender.setColor(0xFFFF00);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		
		positionRender.draw(getX() - positionRender.getTextWidth() - (positionRender.getScale() < 1 ? 3 : 5), getY() - (positionRender.getScale() < 1 ? 1 : 2));
		durationRender.draw(getX() + getWidth() + (positionRender.getScale() < 1 ? 3 : 5), getY() - (positionRender.getScale() < 1 ? 1 : 2));
	}
	
	private static double getProgress(ITrackManager manager) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track == null) {
			return 0;
		}
		if (track.getInfo().isStream()) {
			return 0.5;
		}
		return (double) track.getPosition() / track.getDuration();
	}
	
	private static void updateProgress(ITrackManager manager, double value) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track != null) {
			track.setPosition((long) (track.getDuration() * value));
		}
	}
}
