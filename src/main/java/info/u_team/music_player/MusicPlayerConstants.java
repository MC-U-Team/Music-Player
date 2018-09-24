package info.u_team.music_player;

import org.apache.logging.log4j.*;

public class MusicPlayerConstants {
	
	public static final String MODID = "musicplayer";
	public static final String NAME = "Music Player";
	public static final String VERSION = "${version}";
	public static final String MCVERSION = "${mcversion}";
	public static final String DEPENDENCIES = "required:forge@[14.23.4.2705,);required-after:uteamcore@[2.0.0.81,);";
	public static final String UPDATEURL = "https://api.u-team.info/update/musicplayer.json";
	
	public static final String COMMONPROXY = "info.u_team.musicplayer.proxy.CommonProxy";
	public static final String CLIENTPROXY = "info.u_team.musicplayer.proxy.ClientProxy";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	private MusicPlayerConstants() {
	}
	
}