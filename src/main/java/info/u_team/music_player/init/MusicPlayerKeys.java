package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_CATEGORY;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_OPEN;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_PAUSE;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_BACK;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_FORWARD;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MusicPlayerKeys {
	
	public static final KeyMapping OPEN = new KeyMapping(KEY_OPEN, GLFW.GLFW_KEY_F8, KEY_CATEGORY);
	
	public static final KeyMapping PAUSE = new KeyMapping(KEY_PAUSE, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_8, KEY_CATEGORY);
	public static final KeyMapping SKIP_FORWARD = new KeyMapping(KEY_SKIP_FORWARD, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_9, KEY_CATEGORY);
	public static final KeyMapping SKIP_BACK = new KeyMapping(KEY_SKIP_BACK, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_7, KEY_CATEGORY);
	
	private static void setup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(OPEN);
		ClientRegistry.registerKeyBinding(PAUSE);
		ClientRegistry.registerKeyBinding(SKIP_FORWARD);
		ClientRegistry.registerKeyBinding(SKIP_BACK);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(MusicPlayerKeys::setup);
	}
}
