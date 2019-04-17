package info.u_team.music_player.lavaplayer.search;

import info.u_team.music_player.lavaplayer.api.*;

public class SearchResult implements ISearchResult {

	private final IAudioTrackList tracklist;
	private final IAudioTrack track;

	private final Exception exception;

	public SearchResult(Exception exception) {
		this(null, null, exception);
	}

	public SearchResult(IAudioTrackList tracklist) {
		this(tracklist, null, null);
	}

	public SearchResult(IAudioTrack track) {
		this(null, track, null);
	}

	private SearchResult(IAudioTrackList tracklist, IAudioTrack track, Exception exception) {
		this.tracklist = tracklist;
		this.track = track;
		this.exception = exception;
	}

	@Override
	public boolean isList() {
		return tracklist != null;
	}

	@Override
	public IAudioTrackList getTrackList() {
		return tracklist;
	}

	@Override
	public IAudioTrack getTrack() {
		return track;
	}

	@Override
	public boolean hasError() {
		return exception != null;
	}

	@Override
	public String getErrorMessage() {
		return exception.getMessage();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return exception.getStackTrace();
	}

}
