package info.u_team.music_player.lavaplayer.search;

import java.util.*;
import java.util.function.Consumer;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.lavaplayer.impl.AudioTrackImpl;

public class TrackSearch implements ITrackSearch {
	
	private final AudioPlayerManager audioplayermanager;
	
	public TrackSearch(AudioPlayerManager audioplayermanager) {
		this.audioplayermanager = audioplayermanager;
	}
	
	@Override
	public void getTracks(String uri, Consumer<List<IAudioTrack>> consumer) {
		audioplayermanager.loadItem(uri, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				ArrayList<IAudioTrack> list = new ArrayList<>();
				list.add(new AudioTrackImpl(track));
				consumer.accept(list);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				ArrayList<IAudioTrack> list = new ArrayList<>();
				playlist.getTracks().forEach(track -> list.add(new AudioTrackImpl(track)));
				consumer.accept(list);
			}
			
			@Override
			public void noMatches() {
				System.out.println("NO MATCHES");
				consumer.accept(null);
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				exception.printStackTrace();
				consumer.accept(null);
			}
		});
	}
}
