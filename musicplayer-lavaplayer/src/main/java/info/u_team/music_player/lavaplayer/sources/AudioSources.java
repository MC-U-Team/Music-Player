package info.u_team.music_player.lavaplayer.sources;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

public class AudioSources {
	
	public static void registerSources(AudioPlayerManager audioplayermanager) {
		YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager(true);
		youtube.setPlaylistPageCount(20);
		audioplayermanager.registerSourceManager(youtube);
		audioplayermanager.registerSourceManager(new SoundCloudAudioSourceManager());
		audioplayermanager.registerSourceManager(new BandcampAudioSourceManager());
		audioplayermanager.registerSourceManager(new VimeoAudioSourceManager());
		audioplayermanager.registerSourceManager(new TwitchStreamAudioSourceManager());
		audioplayermanager.registerSourceManager(new BeamAudioSourceManager());
		audioplayermanager.registerSourceManager(new VimeoAudioSourceManager());
		audioplayermanager.registerSourceManager(new HttpAudioSourceManager());
		audioplayermanager.registerSourceManager(new LocalAudioSourceManager());
	}
}
