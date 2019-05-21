package info.u_team.music_player;

import java.io.File;
import java.nio.file.*;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.sun.jna.Platform;

import info.u_team.music_player.proxy.CommonProxy;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = MusicPlayerMod.modid, name = MusicPlayerMod.name, version = MusicPlayerMod.version, acceptedMinecraftVersions = MusicPlayerMod.mcversion, dependencies = MusicPlayerMod.dependencies, updateJSON = MusicPlayerMod.updateurl, clientSideOnly = true)
public class MusicPlayerMod {
	
	public static final String modid = "musicplayer";
	public static final String name = "Music Player";
	public static final String version = "${version}";
	public static final String mcversion = "${mcversion}";
	public static final String dependencies = "required:forge@[14.23.5.2768,);required-after:uteamcore@[2.2.4.94,);";
	public static final String updateurl = "https://api.u-team.info/update/musicplayer.json";
	
	@SidedProxy(serverSide = "info.u_team.music_player.proxy.CommonProxy", clientSide = "info.u_team.music_player.proxy.ClientProxy")
	private static CommonProxy proxy;
	
	private static String getFileDependingOnSystem() {
		String name = "lwjgl_tinyfd";
		if (Platform.isWindows()) {
			if (!Platform.is64Bit()) {
				name += "32";
			}
			name += ".dll";
		} else if (Platform.isLinux() || Platform.isMac()) {
			name = "lib" + name + (Platform.isMac() ? ".dylib" : ".so");
		} else {
			throw new UnsatisfiedLinkError("System no supported");
		}
		return name;
	}
	
	private static File extractFile(String resource) {
		try {
			final Path path = Files.createTempFile(resource, null);
			Files.copy(MusicPlayerMod.class.getResourceAsStream("/" + resource), path, StandardCopyOption.REPLACE_EXISTING);
			return path.toFile();
		} catch (Exception ex) {
			throw new LinkageError("Error occured when extracting file", ex);
		}
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		System.out.println(extractFile(getFileDependingOnSystem()).getAbsolutePath());
		proxy.preinit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		proxy.postinit(event);
	}
	
}