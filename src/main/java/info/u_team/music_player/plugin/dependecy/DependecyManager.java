package info.u_team.music_player.plugin.dependecy;

import static info.u_team.music_player.MusicPlayerConstants.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

import net.minecraft.launchwrapper.LaunchClassLoader;

public class DependecyManager {
	
	private File path = new File(PATH, "dependecies");
	
	public DependecyManager() {
		if (!path.exists()) {
			path.mkdirs();
		}
	}
	
	public void execute() {
		setupIvy();
		for (MavenEntry mavenentry : MAVENENTRY) {
			loadDependency(mavenentry);
		}
		setupMod();
	}
	
	private void setupIvy() {
		try {
			String name = "ivy.jar";
			URL url = new URL("http://central.maven.org/maven2/org/apache/ivy/ivy/2.4.0/ivy-2.4.0.jar");
			
			File file = copyFromURLtoFile(url, name);
			
			URLClassLoader classloader = (URLClassLoader) LaunchClassLoader.class.getClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(classloader, file.toURI().toURL());
			
		} catch (Exception ex) {
			LOGGER.error("Failed to setup ivy!", ex);
		}
	}
	
	private void setupMod() {
		try {
			String name = "musicplayer-impl.jar";
			URL url = getClass().getResource("/impl.jar");
			
			if (url == null) {
				throw new RuntimeException("Resource can't be found in jar!");
			}
			
			File file = copyFromURLtoFile(url, name);
			
			CLASSLOADER.addFile(file);
		} catch (Exception ex) {
			LOGGER.error("Failed to setup mod impl version! This should not happen?!", ex);
		}
	}
	
	private File copyFromURLtoFile(URL url, String name) throws Exception {
		File file = new File(path, name);
		
		if (!file.exists()) {
			FileUtils.copyURLToFile(url, file);
			LOGGER.info("Downloaded " + name + " from " + url + " to " + file + ".");
		} else {
			LOGGER.info("Found " + name + " in path " + file + ".");
		}
		
		return file;
	}
	
	private void loadDependency(MavenEntry mavenentry) {
		try {
			LOGGER.info("Try to setup dependecy " + mavenentry + ".");
			
			File output = new File(path, "files");
			new IvyDownloader(mavenentry, path, output).execute();
			
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
