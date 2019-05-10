package info.u_team.music_player.lavaplayer.api.search;

import java.util.function.Consumer;

public interface ITrackSearch {
	
	void getTracks(String uri, Consumer<ISearchResult> consumer);
	
}
