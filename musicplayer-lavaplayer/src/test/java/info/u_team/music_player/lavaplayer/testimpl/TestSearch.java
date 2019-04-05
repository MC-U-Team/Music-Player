package info.u_team.music_player.lavaplayer.testimpl;

import java.util.Scanner;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;

public class TestSearch {
	
	public static void main(String[] args) {
		IMusicPlayer musicplayer = new MusicPlayer();
		// musicplayer.startAudioOutput();
		
		musicplayer.getTrackSearch().getTracks("http://www.antenne.de/webradio/antenne.m3u", list -> {
			if (list == null) {
				System.out.println("ERROR");
				return;
			}
			list.forEach(track -> {
				System.out.println(track.getInfo().getTitle());
			});
		});
		
		new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				System.out.println("LINE: " + line);
//				musicplayer.getTrackSearch().getTracks(line, (tracks) -> {
//					System.out.println("________");
//					if (tracks != null)
//						tracks.forEach(track -> System.out.println(track.getInfo().getURI()));
//					else
//						System.out.println("ERROR");
//					
//				});
			}
			scanner.close();
		}).start();
	}
	
}
