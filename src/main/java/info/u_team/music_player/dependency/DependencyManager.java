package info.u_team.music_player.dependency;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import cpw.mods.cl.ModularURLHandler;
import cpw.mods.cl.ModularURLHandler.IURLProvider;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.dependency.classloader.DependencyClassLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import net.minecraftforge.forgespi.locating.IModFile;

public class DependencyManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker MARKER_LOAD = MarkerManager.getMarker("Load");
	private static final Marker MARKER_ADD = MarkerManager.getMarker("Add");
	
	private static final String FILE_ENDING = ".jar.packed";
	
	public static final DependencyClassLoader MUSICPLAYER_CLASSLOADER = new DependencyClassLoader();
	
	public static void load() {
		LOGGER.info(MARKER_LOAD, "Load dependencies");
		
		setupURLStreamHack();
		
		final String devPath = System.getProperty("musicplayer.dev");
		if (devPath != null) {
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/libs"), path -> addToMusicPlayerDependencies(pathToUrl().apply(path)));
			findJarFilesInDev(Paths.get(devPath, "musicplayer-lavaplayer/build/dependencies"), path -> addToMusicPlayerDependencies(pathToUrl().apply(path)));
		} else {
			findJarFilesInJar("dependencies", path -> addToMusicPlayerDependencies(createInternalURL(path)));
		}
		
		LOGGER.info(MARKER_LOAD, "Finished loading dependencies");
	}
	
	private static Function<Path, URL> pathToUrl() {
		return LamdbaExceptionUtils.rethrowFunction(path -> path.toUri().toURL());
	}
	
	private static void findJarFilesInDev(Path path, Consumer<Path> consumer) {
		try (Stream<Path> stream = Files.walk(path)) {
			stream.filter(file -> file.toString().endsWith(FILE_ENDING)).forEach(consumer);
		} catch (final IOException ex) {
			LOGGER.error(MARKER_LOAD, "When searching for jar files in dev an exception occured.", ex);
		}
	}
	
	private static void findJarFilesInJar(String folder, Consumer<Path> consumer) {
		final IModFile modfile = ModList.get().getModFileById(MusicPlayerMod.MODID).getFile();
		try (Stream<Path> stream = Files.walk(modfile.findResource("/" + folder))) {
			stream.filter(file -> file.toString().endsWith(FILE_ENDING)).forEach(consumer);
		} catch (final IOException ex) {
			LOGGER.error(MARKER_LOAD, "When searching for jar files in jar an exception occured.", ex);
		}
	}
	
	private static URL createInternalURL(Path path) {
		final String url = "jarlookup://" + MusicPlayerMod.MODID + "/" + path;
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
	
	// TODO replace this with a less hacky solution
	private static void setupURLStreamHack() {
		LOGGER.info(MARKER_ADD, "Trying to initialize url stream handler for musicplayer url protocol.");
		
		try {
			final var field = ModularURLHandler.class.getDeclaredField("handlers");
			final Map<String, IURLProvider> handlers = UnsafeHacks.getField(field, ModularURLHandler.INSTANCE);
			
			handlers.put("jarlookup", new IURLProvider() {
				
				@Override
				public String protocol() {
					return "jarlookup";
				}
				
				@Override
				public Function<URL, InputStream> inputStreamFunction() {
					return url -> {
						try {
							final var info = FMLLoader.getLoadingModList().getModFileById(url.getHost());
							if (info == null) {
								throw new IOException("Modid " + url.getHost() + " does not exists");
							}
							final var path = info.getFile().findResource(url.getPath().substring(1));
							return Files.newInputStream(path);
						} catch (IOException ex) {
							LOGGER.error("Could not find resource {}", url);
							throw new UncheckedIOException(ex);
						}
					};
				}
			});
		} catch (Throwable ex) {
			throw new RuntimeException("Could not setup url stream handler. Please report to mod author.", ex);
		}
	}
}
