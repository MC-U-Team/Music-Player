package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class MusicPlayerKeys {
	
	public static final KeyBinding open = new KeyBinding(key_open, Keyboard.KEY_F8, key_category);
	
	public static final KeyBinding pause = new KeyBinding(key_pause, Keyboard.KEY_NUMPAD8, key_category);
	public static final KeyBinding skipForward = new KeyBinding(key_skip_forward, Keyboard.KEY_NUMPAD9, key_category);
	public static final KeyBinding skipBack = new KeyBinding(key_skip_back, Keyboard.KEY_NUMPAD7, key_category);
	
	public static void construct() {
		ClientRegistry.registerKeyBinding(open);
		ClientRegistry.registerKeyBinding(pause);
		ClientRegistry.registerKeyBinding(skipForward);
		ClientRegistry.registerKeyBinding(skipBack);
	}
}
