package info.u_team.music_player.lavaplayer.sources;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.getyarn.GetyarnAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.nico.NicoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.yamusic.YandexMusicAudioSourceManager;

import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.YoutubeSourceOptions;
import net.hycrafthd.youtube_source_services.LocalService;
import net.hycrafthd.youtube_source_services.YoutubeSourceServices;

public class AudioSources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AudioSources.class);
	
	public static void registerSources(AudioPlayerManager audioPlayerManager) {
		registerManager(audioPlayerManager, () -> {
			final YoutubeSourceOptions options = new YoutubeSourceOptions();
			try {
				final YoutubeSourceServices services = YoutubeSourceServices.builder(Paths.get(System.getProperty("java.io.tmpdir", "/tmp"), "musicplayer-youtube-services")) //
						.log(line -> LOGGER.debug(line)) //
						.build();
				services.start().whenComplete((unused, error) -> {
					if (error != null) {
						LOGGER.warn("Cannot start the youtube services. Youtube tracks might not be playable", error);
					}
				});
				
				final LocalService cipher = services.cipher();
				final LocalService poToken = services.poToken();
				options.setRemoteCipher(cipher.getUrl(), cipher.getToken(), "musicplayer");
				options.setRemotePoToken(poToken.getUrl(), poToken.getToken());
			} catch (final IOException ex) {
				LOGGER.warn("Cannot create the youtube services. Youtube tracks might not be playable", ex);
			}
			final YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager(options, YoutubeAudioSourceManager.DEFAULT_CLIENTS);
			youtube.setPlaylistPageCount(100);
			return youtube;
		});
		registerManager(audioPlayerManager, SoundCloudAudioSourceManager::createDefault);
		registerManager(audioPlayerManager, () -> {
			final String clientId = System.getProperty("musicplayer.lavaplayer.spotify.clientId");
			final String clientSecret = System.getProperty("musicplayer.lavaplayer.spotify.clientSecret");
			if (clientId != null && clientSecret != null) {
				return new SpotifySourceManager(null, clientId, clientSecret, "", audioPlayerManager);
			}
			return null;
		});
		registerManager(audioPlayerManager, BandcampAudioSourceManager::new);
		registerManager(audioPlayerManager, VimeoAudioSourceManager::new);
		registerManager(audioPlayerManager, TwitchStreamAudioSourceManager::new);
		registerManager(audioPlayerManager, GetyarnAudioSourceManager::new);
		registerManager(audioPlayerManager, NicoAudioSourceManager::new);
		registerManager(audioPlayerManager, YandexMusicAudioSourceManager::new);
		registerManager(audioPlayerManager, HttpAudioSourceManager::new);
		registerManager(audioPlayerManager, LocalAudioSourceManager::new);
	}
	
	private static void registerManager(AudioPlayerManager audioPlayerManager, Supplier<AudioSourceManager> audioSourceManager) {
		try {
			final AudioSourceManager manager = audioSourceManager.get();
			if (manager != null) {
				LOGGER.info("Register {} source manager for music player", manager.getSourceName());
				audioPlayerManager.registerSourceManager(manager);
			}
		} catch (final Exception ex) {
			LOGGER.warn("Cannot register source manager. Some music tracks might not be playable. Most often it is caused by an unstable internet connection or blocked services", ex);
		}
	}
}
