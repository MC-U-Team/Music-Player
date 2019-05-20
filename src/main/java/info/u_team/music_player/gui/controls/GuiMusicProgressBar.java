package info.u_team.music_player.gui.controls;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class GuiMusicProgressBar extends GuiProgressBar {
	
	private final RenderScalingText positionRender;
	private final RenderScalingText durationRender;
	
	public GuiMusicProgressBar(ITrackManager manager, int x, int y, int width, int height, float scale) {
		super(x, y, width, height, 0xFF555555, 0xFF3e9100, () -> getProgress(manager), (value) -> updateProgress(manager, value));
		final FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
		positionRender = new RenderScalingText(() -> fontRender, () -> GuiTrackUtils.getFormattedPosition(manager.getCurrentTrack()));
		positionRender.setScale(scale);
		positionRender.setColor(0xFFFF00);
		durationRender = new RenderScalingText(() -> fontRender, () -> GuiTrackUtils.getFormattedDuration(manager.getCurrentTrack()));
		durationRender.setScale(scale);
		durationRender.setColor(0xFFFF00);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
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
