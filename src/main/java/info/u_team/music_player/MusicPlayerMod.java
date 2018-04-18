package info.u_team.music_player;

import static info.u_team.music_player.MusicPlayerConstants.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = MCVERSION)
public class MusicPlayerMod {
	
	@Instance
	private static MusicPlayerMod instance;
	
	public static MusicPlayerMod getInstance() {
		return instance;
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
