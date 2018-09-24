package info.u_team.music_player.classloader;

import java.io.File;
import java.net.*;

import info.u_team.music_player.MusicPlayerConstants;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class CustomURLClassLoader extends URLClassLoader {
	
	public CustomURLClassLoader() {
		super(new URL[] {}, null);
	}
	
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			return super.findClass(name);
		} catch (ClassNotFoundException ex) {
			return ((LaunchClassLoader) this.getClass().getClassLoader()).findClass(name);
		}
	}
	
	@Override
	public URL findResource(String name) {
		URL url = super.findResource(name);
		if (url == null) {
			url = ((LaunchClassLoader) this.getClass().getClassLoader()).findResource(name);
		}
		return url;
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
	
	public void addFile(File file) {
		try {
			addURL(file.toURI().toURL());
		} catch (MalformedURLException ex) {
			MusicPlayerConstants.LOGGER.error(ex);
		}
	}
	
}
