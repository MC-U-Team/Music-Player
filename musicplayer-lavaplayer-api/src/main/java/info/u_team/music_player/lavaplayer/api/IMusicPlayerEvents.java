package info.u_team.music_player.lavaplayer.api;

public interface IMusicPlayerEvents {
	
	public default void onPlay(IAudioTrack track) {
	}
	
	public default void onStop() {
	}
	
	public default void onSearchSuccess(State state) {
	}
	
	public default void onSearchTrack(State state, IAudioTrack track) {
		onSearchSuccess(state);
	}
	
	public default void onSearchTrackList(State state, IAudioTrackList tracklist) {
		onSearchSuccess(state);
	}
	
	public default void onSearchFailed(String error, Exception exeption) {
	}
	
	public enum State {
		PLAY,
		QUEUE;
	}
	
}
