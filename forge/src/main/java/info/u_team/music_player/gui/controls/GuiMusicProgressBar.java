package info.u_team.music_player.gui.controls;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerColors;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.u_team_core.gui.elements.ProgressBar;
import info.u_team.u_team_core.gui.elements.ScalableText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class GuiMusicProgressBar extends ProgressBar {
	
	private final ScalableText positionRender;
	private final ScalableText durationRender;
	
	public GuiMusicProgressBar(ITrackManager manager, int x, int y, int width, int height, float scale) {
		super(x, y, width, height, MusicPlayerColors.GREY, MusicPlayerColors.GREEN, () -> getProgress(manager), (value) -> updateProgress(manager, value));
		final Font fontRender = Minecraft.getInstance().font;
		positionRender = new ScalableText(fontRender, () -> GuiTrackUtils.getFormattedPosition(manager.getCurrentTrack()), x, y);
		positionRender.setScale(scale);
		positionRender.setColor(MusicPlayerColors.YELLOW);
		positionRender.setTextChanged(renderer -> {
			renderer.setX(getX() - renderer.getTextWidth() - (renderer.getScale() < 1 ? 3 : 5));
			renderer.setY(getY() - (renderer.getScale() < 1 ? 1 : 2));
		});
		durationRender = new ScalableText(fontRender, () -> GuiTrackUtils.getFormattedDuration(manager.getCurrentTrack()), x, y);
		durationRender.setScale(scale);
		durationRender.setColor(MusicPlayerColors.YELLOW);
		durationRender.setTextChanged(renderer -> {
			renderer.setX(getX() + getWidth() + (renderer.getScale() < 1 ? 3 : 5));
			renderer.setY(getY() - (renderer.getScale() < 1 ? 1 : 2));
		});
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		
		positionRender.render(matrixStack, mouseX, mouseY, partialTicks);
		durationRender.render(matrixStack, mouseX, mouseY, partialTicks);
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
