package info.u_team.music_player.connector;

import info.u_team.music_player.event.IEvents;
import net.minecraftforge.fml.common.event.*;

public interface IConnector extends IEvents {
	
	public void preinit(FMLPreInitializationEvent event);
	
	public void init(FMLInitializationEvent event);
	
	public void postinit(FMLPostInitializationEvent event);
	
}
