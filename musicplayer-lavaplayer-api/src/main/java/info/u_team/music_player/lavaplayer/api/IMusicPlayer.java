package info.u_team.music_player.lavaplayer.api;

import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;

public interface IMusicPlayer {
	
	ITrackManager getTrackManager();
	
	ITrackSearch getTrackSearch();
	
	void startAudioOutput();
	
	int getVolume();
	
	void setVolume(int volume);
}
