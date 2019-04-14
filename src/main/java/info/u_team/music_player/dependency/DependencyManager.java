package info.u_team.music_player.dependency;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.*;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.init.MusicPlayerFiles;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;

public class DependencyManager {
	
	private static final Logger logger = LogManager.getLogger();
	private static final Marker setup = MarkerManager.getMarker("Setup");
	private static final Marker load = MarkerManager.getMarker("Load");
	
	public static final DependencyMusicPlayerClassLoader musicplayerclassloader = new DependencyMusicPlayerClassLoader();
	
	private static Path embeddependencies, musicplayerdependencies;
	
	public static void construct() {
		setupCache();
		copyDependencies();
		loadDependencies();
	}
	
	private static void setupCache() {
		Path cache = MusicPlayerFiles.cache;
		embeddependencies = cache.resolve("embed");
		musicplayerdependencies = cache.resolve("musicplayer");
		logger.info(setup, "Creating musicplayer cache at " + cache);
		
		FileUtils.deleteQuietly(cache.toFile());
		try {
			Files.createDirectory(embeddependencies);
			Files.createDirectory(musicplayerdependencies);
		} catch (IOException ex) {
			logger.error(setup, "Could not create music player cache", ex);
		}
		
	}
	
	private static void copyDependencies() {
		logger.info(setup, "Try to copy dependencies to cache directory");
		String path = System.getProperty("musicplayer.dev");
		try {
			if (path != null) {
				copyDependenciesDev(path);
			} else {
				copyDependenciesFromJar();
			}
			logger.info(setup, "Finished copy of dependencies to cache");
		} catch (Exception ex) {
			logger.error(setup, "Could not copy dependencies to cache", ex);
		}
	}
	
	private static void copyDependenciesDev(String path) throws Exception {
		logger.info(setup, "Search dependencies in dev");
		
		FileUtils.copyDirectory(Paths.get(path, "musicplayer-lavaplayer/build/libs").toFile(), musicplayerdependencies.toFile());
		FileUtils.copyDirectory(Paths.get(path, "musicplayer-lavaplayer/build/dependencies").toFile(), musicplayerdependencies.toFile());
	}
	
	private static void copyDependenciesFromJar() throws Exception {
		logger.info(setup, "Search dependencies in jar");
		
		ModFile modfile = ModList.get().getModFileById(MusicPlayerMod.modid).getFile();
		
		extractJarFilesFromZipFolder(modfile, "embed-dependencies", embeddependencies);
		extractJarFilesFromZipFolder(modfile, "dependencies", musicplayerdependencies);
	}
	
	private static void loadDependencies() {
		logger.info(load, "Load dependencies into classloaders");
		
		// No need to check if the runtime is in eclipse because folder will be reset
		// every start correctly
		
		try {
			URLClassLoader systemloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			
			forEachJarFile(embeddependencies, file -> {
				try {
					method.invoke(systemloader, file.toUri().toURL());
					logger.info(load, "Added jar to system classloader: " + file);
				} catch (Exception ex) {
					throw new RuntimeException("Method addURL on system classloader could not be invoked", ex);
				}
			});
		} catch (Exception ex) {
			logger.error(load, "Could not load embedded dependencies", ex);
		}
		
		forEachJarFile(musicplayerdependencies, file -> {
			musicplayerclassloader.addFile(file.toFile());
			logger.info(load, "Added jar to musicplayer classloader: " + file);
		});
		
		logger.info(load, "Dependencies have sucessfully been loaded into classloaders");
	}
	
	private static void extractJarFilesFromZipFolder(ModFile modfile, String folder, Path path) throws IOException {
		Files.walk(modfile.findResource("/" + folder)).filter(file -> file.toString().endsWith(".jar")).forEach(file -> {
			try {
				Files.copy(file, new File(path.toFile(), file.getFileName().toString()).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
	
	private static void forEachJarFile(Path path, Consumer<Path> consumer) {
		try {
			Files.walk(path).filter(file -> file.toString().endsWith(".jar") && file.toFile().isFile()).forEach(consumer);
		} catch (IOException ex) {
			logger.error(load, "Could not jars into classpath from path " + path, ex);
		}
	}
}
