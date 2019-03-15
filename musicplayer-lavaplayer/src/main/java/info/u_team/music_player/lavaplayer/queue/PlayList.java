package info.u_team.music_player.lavaplayer.queue;

import java.util.*;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlayList {
	
	private final LinkedList<AudioTrack> tracks;
	
	public PlayList() {
		tracks = new LinkedList<>();
	}
	
	public synchronized void offerFirst(AudioTrack track) {
		tracks.offerFirst(track);
	}
	
	public synchronized void offerLast(AudioTrack track) {
		tracks.offerLast(track);
	}
	
	public synchronized AudioTrack pollFirst() {
		return tracks.pollFirst();
	}
	
	public synchronized AudioTrack pollLast() {
		return tracks.pollLast();
	}
	
	public synchronized AudioTrack pollRandom() {
		if (tracks.isEmpty()) {
			return null;
		}
		int random = new Random().nextInt(tracks.size());
		return tracks.remove(random);
	}
	
	public synchronized void clear() {
		tracks.clear();
	}
	
	public synchronized void mix() {
		Collections.shuffle(tracks);
	}
	
	public List<AudioTrack> getTracks() {
		return Collections.unmodifiableList(tracks);
	}
	
}
