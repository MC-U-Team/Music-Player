package info.u_team.music_player;

import static info.u_team.music_player.MusicPlayerConstants.*;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class MusicPlayerMod {
	
	@Instance
	private static MusicPlayerMod instance;
	
	public static MusicPlayerMod getInstance() {
		return instance;
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		if (event.getSide() == Side.SERVER) {
			FMLLog.bigWarning("This mod (modid: %s, name: %s) can only be used on client side! Please remove it on server side!", MODID, NAME);
			FMLCommonHandler.instance().exitJava(0, false);
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
	}
	
}
