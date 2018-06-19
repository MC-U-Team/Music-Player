package info.u_team.music_player.config;

import java.io.File;

import net.minecraftforge.common.config.*;

public class Config {
	
	private static Configuration configuration;
	
	private static Property music_volume;
	private static Property overlay_display;
	
	public static void init(File file) {
		configuration = new Configuration(file);
		configuration.load();
		
		music_volume = configuration.get("Music", "Volume", 10, "Volume of musicplayer in %", 0, 100);
		overlay_display = configuration.get("Overlay", "Overlay Display", true, "Display current track as an overlay");
		
		configuration.save();
	}
	
	public static void setVolume(int volume) {
		music_volume.set(volume);
		configuration.save();
	}
	
	public static int getVolume() {
		return music_volume.getInt();
	}
	
	public static void setOverlayDisplay(boolean display) {
		overlay_display.set(display);
		configuration.save();
	}
	
	public static boolean getOverlayDisplay() {
		return overlay_display.getBoolean();
	}
	
}
