package info.u_team.music_player.dependency;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.logging.log4j.*;

import cpw.mods.modlauncher.TransformingClassLoader;
import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.dependency.classloader.DependencyClassLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

public class DependencyManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker MARKER = MarkerManager.getMarker("Load");
	
	private static final DependencyClassLoader MUSICPLAYER_CLASSLOADER = new DependencyClassLoader();
	
	public static void construct() {
		LOGGER.info(MARKER, "Load dependencies");
		
		final String devPath = System.getProperty("musicplayer.dev");
		if (devPath != null) {
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/libs"), DependencyManager::addToMusicPlayerDependencies);
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/dependencies"), DependencyManager::addToMusicPlayerDependencies);
		} else {
			findJarFilesInJar("dependencies/internal", path -> addToInternalDependencies(createInternalURL(path)));
			findJarFilesInJar("dependencies/musicplayer", path -> addToMusicPlayerDependencies(createInternalURL(path)));
		}
		
		LOGGER.info(MARKER, "Finished loading dependencies");
	}
	
	public static DependencyClassLoader getClassLoader() {
		return MUSICPLAYER_CLASSLOADER;
	}
	
	private static void findJarFilesInDev(Path path, Consumer<Path> consumer) {
		try (Stream<Path> stream = Files.walk(path)) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (IOException ex) {
			LOGGER.error(MARKER, "When searching for jar files in dev an exception occured.", ex);
		}
	}
	
	private static void findJarFilesInJar(String folder, Consumer<Path> consumer) {
		final ModFile modfile = ModList.get().getModFileById(MusicPlayerMod.MODID).getFile();
		try (Stream<Path> stream = Files.walk(modfile.findResource("/" + folder))) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (IOException ex) {
			LOGGER.error(MARKER, "When searching for jar files in jar an exception occured.", ex);
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "modjar://" + MusicPlayerMod.MODID + path;
		LOGGER.debug(MARKER, "Load url" + url);
		try {
			return new URL(url);
		} catch (MalformedURLException ex) {
			LOGGER.error(MARKER, "Could not create url from internal path", ex);
		}
		return null;
	}
	
	// Add to different classloader
	
	private static void addToMusicPlayerDependencies(URL url) {
		MUSICPLAYER_CLASSLOADER.addURL(url);
	}
	
	private static void addToMusicPlayerDependencies(Path path) {
		MUSICPLAYER_CLASSLOADER.addPath(path);
	}
	
	private static void addToInternalDependencies(URL url) {
		try {
			final Field field = TransformingClassLoader.class.getDeclaredField("delegatedClassLoader");
			field.setAccessible(true);
			final URLClassLoader delegatedUrlClassLoader = (URLClassLoader) field.get(Thread.currentThread().getContextClassLoader());
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(delegatedUrlClassLoader, url);
		} catch (final Exception ex) {
			LOGGER.error(MARKER, "Method addURL on delegated classloader could not be invoked", ex);
		}
	}
}
