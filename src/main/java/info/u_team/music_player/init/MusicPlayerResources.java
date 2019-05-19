package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraft.util.ResourceLocation;

public class MusicPlayerResources {
	
	public static final ResourceLocation textureCreate = createResource("create");
	public static final ResourceLocation textureOpen = createResource("open");
	public static final ResourceLocation textureClear = createResource("clear");
	public static final ResourceLocation texturePlay = createResource("play");
	public static final ResourceLocation textureStop = createResource("stop");
	public static final ResourceLocation texturePause = createResource("pause");
	public static final ResourceLocation textureAdd = createResource("add");
	public static final ResourceLocation textureUp = createResource("up");
	public static final ResourceLocation textureDown = createResource("down");
	public static final ResourceLocation textureBack = createResource("back");
	public static final ResourceLocation textureSkipForward = createResource("skip-forward");
	public static final ResourceLocation textureSkipBack = createResource("skip-back");
	public static final ResourceLocation textureShuffle = createResource("shuffle");
	public static final ResourceLocation textureRepeat = createResource("repeat");
	public static final ResourceLocation textureRepeatSingle = createResource("repeat-single");
	public static final ResourceLocation textureSettings = createResource("settings");
	
	public static final ResourceLocation textureSocialYoutube = createResource("socialmedia/youtube-logo");
	public static final ResourceLocation textureSocialSoundcloud = createResource("socialmedia/soundcloud-logo");
	
	private static ResourceLocation createResource(String name) {
		return new ResourceLocation(MusicPlayerMod.modid, "textures/gui/" + name + ".png");
	}
	
}
