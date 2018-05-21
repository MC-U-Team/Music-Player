package info.u_team.music_player.dependency;

import static info.u_team.music_player.MusicPlayerConstants.*;

import java.io.*;
import java.lang.reflect.Field;

import net.hycrafthd.gradlew.*;
import net.minecraftforge.common.ForgeVersion;

public class DependencyManager {
	
	private String mcversion;
	private File project;
	
	public DependencyManager() {
		mcversion = getMcVersion();
		PATH.mkdirs();
	}
	
	public void execute() {
		runGradle();
		addLibariesToClassLoader();
		// CLASSLOADER.addFile(new File("C:\\Users\\hycra\\Desktop\\music_player_impl-1.8.jar")); 
		//TODO load jars from file to classloader
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
	
	private String getMcVersion() {
		try {
			Field field = ForgeVersion.class.getDeclaredField("mcVersion");
			return (String) field.get(null);
		} catch (Exception ex) {
			return "error";
		}
	}
	
}
