package info.u_team.music_player.lavaplayer.api;

public interface ISearchResult {

	boolean isList();

	IAudioTrackList getTrackList();

	IAudioTrack getTrack();

	boolean hasError();

	String getErrorMessage();

	StackTraceElement[] getStackTrace();

}
