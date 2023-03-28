package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraft.resources.ResourceLocation;

public class MusicPlayerResources {
	
	public static final ResourceLocation TEXTURE_CREATE = createResource("create");
	public static final ResourceLocation TEXTURE_OPEN = createResource("open");
	public static final ResourceLocation TEXTURE_CLEAR = createResource("clear");
	public static final ResourceLocation TEXTURE_PLAY = createResource("play");
	public static final ResourceLocation TEXTURE_STOP = createResource("stop");
	public static final ResourceLocation TEXTURE_PAUSE = createResource("pause");
	public static final ResourceLocation TEXTURE_ADD = createResource("add");
	public static final ResourceLocation TEXTURE_UP = createResource("up");
	public static final ResourceLocation TEXTURE_DOWN = createResource("down");
	public static final ResourceLocation TEXTURE_BACK = createResource("back");
	public static final ResourceLocation TEXTURE_SKIP_FORWARD = createResource("skip-forward");
	public static final ResourceLocation TEXTURE_SKIP_BACK = createResource("skip-back");
	public static final ResourceLocation TEXTURE_SHUFFLE = createResource("shuffle");
	public static final ResourceLocation TEXTURE_REPEAT = createResource("repeat");
	public static final ResourceLocation TEXTURE_REPEAT_SINGLE = createResource("repeat-single");
	public static final ResourceLocation TEXTURE_SETTINGS = createResource("settings");
	
	public static final ResourceLocation TEXTURE_SOCIAL_YOUTUBE = createResource("socialmedia/youtube-logo");
	public static final ResourceLocation TEXTURE_SOCIAL_SOUNDCLOUD = createResource("socialmedia/soundcloud-logo");
	
	private static ResourceLocation createResource(String name) {
		return new ResourceLocation(MusicPlayerMod.MODID, "textures/gui/" + name + ".png");
	}
	
}
