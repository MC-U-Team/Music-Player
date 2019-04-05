package info.u_team.music_player.lavaplayer.api;

import java.util.List;
import java.util.function.Consumer;

public interface ITrackSearch {
	
	void getTracks(String uri, Consumer<List<IAudioTrack>> consumer);
	
}
