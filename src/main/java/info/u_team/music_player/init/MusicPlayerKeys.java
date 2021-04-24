package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_CATEGORY;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_OPEN;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_PAUSE;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_BACK;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_FORWARD;

import org.lwjgl.glfw.GLFW;

import info.u_team.u_team_core.util.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MusicPlayerKeys {
	
	public static final KeyBinding OPEN = new KeyBinding(KEY_OPEN, GLFW.GLFW_KEY_F8, KEY_CATEGORY);
	
	public static final KeyBinding PAUSE = new KeyBinding(KEY_PAUSE, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_8, KEY_CATEGORY);
	public static final KeyBinding SKIP_FORWARD = new KeyBinding(KEY_SKIP_FORWARD, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_9, KEY_CATEGORY);
	public static final KeyBinding SKIP_BACK = new KeyBinding(KEY_SKIP_BACK, KeyConflictContext.UNIVERSAL, KeyModifier.ALT, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_7, KEY_CATEGORY);
	
	private static void setup(FMLClientSetupEvent event) {
		ClientRegistry.registerKeybinding(OPEN);
		ClientRegistry.registerKeybinding(PAUSE);
		ClientRegistry.registerKeybinding(SKIP_FORWARD);
		ClientRegistry.registerKeybinding(SKIP_BACK);
	}
	
	public static void registerMod(IEventBus bus) {
		bus.addListener(MusicPlayerKeys::setup);
	}
}
