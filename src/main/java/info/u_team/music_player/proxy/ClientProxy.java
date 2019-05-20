package info.u_team.music_player.proxy;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.harawata.appdirs.AppDirsFactory;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preinit(FMLPreInitializationEvent event) {
		super.preinit(event);
		System.setProperty("http.agent", "Chrome");
		
		DependencyManager.construct();
		
		System.out.println("___________________________________________________________________");
		generatePlayer();
		String appdirs = AppDirsFactory.getInstance().getSiteConfigDir("test", "1.2", "mc-u-team");
		System.out.println(appdirs);
		System.out.println("___________________________________________________________________");
	}
	
	private static void generatePlayer() {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, DependencyManager.getClassLoader());
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			IMusicPlayer player = (IMusicPlayer) clazz.newInstance();
			System.out.println("Successfully created music player instance");
		} catch (Exception ex) {
			System.out.println("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors");
			ex.printStackTrace();
			System.exit(0);
		}
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
	}
}
