package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.lwjgl.glfw.GLFW;

import info.u_team.u_team_core.util.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.settings.*;

@OnlyIn(Dist.CLIENT)
public class MusicPlayerKeys {
	
	public static final KeyBinding open = new KeyBinding(key_open, GLFW.GLFW_KEY_F8, key_category);
	
	public static final KeyBinding pause = new KeyBinding(key_pause, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_8, key_category);
	public static final KeyBinding skipForward = new KeyBinding(key_skip_forward, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_9, key_category);
	public static final KeyBinding skipBack = new KeyBinding(key_skip_back, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_7, key_category);
	
	public static void construct() {
		ClientRegistry.registerKeybinding(open);
		ClientRegistry.registerKeybinding(pause);
		ClientRegistry.registerKeybinding(skipForward);
		ClientRegistry.registerKeybinding(skipBack);
	}
}
