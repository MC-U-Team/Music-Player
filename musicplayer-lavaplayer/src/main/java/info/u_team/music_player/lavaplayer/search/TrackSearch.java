package info.u_team.music_player.lavaplayer.search;

import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.api.search.ISearchResult;
import info.u_team.music_player.lavaplayer.api.search.ITrackSearch;
import info.u_team.music_player.lavaplayer.impl.AudioTrackImpl;
import info.u_team.music_player.lavaplayer.impl.AudioTrackListImpl;

public class TrackSearch implements ITrackSearch {
	
	private final AudioPlayerManager audioPlayerManager;
	
	public TrackSearch(AudioPlayerManager audioplayermanager) {
		audioPlayerManager = audioplayermanager;
	}
	
	@Override
	public void getTracks(String uri, Consumer<ISearchResult> consumer) {
		audioPlayerManager.loadItem(uri, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				consumer.accept(new SearchResult(uri, new AudioTrackImpl(track)));
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				consumer.accept(new SearchResult(uri, new AudioTrackListImpl(uri, playlist)));
			}
			
			@Override
			public void noMatches() {
				consumer.accept(new SearchResult(uri, new RuntimeException("No matches found")));
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				consumer.accept(new SearchResult(uri, exception));
			}
		});
	}
}
