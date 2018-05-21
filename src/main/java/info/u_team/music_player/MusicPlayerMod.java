package info.u_team.music_player;

import static info.u_team.music_player.MusicPlayerConstants.*;

import info.u_team.music_player.config.Config;
import info.u_team.music_player.connector.IConnector;
import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.event.Events;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.*;

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
		if (FMLLaunchHandler.side() == Side.SERVER) {
			FMLLog.bigWarning("This mod (modid: %s, name: %s) can only be used on client side! Please remove it on server side!", MODID, NAME);
			FMLCommonHandler.instance().exitJava(0, false);
		}
		try {
			new DependencyManager().execute();
			Class<?> clazz = Class.forName("info.u_team.music_player.impl.MusicPlayerImpl", true, CLASSLOADER);
			if (!IConnector.class.isAssignableFrom(clazz)) {
				throw new RuntimeException("The class " + clazz + " does not implement IConnector! This should not happen?!");
			}
			connector = (IConnector) clazz.newInstance();
		} catch (Exception ex) {
			LOGGER.fatal("Fatal error while loading libraries! Please report to the mod authors!", ex);
			FMLCommonHandler.instance().exitJava(0, false);
		}
		config = new Config(event.getSuggestedConfigurationFile());
		
		Events events = new Events(connector);
		MinecraftForge.EVENT_BUS.register(events);
		FMLCommonHandler.instance().bus().register(events);
		
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
