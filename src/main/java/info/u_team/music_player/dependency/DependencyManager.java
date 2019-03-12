package info.u_team.music_player.dependency;

import java.io.*;
import java.nio.file.*;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.*;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraftforge.fml.ModList;

public class DependencyManager {
	
	private static final Logger logger = LogManager.getLogger();
	
	public static final DependencyClassLoader classloader = new DependencyClassLoader();
	
	private static Path cache;
	
	public static void construct() {
		setupCache();
		copyDependencies();
		loadDependencies();
	}
	
	private static void setupCache() {
		logger.info("Creating musicplayer cache");
		try {
			cache = Files.createTempDirectory("musicplayer-dependency-cache");
		} catch (IOException ex) {
			logger.error("Could not create music player cache", ex);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Deleting musicplayer cache " + cache);
			try {
				FileUtils.deleteDirectory(cache.toFile());
			} catch (IOException ex) {
				logger.warn("Could not delete music player cache", ex);
			}
		}, "Remove Music Player Cache Path"));
	}
	
	private static void copyDependencies() {
		logger.info("Try to copy dependencies to cache directory");
		String path = System.getProperty("musicplayer.dev");
		try {
			if (path != null) {
				copyDependenciesDev(path);
			} else {
				copyDependenciesFromJar();
			}
			logger.info("Finished copy of dependencies to cache");
		} catch (Exception ex) {
			logger.error("Could not copy dependencies to cache ", ex);
		}
	}
	
	private static void copyDependenciesDev(String path) throws Exception {
		logger.info("Search dependencies in dev");
		
		FileUtils.copyDirectory(Paths.get(path, "musicplayer-lavaplayer/build/libs").toFile(), cache.toFile());
		FileUtils.copyDirectory(Paths.get(path, "musicplayer-lavaplayer/build/dependencies").toFile(), cache.toFile());
	}
	
	private static void copyDependenciesFromJar() throws Exception {
		logger.info("Search dependencies in jar");
		
		Files.walk(ModList.get().getModFileById(MusicPlayerMod.modid).getFile().findResource("/dependencies")).forEach(path -> {
			try {
				Files.copy(path, new File(cache.toFile(), path.getFileName().toString()).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
	
	private static void loadDependencies() {
		logger.info("Load dependencies into classloader.");
		try {
			Files.walk(cache).filter(path -> path.toFile().isFile()).forEach(path -> classloader.addFile(path.toFile()));
		} catch (IOException ex) {
			logger.error("Could not load file into classloader ", ex);
		}
	}
}
