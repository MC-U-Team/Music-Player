package info.u_team.music_player.dependency;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.logging.log4j.*;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.dependency.classloader.DependencyClassLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

public class DependencyManager {
	
	private static final Logger logger = LogManager.getLogger();
	private static final Marker load = MarkerManager.getMarker("Load");
	
	private static final DependencyClassLoader musicplayerclassloader = new DependencyClassLoader();
	
	public static void construct() {
		logger.info(load, "Load dependencies");
		
		final String devPath = System.getProperty("musicplayer.dev");
		if (devPath != null) {
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/libs"), DependencyManager::addToMusicPlayerDependencies);
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/dependencies"), DependencyManager::addToMusicPlayerDependencies);
		} else {
			findJarFilesInJar("dependencies/internal", path -> addToInternalDependencies(createInternalURL(path)));
			findJarFilesInJar("dependencies/musicplayer", path -> addToMusicPlayerDependencies(createInternalURL(path)));
		}
		
		logger.info(load, "Finished loading dependencies");
	}
	
	public static DependencyClassLoader getClassLoader() {
		return musicplayerclassloader;
	}
	
	private static void findJarFilesInDev(Path path, Consumer<Path> consumer) {
		try (Stream<Path> stream = Files.walk(path)) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (IOException ex) {
			logger.error(load, "When searching for jar files in dev an exception occured.", ex);
		}
	}
	
	private static void findJarFilesInJar(String folder, Consumer<Path> consumer) {
		final ModFile modfile = ModList.get().getModFileById(MusicPlayerMod.MODID).getFile();
		try (Stream<Path> stream = Files.walk(modfile.findResource("/" + folder))) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (IOException ex) {
			logger.error(load, "When searching for jar files in jar an exception occured.", ex);
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "modjar://" + MusicPlayerMod.MODID + path;
		logger.debug(load, "Load url" + url);
		try {
			return new URL(url);
		} catch (MalformedURLException ex) {
			logger.error(load, "Could not create url from internal path", ex);
		}
		return null;
	}
	
	// Add to different classloader
	
	private static void addToMusicPlayerDependencies(URL url) {
		musicplayerclassloader.addURL(url);
	}
	
	private static void addToMusicPlayerDependencies(Path path) {
		musicplayerclassloader.addPath(path);
	}
	
	private static void addToInternalDependencies(URL url) {
		try {
			final URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(systemClassLoader, url);
		} catch (Exception ex) {
			logger.error(load, "Method addURL on system classloader could not be invoked", ex);
		}
	}
}
