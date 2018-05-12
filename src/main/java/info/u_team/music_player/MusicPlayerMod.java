package info.u_team.music_player;

import static info.u_team.music_player.MusicPlayerConstants.*;

import info.u_team.music_player.config.Config;
import info.u_team.music_player.connector.IConnector;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = MCVERSION)
public class MusicPlayerMod {
	
	@Instance
	private static MusicPlayerMod instance;
	
	public static MusicPlayerMod getInstance() {
		return instance;
	}
	
	private IConnector connector;
	
	public Config config;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.impl.MusicPlayerImpl", true, CLASSLOADER);
			if (!IConnector.class.isAssignableFrom(clazz)) {
				throw new RuntimeException("The class " + clazz + " does not implement IConnector! This should not happen?!");
			}
			connector = (IConnector) clazz.newInstance();
		} catch (Exception ex) {
			LOGGER.fatal("Fatal error while trying to get MusicPlayerImpl via IConnector! Please report to the mod authors!", ex);
			FMLCommonHandler.instance().exitJava(0, false);
		}
		config = new Config(event.getSuggestedConfigurationFile());
		connector.preinit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		connector.init(event);
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		connector.postinit(event);
	}
	
}
