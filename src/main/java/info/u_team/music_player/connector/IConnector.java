package info.u_team.music_player.connector;

import net.minecraftforge.fml.common.event.*;

public interface IConnector {
	
	public void preinit(FMLPreInitializationEvent event);
	
	public void init(FMLInitializationEvent event);
	
	public void postinit(FMLPostInitializationEvent event);
	
}
