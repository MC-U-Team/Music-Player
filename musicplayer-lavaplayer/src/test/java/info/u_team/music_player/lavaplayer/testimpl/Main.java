package info.u_team.music_player.lavaplayer.testimpl;

import java.util.*;

import org.slf4j.*;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.*;

public class Main {
	
	private final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		IMusicPlayer musicPlayer = new MusicPlayer();
		musicPlayer.startAudioOutput();
		
		musicPlayer.setVolume(10);
		
		ITrackManager manager = musicPlayer.getTrackManager();
		
		musicPlayer.getTrackSearch().getTracks("https://www.youtube.com/playlist?list=PLyseegEZ84-drkYTkLldkiIBPHRJd7Xgd", result -> {
			
			manager.setTrackQueue(new TrackQueue(result.getTrackList().getTracks()));
			log.info("Loaded all tracks -> starting now");
			manager.start();
		});
		
		new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("pause")) {
					manager.setPaused(true);
				} else if (line.startsWith("unpause")) {
					manager.setPaused(false);
				} else if (line.startsWith("skip")) {
					manager.skip();
				} else if (line.startsWith("stop")) {
					manager.stop();
				} else if (line.startsWith("setending")) {
					IAudioTrack audiotrack = manager.getCurrentTrack();
					audiotrack.setPosition(audiotrack.getDuration() - 10000);
				}
			}
			scanner.close();
		}, "Music player manager").start();
		
	}
	
	private class TrackQueue implements ITrackQueue {
		
		private final List<IAudioTrack> tracks;
		
		public TrackQueue(List<IAudioTrack> tracks) {
			this.tracks = tracks;
		}
		
		@Override
		public boolean calculateNext() {
			return true;
		}
		
		@Override
		public IAudioTrack getNext() {
			return tracks.get(new Random().nextInt(tracks.size()));
		}
		
	}
	
}
