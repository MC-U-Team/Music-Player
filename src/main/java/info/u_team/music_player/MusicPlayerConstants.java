package info.u_team.music_player;

import java.io.File;

import org.apache.logging.log4j.*;

import info.u_team.music_player.classloader.CustomURLClassLoader;
import info.u_team.music_player.plugin.dependecy.MavenEntry;

public class MusicPlayerConstants {
	
	public static final String MODID = "musicplayer";
	public static final String NAME = "Music Player";
	public static final String VERSION = "1.0.0";
	public static final String MCVERSION = "[1.8,1.12.2]";
	
	public static final MavenEntry[] MAVENENTRY = new MavenEntry[] { new MavenEntry("com.sedmelluq", "lavaplayer", "1.2.62") };
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	public static final File PATH = new File(new File(System.getProperty("user.dir")), "musicplayer");
	
	public static final CustomURLClassLoader CLASSLOADER = new CustomURLClassLoader();
	
	private MusicPlayerConstants() {
	}
	
}
