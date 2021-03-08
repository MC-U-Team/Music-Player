package info.u_team.music_player.lavaplayer.api.util;

import java.util.*;

import javax.sound.sampled.*;

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
