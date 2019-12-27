package info.u_team.music_player.dependency.classloader;

import java.net.*;
import java.nio.file.Path;

import org.apache.logging.log4j.*;

public class DependencyClassLoader extends URLClassLoader {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	static {
		ClassLoader.registerAsParallelCapable();
	}
	
	public DependencyClassLoader() {
		super(new URL[] {}, null);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			return super.loadClass(name);
		} catch (ClassNotFoundException ex) {
			if (name.startsWith("info.u_team.music_player.lavaplayer.api")) {
				return getClass().getClassLoader().loadClass(name);
			}
			throw ex;
		}
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
	
	public void addPath(Path path) {
		try {
			addURL(path.toUri().toURL());
		} catch (MalformedURLException ex) {
			LOGGER.error("Could not add dependency path to classloader", ex);
		}
	}
}
