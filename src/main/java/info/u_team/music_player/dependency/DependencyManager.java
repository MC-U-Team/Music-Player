package info.u_team.music_player.dependency;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.classloader.CustomURLClassLoader;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DependencyManager {
	
	private static CustomURLClassLoader classloader = new CustomURLClassLoader();
	
	public static void init() {
		
	}
	
	public static IMusicPlayer getMusicPlayer() {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, classloader);
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			return (IMusicPlayer) clazz.newInstance();
		} catch (Exception ex) {
			MusicPlayerConstants.LOGGER.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors.", ex);
			FMLCommonHandler.instance().exitJava(0, true);
			return null;
		}
	}
}
