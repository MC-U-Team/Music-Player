package info.u_team.music_player.lavaplayer.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;
import info.u_team.music_player.lavaplayer.impl.AudioTrackImpl;

public class TrackQueueWrapper {
	
	private final ITrackQueue queue;
	
	public TrackQueueWrapper(ITrackQueue queue) {
		this.queue = queue;
	}
	
	public boolean hasNext() {
		return queue.hasNext();
	}
	
	public AudioTrack getNext() {
		return ((AudioTrackImpl) queue.getNext()).getImplTrack().makeClone(); // We know this cast must work because this interface is only implemented by AudioTrackImpl. Still not the best.
	}
	
}
