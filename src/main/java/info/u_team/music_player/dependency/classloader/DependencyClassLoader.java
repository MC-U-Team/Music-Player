package info.u_team.music_player.dependency.classloader;

import cpw.mods.cl.ModularURLHandler;

import java.net.URL;
import java.net.URLClassLoader;

public class DependencyClassLoader extends URLClassLoader {
	
	static {
		ClassLoader.registerAsParallelCapable();
		/*
		 Call ServiceLoader.load(ModuleLayer, IURLProvider.class) and put to private Map<String, IURLProvider> handlers
		 (See resources/META-INF/services/cpw.mods.cl.ModularURLHandler$IURLProvider and cpw.mods.cl.ModularURLHandler#initFrom)
		 */
		ModularURLHandler.initFrom(DependencyClassLoader.class.getModule().getLayer());
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
			if (name.startsWith("info.u_team.music_player.lavaplayer.api")) {
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
