package info.u_team.music_player.dependency;

import static info.u_team.music_player.MusicPlayerConstants.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import info.u_team.music_player.MusicPlayerConstants;
import net.hycrafthd.gradlew.*;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;

public class DependencyManager {
	
	private String mcversion;
	private boolean isDev;
	private File project;
	
	public DependencyManager() {
		mcversion = getMcVersion();
		isDev = isDev();
		PATH.mkdirs();
	}
	
	public void execute() {
		runGradle();
		addLibariesToClassLoader();
		loadImpl();
	}
	
	private void runGradle() {
		GradlewExecutor executor = new GradlewExecutor(PATH, new IExecutorLog() {
			
			@Override
			public void log(InputStream inputstream) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
				String line;
				try {
					while ((line = reader.readLine()) != null) {
						LOGGER.info(line);
					}
				} catch (IOException ex) {
					LOGGER.error("Error while logging gradle process.", ex);
				}
			}
			
			@Override
			public void print(String string) {
				LOGGER.info(string);
			}
		});
		executor.execute("--info", "processResources", "copyLibraries");
		project = executor.getProject();
	}
	
	private void addLibariesToClassLoader() {
		File libaries = new File(project, "libraries");
		File[] files = libaries.listFiles();
		if (files == null) {
			throw new IllegalArgumentException("Why are no libraries downloaded? Maybe missing internet connection?");
		}
		for (File file : files) {
			CLASSLOADER.addFile(file);
		}
	}
	
	private void loadImpl() {
		String[] split = MusicPlayerConstants.VERSION.split("\\.");
		String version = split[0] + "." + split[1] + "." + split[2];
		String name = "music_player_impl-" + mcversion + "-" + version + (isDev ? "-dev" : "") + ".jar";
		URL url = getClass().getResource("/impl/" + name);
		try {
			File file = new File(PATH, name);
			try {
				file.delete();
			} catch (Exception ex) {
				// Dont do anything. If it fails it doesn't matter cause its only a clean up.
				// Should be overwrited anyways
			}
			FileUtils.copyURLToFile(url, file);
			CLASSLOADER.addFile(file);
		} catch (Exception ex) {
			LOGGER.fatal("Failed to load right impl jar (" + name + "). When you are indev, you can ignore this error. Else check your Minecraft version.", ex);
		}
	}
	
	private String getMcVersion() {
		try {
			Field field = ForgeVersion.class.getDeclaredField("mcVersion");
			return (String) field.get(null);
		} catch (Exception ex) {
			return "error";
		}
	}
	
	private boolean isDev() {
		return ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"));
	}
}
