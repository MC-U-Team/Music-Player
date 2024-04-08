package info.u_team.music_player;

import info.u_team.u_team_core.util.annotation.AnnotationManager;
import net.neoforged.fml.common.Mod;

@Mod(MusicPlayerMod.MODID)
public class MusicPlayerMod {
	
	public static final String MODID = MusicPlayerReference.MODID;
	
	public MusicPlayerMod() {
		AnnotationManager.callAnnotations(MODID);
	}
}
