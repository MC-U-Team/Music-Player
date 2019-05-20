package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.lwjgl.input.Keyboard;

import info.u_team.u_team_core.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class MusicPlayerKeys {
	
	public static final KeyBinding open = new KeyBinding(key_open, Keyboard.KEY_F8, key_category);
	
	public static final KeyBinding pause = new KeyBinding(key_pause, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_NUMPAD8, key_category);
	public static final KeyBinding skipForward = new KeyBinding(key_skip_forward, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_NUMPAD9, key_category);
	public static final KeyBinding skipBack = new KeyBinding(key_skip_back, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_NUMPAD7, key_category);
	
	public static void construct() {
		ClientRegistry.registerKeybinding(open);
		ClientRegistry.registerKeybinding(pause);
		ClientRegistry.registerKeybinding(skipForward);
		ClientRegistry.registerKeybinding(skipBack);
	}
}
