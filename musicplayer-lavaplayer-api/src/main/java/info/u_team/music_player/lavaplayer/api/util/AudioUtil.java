package info.u_team.music_player.lavaplayer.api.util;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

public class AudioUtil {
	
	public static List<String> findAudioDevices(Line.Info lineInfo) {
		final List<String> list = new ArrayList<>();
		for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(lineInfo)) {
				list.add(mixerInfo.getName());
			}
		}
		return list;
	}
}
