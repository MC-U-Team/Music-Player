package info.u_team.music_player.lavaplayer.search;

import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.lavaplayer.impl.*;

public class TrackSearch implements ITrackSearch {

	private final AudioPlayerManager audioplayermanager;

	public TrackSearch(AudioPlayerManager audioplayermanager) {
		this.audioplayermanager = audioplayermanager;
	}

	@Override
	public void getTracks(String uri, Consumer<ISearchResult> consumer) {
		audioplayermanager.loadItem(uri, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack track) {
				consumer.accept(new SearchResult(new AudioTrackImpl(track)));
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				consumer.accept(new SearchResult(new AudioTrackListImpl(uri, playlist)));
			}

			@Override
			public void noMatches() {
				consumer.accept(new SearchResult(new RuntimeException("No matches found")));
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				consumer.accept(new SearchResult(exception));
			}
		});
	}
}
