package info.u_team.music_player;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import info.u_team.music_player.init.MusicPlayerClientConstruct;
import net.fabricmc.api.ClientModInitializer;

public class MusicPlayerMod implements ClientModInitializer {
	
	public static final String MODID = "musicplayer";
	public static Logger LOGGER = LogUtils.getLogger();
	
	@Override
	public void onInitializeClient() {
		MusicPlayerClientConstruct.construct();
	}
}
