package info.u_team.music_player.lavaplayer.api;

import info.u_team.music_player.lavaplayer.api.output.IOutputConsumer;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;

public interface IMusicPlayer {
	
	ITrackManager getTrackManager();
	
	ITrackSearch getTrackSearch();
	
	void startAudioOutput();
	
	void setMixer(String name);
	
	int getVolume();
	
	void setVolume(int volume);
	
	void setOutputConsumer(IOutputConsumer consumer);
}
