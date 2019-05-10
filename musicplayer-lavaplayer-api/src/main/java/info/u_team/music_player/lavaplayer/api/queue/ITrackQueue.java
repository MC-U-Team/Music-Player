package info.u_team.music_player.lavaplayer.api.queue;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;

public interface ITrackQueue {
	
	boolean hasNext();
	
	IAudioTrack getNext();
	
}
