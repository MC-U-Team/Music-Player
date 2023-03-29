package info.u_team.music_player.dependency;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import com.google.common.base.Predicates;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.dependency.classloader.DependencyClassLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.StringUtil;

public class DependencyManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker MARKER_LOAD = MarkerManager.getMarker("Load");
	private static final Marker MARKER_ADD = MarkerManager.getMarker("Add");
	
	private static final String FILE_ENDING = ".jar.packed";
	
	public static final DependencyClassLoader MUSICPLAYER_CLASSLOADER = new DependencyClassLoader();
	
	public static void load() {
		LOGGER.info(MARKER_LOAD, "Load dependencies");
		
		final Path tmpPath = createExtractDirectory();
		
		LOGGER.info(MARKER_LOAD, "Extraction directory for jar files is {} ", tmpPath.toAbsolutePath());
		
		final String devPath = System.getProperty("musicplayer.dev");
		final Set<Path> paths;
		if (devPath != null) {
			paths = Stream.of(devPath.split(";")) //
					.filter(Predicates.not(StringUtil::isNullOrEmpty)) //
					.map(Paths::get) //
					.map(DependencyManager::findJarFilesInDev) //
					.flatMap(Set::stream) //
					.collect(Collectors.toUnmodifiableSet());
		} else {
			paths = findJarFilesInJar("dependencies");
		}
		
		paths.stream() //
				.map(path -> extractFile(tmpPath, path)) //
				.map(DependencyManager::pathToUrl) //
				.forEach(DependencyManager::addToMusicPlayerDependencies);
		
		LOGGER.info(MARKER_LOAD, "Finished loading dependencies");
	}
	
	private static Path createExtractDirectory() {
		try {
			final Path baseDirectory = Paths.get(System.getProperty("java.io.tmpdir", "/tmp"), MusicPlayerMod.MODID + "-extraction-tmp");
			final Path specificDirectory = baseDirectory.resolve(String.valueOf(System.currentTimeMillis()));
			
			// Try to clean base directory before
			try {
				FileUtils.deleteDirectory(baseDirectory.toFile());
			} catch (Exception ex) {
			}
			
			Files.createDirectories(specificDirectory);
			return specificDirectory;
		} catch (final IOException unused) {
			try {
				return Files.createTempDirectory(MusicPlayerMod.MODID + "-extraction-tmp");
			} catch (final IOException ex) {
				throw new RuntimeException("Cannot create extract directory for musicplayer files", ex);
			}
		}
	}
	
	private static Path extractFile(Path extractDirectory, Path path) {
		final Path extractPath = extractDirectory.resolve(path.getFileName().toString());
		try (final InputStream inputStream = Files.newInputStream(path); //
				final OutputStream outputStream = Files.newOutputStream(extractPath, StandardOpenOption.CREATE);) {
			inputStream.transferTo(outputStream);
			LOGGER.debug(MARKER_LOAD, "Copied file from ({}) to ({})", path, extractPath);
		} catch (final IOException ex) {
			throw new RuntimeException("Cannot extract file " + path + " to " + extractPath, ex);
		}
		return extractPath;
	}
	
	private static URL pathToUrl(Path path) {
		try {
			return path.toUri().toURL();
		} catch (final MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static Set<Path> findJarFilesInDev(Path path) {
		try (final Stream<Path> stream = Files.walk(path)) {
			return filterPackedFiles(stream);
		} catch (final IOException ex) {
			LOGGER.error(MARKER_LOAD, "When searching for jar files in dev an exception occured.", ex);
		}
		return Collections.emptySet();
	}
	
	private static Set<Path> findJarFilesInJar(String folder) {
		final ModContainer container = FabricLoader.getInstance().getModContainer(MusicPlayerMod.MODID).orElseThrow(IllegalStateException::new);
		
		try (final Stream<Path> stream = Files.walk(container.findPath(folder).orElseThrow(IllegalStateException::new))) {
			return filterPackedFiles(stream);
		} catch (final IOException | IllegalStateException ex) {
			LOGGER.error(MARKER_LOAD, "When searching for jar files in jar an exception occured.", ex);
		}
		return Collections.emptySet();
	}
	
	private static Set<Path> filterPackedFiles(Stream<Path> stream) {
		return stream.filter(file -> file.toString().endsWith(FILE_ENDING)).collect(Collectors.toSet());
	}
	
	private static void addToMusicPlayerDependencies(URL url) {
		MUSICPLAYER_CLASSLOADER.addURL(url);
		LOGGER.debug(MARKER_ADD, "Added new jar file ({}) to the musicplayer dependency classloader.", url);
	}
}
