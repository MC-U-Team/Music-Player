package info.u_team.music_player.lavaplayer.search;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.*;

public class SearchResult implements ISearchResult {
	
	private final List<IAudioTrack> list;
	
	private final Exception exception;
	
	public SearchResult(Exception exception) {
		this(new ArrayList<>(), exception);
	}
	
	public SearchResult(List<IAudioTrack> list) {
		this(list, null);
	}
	
	public SearchResult(List<IAudioTrack> list, Exception exception) {
		this.list = list;
		this.exception = exception;
	}
	
	@Override
	public List<IAudioTrack> getTracks() {
		return list;
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
