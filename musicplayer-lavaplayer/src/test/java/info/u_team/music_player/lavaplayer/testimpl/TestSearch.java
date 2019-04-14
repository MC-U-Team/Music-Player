package info.u_team.music_player.lavaplayer.testimpl;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

import com.sedmelluq.discord.lavaplayer.tools.io.*;

import info.u_team.music_player.lavaplayer.MusicPlayer;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;

public class TestSearch {
	
	public static void main(String[] args) {
		IMusicPlayer musicplayer = new MusicPlayer();
		
		HttpInterfaceManager manager = new ThreadLocalHttpInterfaceManager(HttpClientTools.createSharedCookiesHttpBuilder().setRedirectStrategy(new HttpClientTools.NoRedirectsStrategy()), HttpClientTools.DEFAULT_REQUEST_CONFIG);
		
		try (PersistentHttpStream inputStream = new PersistentHttpStream(manager.getInterface(), new URI("http://stream06.iloveradio.de/iloveradio1.mp3"), Long.MAX_VALUE)) {
			int statusCode = inputStream.checkStatusCode();
			System.out.println(statusCode);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
}
