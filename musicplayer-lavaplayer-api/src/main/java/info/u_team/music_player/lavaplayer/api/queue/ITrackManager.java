package info.u_team.music_player.lavaplayer.api.queue;

import info.u_team.music_player.lavaplayer.api.audio.IPlayingTrack;

public interface ITrackManager {
	
	void start();
	
	void stop();
	
	void setTrackQueue(ITrackQueue queue);
	
	void skip();
	
	void setPaused(boolean value);
	
	boolean isPaused();
	
	IPlayingTrack getCurrentTrack();
}
