package info.u_team.music_player.lavaplayer.api;

import java.util.List;

import info.u_team.music_player.lavaplayer.api.output.IOutputConsumer;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;

public interface IMusicPlayer {
	
	ITrackManager getTrackManager();
	
	ITrackSearch getTrackSearch();
	
	void startAudioOutput();
	
	List<String> audioDevices();
	
	void setAudioDevice(String name);
	
	String getAudioDevice();
	
	int getVolume();
	
	void setVolume(int volume);
	
	float getSpeed();
	
	void setSpeed(float speed);
	
	float getPitch();
	
	void setPitch(float pitch);
	
	void setOutputConsumer(IOutputConsumer consumer);
}
