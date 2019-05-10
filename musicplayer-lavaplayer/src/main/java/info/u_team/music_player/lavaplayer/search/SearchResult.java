package info.u_team.music_player.lavaplayer.search;

import info.u_team.music_player.lavaplayer.api.audio.*;
import info.u_team.music_player.lavaplayer.api.search.ISearchResult;

public class SearchResult implements ISearchResult {
	
	private final String uri;
	
	private final IAudioTrackList tracklist;
	private final IAudioTrack track;
	
	private final Exception exception;
	
	public SearchResult(String uri, Exception exception) {
		this(uri, null, null, exception);
	}
	
	public SearchResult(String uri, IAudioTrackList tracklist) {
		this(uri, tracklist, null, null);
	}
	
	public SearchResult(String uri, IAudioTrack track) {
		this(uri, null, track, null);
	}
	
	private SearchResult(String uri, IAudioTrackList tracklist, IAudioTrack track, Exception exception) {
		this.uri = uri;
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
	
	@Override
	public String getUri() {
		return uri;
	}
	
}
