package info.u_team.music_player.impl.key;

import org.lwjgl.input.Keyboard;

import info.u_team.music_player.MusicPlayerConstants;
import net.minecraft.client.settings.KeyBinding;

public class MusicPlayerKeys {
	
	public static final KeyBinding key_openmusicplayer = new KeyBinding(MusicPlayerConstants.MODID + ":key.open", Keyboard.KEY_F4, MusicPlayerConstants.MODID + ":keycategory");
	
}
