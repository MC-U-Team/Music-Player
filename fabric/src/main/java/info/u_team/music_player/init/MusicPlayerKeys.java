package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_CATEGORY;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_OPEN;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_PAUSE;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_BACK;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_FORWARD;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class MusicPlayerKeys {
	
	public static final KeyMapping OPEN = new KeyMapping(KEY_OPEN, GLFW.GLFW_KEY_F8, KEY_CATEGORY);
	// TODO check to add modifier key ALT back
	public static final KeyMapping PAUSE = new KeyMapping(KEY_PAUSE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_8, KEY_CATEGORY);
	public static final KeyMapping SKIP_FORWARD = new KeyMapping(KEY_SKIP_FORWARD, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_9, KEY_CATEGORY);
	public static final KeyMapping SKIP_BACK = new KeyMapping(KEY_SKIP_BACK, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_7, KEY_CATEGORY);
	
	public static void register() {
		KeyBindingHelper.registerKeyBinding(OPEN);
		KeyBindingHelper.registerKeyBinding(PAUSE);
		KeyBindingHelper.registerKeyBinding(SKIP_FORWARD);
		KeyBindingHelper.registerKeyBinding(SKIP_BACK);
	}
}
