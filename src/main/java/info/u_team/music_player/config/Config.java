package info.u_team.music_player.config;

import java.io.File;

import net.minecraftforge.common.config.*;

public class Config {
	
	private Configuration configuration;
	
	private Property music_volume = configuration.get("Volume", "Music", 10, "Volume of musicplayer in %", 0, 100);
	
	public Config(File file) {
		configuration = new Configuration(file);
		configuration.load();
	}
	
	public void setVolume(int volume) {
		music_volume.set(volume);
		configuration.save();
	}
	
	public int getVolume() {
		configuration.load();
		return music_volume.getInt();
	}
	
}
