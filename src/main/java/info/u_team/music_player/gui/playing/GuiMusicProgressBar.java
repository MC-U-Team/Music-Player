package info.u_team.music_player.gui.playing;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.util.TimeUtil;
import info.u_team.u_team_core.gui.elements.GuiProgressBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class GuiMusicProgressBar extends GuiProgressBar {
	
	private final ITrackManager manager;
	
	public GuiMusicProgressBar(ITrackManager manager, int x, int y, int width, int height) {
		super(x, y, width, height, 0xFF555555, 0xFF3e9100, () -> getProgress(manager), (value) -> updateProgress(manager, value));
		this.manager = manager;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		
		final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
		final IAudioTrack track = manager.getCurrentTrack();
		
		final boolean stream;
		final long position, duration;
		
		if (track == null) {
			stream = false;
			position = duration = 0;
		} else {
			stream = track.getInfo().isStream();
			position = track.getPosition() / 1000;
			duration = track.getDuration() / 1000;
		}
		
		final String positionString = TimeUtil.timeConversion(position);
		final String durationString;
		if (stream) {
			durationString = "undefined";
		} else {
			durationString = TimeUtil.timeConversion(duration);
		}
		
		fontRenderer.drawString(positionString, getX() - fontRenderer.getStringWidth(positionString) - 5, getY() - 2, 0xFFFF00);
		fontRenderer.drawString(durationString, getX() + getWidth() + 5, getY() - 2, 0xFFFF00);
	}
	
	private static double getProgress(ITrackManager manager) {
		final IAudioTrack track = manager.getCurrentTrack();
		if (track == null) {
			return 0;
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
