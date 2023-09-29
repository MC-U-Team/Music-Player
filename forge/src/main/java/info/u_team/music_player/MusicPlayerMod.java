package info.u_team.music_player;

import java.nio.ByteBuffer;

import org.lwjgl.openal.ALC11;

import info.u_team.u_team_core.util.annotation.AnnotationManager;
import info.u_team.u_team_core.util.verify.JarSignVerifier;
import net.minecraftforge.fml.common.Mod;

@Mod(MusicPlayerMod.MODID)
public class MusicPlayerMod {
	
	public static final String MODID = MusicPlayerReference.MODID;
	
	public MusicPlayerMod() {
		JarSignVerifier.checkSigned(MODID);
		
		AnnotationManager.callAnnotations(MODID);
		
		ALC11.alcOpenDevice((String) null);
	}
}
