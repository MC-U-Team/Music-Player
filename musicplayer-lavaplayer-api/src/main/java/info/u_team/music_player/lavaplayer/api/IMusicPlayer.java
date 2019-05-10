package info.u_team.music_player.lavaplayer.api;

import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;

public interface IMusicPlayer {
	
	void setTrackQueue(ITrackQueue queue);
	
	ITrackSearch getTrackSearch();
	
	void startAudioOutput();
	
	int getVolume();
	
	void setVolume(int volume);
}
