package info.u_team.music_player.proxy;

import static info.u_team.music_player.MusicPlayerConstants.*;

import net.minecraftforge.fml.common.event.*;

public class CommonProxy {
	
	public void preinit(FMLPreInitializationEvent event) {
		LOGGER.info("This mod (modid: %s, name: %s) can only be used on client side! It will be disabled on the server side!", MODID, NAME);
	}
	
	public void init(FMLInitializationEvent event) {
	}
	
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
