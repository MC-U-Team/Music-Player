package info.u_team.music_player.lavaplayer.api;

import java.util.List;

public interface ITrackScheduler {
	
	public void stop();
	
	public void skip();
	
	public void shuffle();
	
	public void mix();
	
	public void setRepeat(boolean repeat);
	
	public void setShuffle(boolean shuffle);
	
	public void setPaused(boolean pause);
	
	public boolean isRepeat();
	
	public boolean isShuffle();
	
	public boolean isPaused();
	
	public IAudioTrack getCurrentTrack();
	
	public List<IAudioTrack> getQueue();
	
}
