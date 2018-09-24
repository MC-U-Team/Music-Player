package info.u_team.music_player.lavaplayer.api;

public interface IAudioTrack {
	
	public IAudioTrackInfo getInfo();
	
	public long getPosition();
	
	public void setPosition(long position);
	
	public long getDuration();
	
}
