package info.u_team.music_player.dependency;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.*;

import org.apache.commons.io.FileUtils;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.classloader.CustomURLClassLoader;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DependencyManager {
	
	private static CustomURLClassLoader classloader = new CustomURLClassLoader();
	
	private static URL player;
	
	public static void init() {
		findPlayer();
		loadClasses();
		loadDependencies();
	}
	
	private static void findPlayer() {
		if (((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))) {
			try {
				File backpath = new File("").getAbsoluteFile().getParentFile();
				File playerfile = new File(backpath, "musicplayer-lavaplayer/build/libs/musicplayer-lavaplayer.jar");
				if (!playerfile.exists()) {
					throw new FileNotFoundException("The playfile " + playerfile + " could not be found.");
				}
				player = playerfile.toURI().toURL();
			} catch (Exception ex) {
				MusicPlayerConstants.LOGGER.error("This is in dev environment. Could not find musicplayer and musicplayer-api. Did you forgot to build the project first?", ex);
			}
		} else {
			player = DependencyManager.class.getResource("/dependencies/musicplayer-lavaplayer.jar");
		}
	}
	
	private static void loadClasses() {
		classloader.addURL(player);
	}
	
	private static void loadDependencies() {
		try {
			ArrayList<String> dependencylist = new ArrayList<>();
			ZipInputStream stream = new ZipInputStream(player.openStream());
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null) {
				if (!entry.isDirectory() && entry.getName().startsWith("dependencies/")) {
					dependencylist.add(entry.getName());
				}
			}
			stream.close();
			
			File cache = new File("musicplayer/dep_cache");
			cache.mkdirs();
			FileUtils.cleanDirectory(cache);
			
			dependencylist.forEach(name -> {
				URL url = classloader.getResource(name);
				File file = new File(cache, name);
				try {
					FileUtils.copyURLToFile(url, file);
					classloader.addFile(file);
				} catch (IOException ex) {
					MusicPlayerConstants.LOGGER.error("Cannot copy and load dependency file from cache directory " + file, ex);
				}
			});
		} catch (Exception ex) {
			MusicPlayerConstants.LOGGER.error("Cannot load dependencies.", ex);
		}
	}
	
	public static IMusicPlayer getMusicPlayer() {
		try {
			Class<?> clazz = Class.forName("info.u_team.music_player.lavaplayer.MusicPlayer", true, classloader);
			if (!IMusicPlayer.class.isAssignableFrom(clazz)) {
				throw new IllegalAccessError("The class " + clazz + " does not implement IMusicPlayer! This should not happen?!");
			}
			IMusicPlayer player = (IMusicPlayer) clazz.newInstance();
			MusicPlayerConstants.LOGGER.info("Successfully created music player instance.");
			return player;
		} catch (Exception ex) {
			MusicPlayerConstants.LOGGER.fatal("Cannot create music player instance. This is a serious bug and the mod will not work. Report to the mod authors.", ex);
			FMLCommonHandler.instance().exitJava(0, true);
			return null;
		}
	}
}
