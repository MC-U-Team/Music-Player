package info.u_team.music_player.lavaplayer.api;

public interface IAudioTrackInfo {
	
	public String getTitle();
	
	public String getAuthor();
	
	public String getIdentifier();
	
	public String getURI();
	
	public boolean isStream();
	
}
