package info.u_team.music_player.lavaplayer.api.audio;

public interface IAudioTrack {
	
	IAudioTrackInfo getInfo();
	
	long getPosition();
	
	void setPosition(long position);
	
	long getDuration();
	
}
