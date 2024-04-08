package info.u_team.music_player.impl;

import java.io.IOException;
import java.nio.file.Path;

import info.u_team.music_player.dependency.DependencyManager.PathLocator;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class FabricPathLocator implements PathLocator {
	
	@Override
	public Path locate(String modid, String folder) throws IOException, IllegalStateException {
		final ModContainer container = FabricLoader.getInstance().getModContainer(modid).orElseThrow(IllegalStateException::new);
		
		return container.findPath(folder).orElseThrow(IllegalStateException::new);
	}
	
}
