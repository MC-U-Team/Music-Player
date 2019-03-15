package info.u_team.music_player.lavaplayer.search;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import info.u_team.music_player.lavaplayer.queue.TrackScheduler;

public class TrackDirectSearch extends TrackSearch {
	
	public TrackDirectSearch(AudioPlayerManager audioplayermanager, TrackScheduler trackscheduler) {
		super(audioplayermanager, trackscheduler);
	}
	
	@Override
	public void play(String identifier, boolean force) {
		audioplayermanager.loadItemOrdered(this, identifier, new AudioLoadResultHandlerDirectSearch(identifier, force));
	}
	
	protected class AudioLoadResultHandlerDirectSearch extends AudioLoadResultHandlerSearch {
		
		public AudioLoadResultHandlerDirectSearch(String identifier, boolean force) {
			super(identifier, force, true);
		}
		
	}
}
