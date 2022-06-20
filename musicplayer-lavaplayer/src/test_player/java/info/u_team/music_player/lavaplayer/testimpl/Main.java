package info.u_team.music_player.lavaplayer.testimpl;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.lavaplayer.api.queue.ITrackQueue;

public class Main {
	
	private final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		final IMusicPlayer musicPlayer = new MusicPlayer();
		musicPlayer.startAudioOutput();
		
		musicPlayer.setVolume(10);
		
		final ITrackManager manager = musicPlayer.getTrackManager();
		
		musicPlayer.getTrackSearch().getTracks("https://www.youtube.com/playlist?list=PLyseegEZ84-drkYTkLldkiIBPHRJd7Xgd", result -> {
			if (result.isList()) {
				manager.setTrackQueue(new TrackQueue(result.getTrackList().getTracks()));
			} else {
				manager.setTrackQueue(new TrackQueue(Arrays.asList(result.getTrack())));
			}
			log.info("Loaded all tracks -> starting now");
			manager.start();
		});
		
		new Thread(() -> {
			final Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()) {
				final String line = scanner.nextLine();
				if (line.startsWith("pause")) {
					manager.setPaused(true);
				} else if (line.startsWith("unpause")) {
					manager.setPaused(false);
				} else if (line.startsWith("skip")) {
					manager.skip();
				} else if (line.startsWith("stop")) {
					manager.stop();
				} else if (line.startsWith("setending")) {
					final IAudioTrack audioTrack = manager.getCurrentTrack();
					audioTrack.setPosition(audioTrack.getDuration() - 10000);
				} else if (line.startsWith("playing")) {
					final IAudioTrack audioTrack = manager.getCurrentTrack();
					System.out.println(audioTrack.getInfo().getAuthor() + " - " + audioTrack.getInfo().getTitle());
				}
			}
			scanner.close();
		}, "Music player manager").start();
		
	}
	
	private class TrackQueue implements ITrackQueue {
		
		private final Random random = new Random();
		
		private final List<IAudioTrack> tracks;
		
		public TrackQueue(List<IAudioTrack> tracks) {
			System.out.println("Init track queue with " + tracks.size() + " tracks.");
			this.tracks = tracks;
		}
		
		@Override
		public boolean calculateNext() {
			return true;
		}
		
		@Override
		public IAudioTrack getNext() {
			return tracks.get(random.nextInt(tracks.size()));
		}
		
	}
	
}
