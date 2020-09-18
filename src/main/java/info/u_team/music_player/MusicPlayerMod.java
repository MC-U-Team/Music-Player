package info.u_team.music_player;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import net.minecraftforge.fml.common.Mod;

@Mod(MusicPlayerMod.MODID)
public class MusicPlayerMod {
	
	public static final String MODID = "musicplayer";
	
	public MusicPlayerMod() {
		JarSignVerifier.checkSigned(MODID);
	}
}
