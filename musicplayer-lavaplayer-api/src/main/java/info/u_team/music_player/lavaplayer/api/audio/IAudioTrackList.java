package info.u_team.music_player.lavaplayer.api.audio;

import java.util.List;

public interface IAudioTrackList {
	
	String getName();
	
	List<IAudioTrack> getTracks();
	
	IAudioTrack getSelectedTrack();
	
	boolean isSearch();
	
	boolean hasUri();
	
	String getUri();
	
}
