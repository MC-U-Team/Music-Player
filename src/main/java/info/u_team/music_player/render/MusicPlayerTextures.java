package info.u_team.music_player.render;

import info.u_team.music_player.MusicPlayerConstants;
import net.minecraft.util.ResourceLocation;

public class MusicPlayerTextures {
	
	private static final String texture_path = MusicPlayerConstants.MODID + ":textures/gui/";
	
	public static final ResourceLocation texture_button_play = new ResourceLocation(texture_path + "play.png");
	public static final ResourceLocation texture_button_pause = new ResourceLocation(texture_path + "pause.png");
	public static final ResourceLocation texture_button_open = new ResourceLocation(texture_path + "open.png");
	public static final ResourceLocation texture_button_repeat = new ResourceLocation(texture_path + "repeat.png");
	public static final ResourceLocation texture_button_shuffle = new ResourceLocation(texture_path + "shuffle.png");
	public static final ResourceLocation texture_button_skip = new ResourceLocation(texture_path + "skip.png");
	public static final ResourceLocation texture_button_clear = new ResourceLocation(texture_path + "clear.png");
	
}
