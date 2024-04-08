package info.u_team.music_player.impl;

import java.io.IOException;
import java.nio.file.Path;

import info.u_team.music_player.dependency.DependencyManager.PathLocator;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.locating.IModFile;

public class ForgePathLocator implements PathLocator {
	
	@Override
	public Path locate(String modid, String folder) throws IOException, IllegalStateException {
		final IModFile modfile = ModList.get().getModFileById(modid).getFile();
		
		return modfile.findResource(folder);
	}
	
}
