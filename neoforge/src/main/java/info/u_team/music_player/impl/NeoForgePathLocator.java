package info.u_team.music_player.impl;

import java.io.IOException;
import java.nio.file.Path;

import info.u_team.music_player.dependency.DependencyManager.PathLocator;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.locating.IModFile;

public class NeoForgePathLocator implements PathLocator {
	
	@Override
	public Path locate(String modid, String folder) throws IOException, IllegalStateException {
		final IModFile modfile = ModList.get().getModFileById(modid).getFile();
		
		return modfile.findResource(folder);
	}
	
}
