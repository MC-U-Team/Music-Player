package info.u_team.music_player.dependency;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.logging.log4j.*;

import info.u_team.music_player.dependency.classloader.DependencyClassLoader;
import info.u_team.music_player.dependency.url.URLStreamHandlerMusicPlayer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class DependencyManager {
	
	private static final Logger logger = LogManager.getLogger();
	private static final Marker load = MarkerManager.getMarker("Load");
	
	private static final DependencyClassLoader musicplayerclassloader = new DependencyClassLoader();
	
	public static void construct() {
		logger.info(load, "Load dependencies");
		
		final String devPath = System.getProperty("musicplayer.dev");
		if (devPath != null) {
			getJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/libs"), DependencyManager::addToMusicPlayerDependencies);
			getJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/dependencies"), DependencyManager::addToMusicPlayerDependencies);
		} else {
			tryUrlHandlerFactory();
			getJarFilesInJar("dependencies/internal", path -> addToInternalDependencies(createInternalURL(path)));
			getJarFilesInJar("dependencies/musicplayer", path -> addToMusicPlayerDependencies(createInternalURL(path)));
			fixSLF4JLogger();
			fixTinyFDLoadLWJGL();
		}
		
		logger.info(load, "Finished loading dependencies");
	}
	
	public static DependencyClassLoader getClassLoader() {
		return musicplayerclassloader;
	}
	
	private static void getJarFilesInDev(Path path, Consumer<Path> consumer) {
		try (Stream<Path> stream = Files.walk(path)) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (IOException ex) {
			logger.error(load, "When searching for jar files in dev an exception occured.", ex);
		}
	}
	
	private static void getJarFilesInJar(String folder, Consumer<Path> consumer) {
		try (FileSystem fileSystem = FileSystems.newFileSystem(DependencyManager.class.getResource("/dependencies").toURI(), Collections.<String, Object> emptyMap())) {
			try (Stream<Path> stream = Files.walk(fileSystem.getPath("/" + folder))) {
				stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
			}
		} catch (Exception ex) {
			logger.error(load, "When searching for jar files in jar an exception occured.", ex);
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "musicplayer:" + path.toString().substring(1);
		logger.debug(load, "Load url " + url);
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
			final LaunchClassLoader launchclassloader = (LaunchClassLoader) DependencyManager.class.getClassLoader();
			launchclassloader.addURL(url);
		} catch (Exception ex) {
			logger.error(load, "Method addURL on launch class loader could not be invoked", ex);
		}
	}
	
	/**
	 * Really hackery. First tries to set the {@link URL#setURLStreamHandlerFactory(URLStreamHandlerFactory)} normally. If
	 * that failed because it is already defined, then use reflections to apply it nevertheless. All urls protocols with
	 * musicplayer are using {@link URLStreamHandlerMusicPlayer} every other protocol is redirected to the url handler that
	 * was set before. If there was non we return null.
	 */
	private static void tryUrlHandlerFactory() {
		try {
			URL.setURLStreamHandlerFactory(protocol -> "musicplayer".equals(protocol) ? new URLStreamHandlerMusicPlayer() : null);
		} catch (Error error) { // Catch error if the handler already is defined. Then try with reflections
			try {
				final Field field = URL.class.getDeclaredField("factory");
				field.setAccessible(true);
				final URLStreamHandlerFactory oldFactory = (URLStreamHandlerFactory) field.get(null);
				final URLStreamHandlerFactory newFactory = protocol -> "musicplayer".equals(protocol) ? new URLStreamHandlerMusicPlayer() : oldFactory != null ? oldFactory.createURLStreamHandler(protocol) : null;
				field.set(null, newFactory);
			} catch (Exception ex) {
				logger.error(load, "Could not replace url stream handler because reflections failed.", ex);
			}
		}
	}
	
	/**
	 * Really hackery. We need to remove all loaded tries of org.slf4j.LoggerFactory from {@link LaunchClassLoader}. Netty
	 * tried to load org.slf4j.LoggerFactory before we can provide our slf4j-api jar. So the {@link LaunchClassLoader}
	 * thinks its an invalid class and prevents it from being loaded again. We remove org.slf4j.LoggerFactory of
	 * {@link LaunchClassLoader#invalidClasses} and {@link LaunchClassLoader#negativeResourceCache} set
	 */
	private static void fixSLF4JLogger() {
		try {
			final String slf4jLoggerFactory = "org.slf4j.LoggerFactory";
			
			final LaunchClassLoader launchClassLoader = (LaunchClassLoader) DependencyManager.class.getClassLoader();
			
			final Set<String> invalidClasses = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, launchClassLoader, "invalidClasses");
			final Set<String> negativeResourceCache = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, launchClassLoader, "negativeResourceCache");
			
			invalidClasses.remove(slf4jLoggerFactory);
			negativeResourceCache.remove(slf4jLoggerFactory);
		} catch (Exception ex) {
			logger.error(load, "Can't fix slf4j logger.", ex);
		}
	}
	
	/**
	 * Really hackery. We remove the lwjgl pack from classloader exceptions, so it can load our class. Then we load our
	 * class and reset the classloader exceptions.
	 */
	private static void fixTinyFDLoadLWJGL() {
		try {
			final String tinyFDClass = "org.lwjgl.util.tinyfd.TinyFileDialogs";
			final String lwjglExclusion = "org.lwjgl.";
			
			final LaunchClassLoader launchClassLoader = (LaunchClassLoader) DependencyManager.class.getClassLoader();
			
			final Set<String> classLoaderExceptions = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, launchClassLoader, "classLoaderExceptions");
			
			classLoaderExceptions.remove(lwjglExclusion);
			launchClassLoader.loadClass(tinyFDClass);
			classLoaderExceptions.add(lwjglExclusion);
		} catch (Exception ex) {
			logger.error(load, "Can't fix tinyfd load lwjgl", ex);
		}
	}
}
