package info.u_team.music_player;

import java.io.*;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.util.Collections;

import com.sun.jna.Platform;

import info.u_team.music_player.dependency.DependencyManager;
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
		System.out.println(resource);
		try {
			final Path path = Files.createTempFile(resource, null);
			InputStream stream = MusicPlayerMod.class.getResourceAsStream("/" + resource);
//			if (stream == null) {
//				try (FileSystem fileSystem = FileSystems.newFileSystem(MusicPlayerMod.class.getResource("/dependencies").toURI(), Collections.<String, Object> emptyMap())) {
//					Files.walk(fileSystem.getPath("/dependencies/internal")).filter(file -> file.toString().startsWith("lwjgl-tinyfd") && file.toString().endsWith(".jar")).forEach(file -> {
//						final String url = "musicplayer:" + path.toString().substring(1);
//					});
//				}
//			}
			Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
			return path.toFile();
		} catch (Exception ex) {
			throw new LinkageError("Error occured when extracting file", ex);
		}
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		proxy.preinit(event);
		System.out.println(MusicPlayerMod.class.getResource("/mcmod.info"));
		System.out.println(MusicPlayerMod.class.getResource("/lwjgl_tinyfd.dll"));
		System.out.println(extractFile(getFileDependingOnSystem()).getAbsolutePath());
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