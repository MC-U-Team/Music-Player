package info.u_team.music_player.gui.playing;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.to_u_team_core.export.GuiProgressBar;

public class GuiMusicProgressBar extends GuiProgressBar {
	
	public GuiMusicProgressBar(ITrackManager manager, int x, int y, int width, int height) {
		super(x, y, width, height, 0xFF555555, 0xFF3e9100, () -> getProgress(manager), (value) -> updateProgress(manager, value));
	}
	
	private static double getProgress(ITrackManager manager) {
		IAudioTrack track = manager.getCurrentTrack();
		if (track == null) {
			return 0;
		}
		return (double) track.getPosition() / track.getDuration();
	}
	
	private static void updateProgress(ITrackManager manager, double value) {
		IAudioTrack track = manager.getCurrentTrack();
		if (track != null) {
			track.setPosition((long) (track.getDuration() * value));
		}
	}
	
}
