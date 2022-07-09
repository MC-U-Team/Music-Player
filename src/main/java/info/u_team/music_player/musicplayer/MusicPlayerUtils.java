package info.u_team.music_player.musicplayer;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.playlist.*;

public final class MusicPlayerUtils {
	
	public static void skipForward() {
		final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
		final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
		if (playlist != null) {
			if (playlist.skip(Skip.FORWARD)) {
				manager.skip();
			}
		}
	}
	
	public static void skipBack() {
		final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
		final Playlist playlist = MusicPlayerManager.getPlaylistManager().getPlaylists().getPlaying();
		if (playlist != null) {
			final IAudioTrack currentlyPlaying = manager.getCurrentTrack();
			
			if (currentlyPlaying == null) {
				return;
			}
			
			long maxDuration = currentlyPlaying.getDuration() / 10;
			if (maxDuration > 10000) {
				maxDuration = 10000;
			}
			if (currentlyPlaying.getPosition() > maxDuration && !currentlyPlaying.getInfo().isStream()) {
				currentlyPlaying.setPosition(0);
			} else {
				
				if (playlist.skip(Skip.PREVIOUS)) {
					manager.skip();
				}
			}
		}
	}
}
