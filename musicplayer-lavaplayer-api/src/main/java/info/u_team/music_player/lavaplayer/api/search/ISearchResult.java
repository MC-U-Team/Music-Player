package info.u_team.music_player.lavaplayer.api.search;

import info.u_team.music_player.lavaplayer.api.audio.*;

public interface ISearchResult {
	
	String getUri();
	
	boolean isList();
	
	IAudioTrackList getTrackList();
	
	IAudioTrack getTrack();
	
	boolean hasError();
	
	String getErrorMessage();
	
	StackTraceElement[] getStackTrace();
	
}
