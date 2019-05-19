package info.u_team.music_player.init;

import org.lwjgl.glfw.GLFW;

import info.u_team.u_team_core.registry.util.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.client.settings.*;

@OnlyIn(Dist.CLIENT)
public class MusicPlayerKeys {
	
	public static final KeyBinding open = new KeyBinding("Open", GLFW.GLFW_KEY_F8, "Music Player");
	
	public static final KeyBinding pause = new KeyBinding("Pause", KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F10, "Music Player");
	public static final KeyBinding skipForward = new KeyBinding("Skip forward", KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F9, "Music Player");
	public static final KeyBinding skipBack = new KeyBinding("Skip back", KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F11, "Music Player");
	
	public static void construct() {
		ClientRegistry.registerKeybinding(open);
		ClientRegistry.registerKeybinding(pause);
		ClientRegistry.registerKeybinding(skipForward);
		ClientRegistry.registerKeybinding(skipBack);
	}
}
