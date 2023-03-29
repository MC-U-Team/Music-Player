package info.u_team.music_player.dependency.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;

public class DependencyClassLoader extends URLClassLoader {
	
	private final Collection<String> ourClassLoaderPackages = Arrays.asList("info.u_team.music_player.lavaplayer.api", "javax.script");
	
	static {
		ClassLoader.registerAsParallelCapable();
	}
	
	private final ClassLoader ourClassLoader;
	
	public DependencyClassLoader() {
		super(new URL[] {}, null);
		this.ourClassLoader = getClass().getClassLoader();
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			return super.loadClass(name);
		} catch (final ClassNotFoundException ex) {
			if (ourClassLoaderPackages.stream().anyMatch(name::startsWith)) {
				return ourClassLoader.loadClass(name);
			}
			throw ex;
		}
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
}