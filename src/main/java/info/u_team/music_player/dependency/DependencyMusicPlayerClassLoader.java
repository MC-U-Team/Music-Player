package info.u_team.music_player.dependency;

import java.io.File;
import java.net.*;

import org.apache.logging.log4j.*;

public class DependencyMusicPlayerClassLoader extends URLClassLoader {
	
	private static final Logger logger = LogManager.getLogger();
	
	DependencyMusicPlayerClassLoader() {
		super(new URL[] {}, null);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			return super.loadClass(name);
		} catch (ClassNotFoundException ex) {
			if (name.startsWith("info.u_team.music_player.lavaplayer.api")) {
				return DependencyMusicPlayerClassLoader.class.getClassLoader().loadClass(name);
			}
			throw ex;
		}
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
	
	public void addFile(File file) {
		try {
			addURL(file.toURI().toURL());
		} catch (MalformedURLException ex) {
			logger.error("Could not add dependency file to classloader", ex);
		}
	}
}
