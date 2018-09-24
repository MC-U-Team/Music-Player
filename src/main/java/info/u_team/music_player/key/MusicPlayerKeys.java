package info.u_team.music_player.key;

import org.lwjgl.input.Keyboard;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.u_team_core.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class MusicPlayerKeys {
	
	public static final KeyBinding key_openmusicplayer = new KeyBinding(MusicPlayerConstants.MODID + ":key.open", Keyboard.KEY_F4, MusicPlayerConstants.MODID + ":keycategory");
	
	public static void init() {
		ClientRegistry.registerKeybinding(key_openmusicplayer);
	}
	
}
