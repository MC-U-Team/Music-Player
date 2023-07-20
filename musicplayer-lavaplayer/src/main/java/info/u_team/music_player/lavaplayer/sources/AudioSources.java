package info.u_team.music_player.lavaplayer.sources;

import java.util.function.Supplier;

import org.slf4j.LoggerFactory;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.getyarn.GetyarnAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class AudioSources {
	
	public static void registerSources(AudioPlayerManager audioPlayerManager) {
		registerManager(audioPlayerManager, () -> {
			final YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager(true, System.getProperty("musicplayer.lavaplayer.youtube.email"), System.getProperty("musicplayer.lavaplayer.youtube.password"));
			youtube.setPlaylistPageCount(100);
			return youtube;
		});
		registerManager(audioPlayerManager, SoundCloudAudioSourceManager::createDefault);
		registerManager(audioPlayerManager, BandcampAudioSourceManager::new);
		registerManager(audioPlayerManager, VimeoAudioSourceManager::new);
		registerManager(audioPlayerManager, TwitchStreamAudioSourceManager::new);
		registerManager(audioPlayerManager, GetyarnAudioSourceManager::new);
		registerManager(audioPlayerManager, HttpAudioSourceManager::new);
		registerManager(audioPlayerManager, LocalAudioSourceManager::new);
	}
	
	private static void registerManager(AudioPlayerManager audioPlayerManager, Supplier<AudioSourceManager> audioSourceManager) {
		try {
			audioPlayerManager.registerSourceManager(audioSourceManager.get());
		} catch (final Exception ex) {
			LoggerFactory.getLogger(AudioSources.class).warn("Cannot register audio source. Some music tracks might not be playable. Most often it is caused by an unstable internet connection or blocked services", ex);
		}
	}
}
