package info.u_team.music_player.dependency;

import java.io.IOException;
import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.function.*;
import java.util.stream.Stream;

import org.apache.logging.log4j.*;

import cpw.mods.modlauncher.TransformingClassLoader;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.dependency.classloader.DependencyClassLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.unsafe.UnsafeHacks;

public class DependencyManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker MARKER_LOAD = MarkerManager.getMarker("Load");
	private static final Marker MARKER_ADD = MarkerManager.getMarker("Add");
	
	public static final DependencyClassLoader MUSICPLAYER_CLASSLOADER = new DependencyClassLoader();
	
	public static void load() {
		LOGGER.info(MARKER_LOAD, "Load dependencies");
		
		final String devPath = System.getProperty("musicplayer.dev");
		if (devPath != null) {
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/libs"), path -> addToMusicPlayerDependencies(pathToUrl().apply(path)));
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/dependencies"), path -> addToMusicPlayerDependencies(pathToUrl().apply(path)));
		} else {
			findJarFilesInJar("dependencies/internal", path -> addToInternalDependencies(createInternalURL(path)));
			findJarFilesInJar("dependencies/musicplayer", path -> addToMusicPlayerDependencies(createInternalURL(path)));
		}
		
		LOGGER.info(MARKER_LOAD, "Finished loading dependencies");
	}
	
	private static Function<Path, URL> pathToUrl() {
		return LamdbaExceptionUtils.rethrowFunction(path -> path.toUri().toURL());
	}
	
	private static void findJarFilesInDev(Path path, Consumer<Path> consumer) {
		try (Stream<Path> stream = Files.walk(path)) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (final IOException ex) {
			LOGGER.error(MARKER_LOAD, "When searching for jar files in dev an exception occured.", ex);
		}
	}
	
	private static void findJarFilesInJar(String folder, Consumer<Path> consumer) {
		final ModFile modfile = ModList.get().getModFileById(MusicPlayerMod.MODID).getFile();
		try (Stream<Path> stream = Files.walk(modfile.findResource("/" + folder))) {
			stream.filter(file -> file.toString().endsWith(".jar")).forEach(consumer);
		} catch (final IOException ex) {
			LOGGER.error(MARKER_LOAD, "When searching for jar files in jar an exception occured.", ex);
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "modjar://" + MusicPlayerMod.MODID + path;
		LOGGER.debug(MARKER_LOAD, "Create mod jar url ({}) from path ({}).", url, path);
		try {
			return new URL(url);
		} catch (final MalformedURLException ex) {
			LOGGER.error(MARKER_LOAD, "Could not create url from internal path.", ex);
		}
		return null;
	}
	
	// Add to different classloader
	
	private static void addToMusicPlayerDependencies(URL url) {
		MUSICPLAYER_CLASSLOADER.addURL(url);
		LOGGER.debug(MARKER_ADD, "Added new jar file ({}) to the musicplayer dependency classloader.", url);
	}
	
	private static void addToInternalDependencies(URL url) {
		try {
			final Field field = TransformingClassLoader.class.getDeclaredField("delegatedClassLoader");
			field.setAccessible(true);
			final URLClassLoader delegatedUrlClassLoader = (URLClassLoader) field.get(Thread.currentThread().getContextClassLoader());
			final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			final Lookup lookup = MethodHandles.lookup();
			UnsafeHacks.setIntField(Lookup.class.getDeclaredField("allowedModes"), lookup, -1);
			final MethodHandle methodHandle = lookup.unreflectSpecial(method, URLClassLoader.class);
			methodHandle.invoke(delegatedUrlClassLoader, url);
			LOGGER.debug(MARKER_ADD, "Added new jar file ({}) to the transforming / delegated classloader.", url);
		} catch (final Throwable ex) {
			LOGGER.error(MARKER_LOAD, "Method addURL on transforming / delegated classloader could not be invoked.", ex);
		}
	}
}
