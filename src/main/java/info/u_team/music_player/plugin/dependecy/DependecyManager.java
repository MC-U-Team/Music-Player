package info.u_team.music_player.plugin.dependecy;

import static info.u_team.music_player.MusicPlayerConstants.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

import net.minecraft.launchwrapper.LaunchClassLoader;

public class DependecyManager {
	
	public DependecyManager() {
		if (!PATH.exists()) {
			PATH.mkdirs();
		}
	}
	
	public void execute() {
		setupIvy();
		for (MavenEntry mavenentry : MAVENENTRY) {
			loadDependency(mavenentry);
		}
	}
	
	private void setupIvy() {
		try {
			String name = "ivy.jar";
			String link = "http://central.maven.org/maven2/org/apache/ivy/ivy/2.4.0/ivy-2.4.0.jar";
			
			File file = new File(PATH, name);
			
			if (!file.exists()) {
				FileUtils.copyURLToFile(new URL(link), file);
				LOGGER.info("Downloaded " + name + " from " + link + " to " + file + ".");
			} else {
				LOGGER.info("Found " + name + " in path " + file + ".");
			}
			
			URLClassLoader classloader = (URLClassLoader) LaunchClassLoader.class.getClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(classloader, file.toURI().toURL());
			
		} catch (Exception ex) {
			LOGGER.error("Failed to setup ivy!", ex);
		}
	}
	
	private void loadDependency(MavenEntry mavenentry) {
		try {
			LOGGER.info("Try to setup dependecy " + mavenentry + ".");
			
			File output = new File(PATH, "files");
			new IvyDownloader(mavenentry, PATH, output).execute();
			
			List<File> validfiles = new ArrayList<File>();
			File[] files = output.listFiles();
			
			if (files == null) {
				throw new RuntimeException("No downloaded files are found");
			}
			
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".jar")) {
					validfiles.add(file);
				}
			}
			validfiles.forEach(CLASSLOADER::addFile);
			
			LOGGER.info("Finished dependecy " + mavenentry + " setup.");
		} catch (Exception ex) {
			LOGGER.error("Can't download and add dependecy " + mavenentry + ".", ex);
		}
	}
}
