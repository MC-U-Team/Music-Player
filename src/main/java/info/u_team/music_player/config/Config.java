package info.u_team.music_player.config;

import java.io.File;

import net.minecraftforge.common.config.*;

public class Config {
	
	private Configuration configuration;
	
	private Property music_volume;
	private Property overlay_display;
	
	public Config(File file) {
		configuration = new Configuration(file);
		configuration.load();
		
		music_volume = configuration.get("Music", "Volume", 10, "Volume of musicplayer in %", 0, 100);
		overlay_display = configuration.get("Overlay", "Overlay Display", true, "Display current track as an overlay");
	}
	
	public void setVolume(int volume) {
		music_volume.set(volume);
		configuration.save();
	}
	
	public int getVolume() {
		configuration.load();
		return music_volume.getInt();
	}
	
	public void setOverlayDisplay(boolean display) {
		overlay_display.set(display);
		configuration.save();
	}
	
	public boolean getOverlayDisplay() {
		configuration.load();
		return overlay_display.getBoolean();
	}
	
}
