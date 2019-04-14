package info.u_team.music_player.lavaplayer.api;

import java.util.List;

public interface ISearchResult {
	
	List<IAudioTrack> getTracks();
	
	boolean hasError();
	
	String getErrorMessage();
	
	StackTraceElement[] getStackTrace();
	
}
