package info.u_team.music_player.lavaplayer.testimpl;

import java.util.Scanner;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.*;

public class Main implements IMusicPlayerEvents {
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		IMusicPlayer musicplayer = new MusicPlayer();
		musicplayer.startAudioOutput();
		
		new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("pause")) {
					musicplayer.getTrackScheduler().setPaused(true);
				} else if (line.startsWith("unpause")) {
					musicplayer.getTrackScheduler().setPaused(false);
//				} else if (line.startsWith("playsearch ")) {
//					musicplayer.getTrackSearchOLD().play(line.substring(5));
//				} else if (line.startsWith("queuesearch ")) {
//					musicplayer.getTrackSearchOLD().queue(line.substring(6));
//				} else if (line.startsWith("play ")) {
//					musicplayer.getTrackDirectSearch().play(line.substring(5));
//				} else if (line.startsWith("queue ")) {
//					musicplayer.getTrackDirectSearch().queue(line.substring(6));
				} else if (line.startsWith("repeat")) {
					musicplayer.getTrackScheduler().setRepeat(!musicplayer.getTrackScheduler().isRepeat());
				} else if (line.startsWith("shuffle")) {
					musicplayer.getTrackScheduler().setShuffle(!musicplayer.getTrackScheduler().isShuffle());
				} else if (line.startsWith("volume ")) {
					int volume = -1;
					try {
						volume = Integer.valueOf(line.substring(7));
					} catch (Exception ex) {
					}
					if (volume != -1) {
						musicplayer.setVolume(volume);
						System.out.println("Volume: " + volume);
					} else {
						System.out.println("Wrong number");
					}
				} else if (line.startsWith("queue")) {
					System.out.println("Now playing: " + getTrackInfo(musicplayer.getTrackScheduler().getCurrentTrack().getInfo()));
					System.out.println("Queue:");
					for (IAudioTrack tracks : musicplayer.getTrackScheduler().getQueue()) {
						System.out.println(getTrackInfo(tracks.getInfo()));
					}
				} else if (line.startsWith("skip")) {
					musicplayer.getTrackScheduler().skip();
				} else if (line.startsWith("mix")) {
					musicplayer.getTrackScheduler().mix();
				}
			}
			scanner.close();
		}, "Music player manager").start();
		
		musicplayer.registerEventHandler(this);
		
	}
	
	@Override
	public void onStop() {
		System.out.println("Player stopped");
	}
	
	@Override
	public void onPlay(IAudioTrack track) {
		System.out.println("Playing now: " + getTrackInfo(track.getInfo()));
	}
	
	@Override
	public void onSearchFailed(String error, Exception exeption) {
		System.out.println(error);
		exeption.printStackTrace();
	}
	
	@Override
	public void onSearchTrack(State state, IAudioTrack track) {
		System.out.println("Queued Track: " + getTrackInfo(track.getInfo()) + " with state " + state);
	}
	
	@Override
	public void onSearchTrackList(State state, IAudioTrackList tracklist) {
		System.out.println("Queued Playlist: " + tracklist.getName() + " with state " + state);
	}
	
	protected String getTrackInfo(IAudioTrackInfo info) {
		if (info.getAuthor().equals("Unknown artist") && info.getTitle().equals("Unknown title")) {
			return info.getURI();
		}
		return info.getAuthor() + " - " + info.getTitle();
	}
	
}
