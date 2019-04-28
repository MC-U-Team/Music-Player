package info.u_team.music_player.lavaplayer.testimpl;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;

public class TestSearch {
	
	public static void main(String[] args) {
		IMusicPlayer musicplayer = new MusicPlayer();
		musicplayer.getTrackSearch().getTracks("https://www.youtube.com/playlist?list=PLyseegEZ84-drkYTkLldkiIBPHRJd7Xgd", result -> {
			System.out.println(result.hasError());
			if (result.hasError())
				System.out.println(result.getErrorMessage());
			System.out.println(result.getTrack());
			System.out.println(result.getTrackList());
			result.getTrackList().getTracks().forEach(System.out::println);
		});
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
