package info.u_team.music_player.lavaplayer.api.audio;

public interface IAudioTrackInfo {
	
	String getTitle();
	
	String getAuthor();
	
	String getIdentifier();
	
	String getURI();
	
	boolean isStream();
	
	String getFixedTitle();
	
	String getFixedAuthor();
	
}
