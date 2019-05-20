package info.u_team.music_player.musicplayer.settings;

import info.u_team.music_player.init.MusicPlayerResources;
import net.minecraft.util.ResourceLocation;

public enum Repeat {

	NO(false, MusicPlayerResources.textureRepeat), PLAYLIST(true, MusicPlayerResources.textureRepeat), SINGLE(true, MusicPlayerResources.textureRepeatSingle);

	private final boolean active;
	private final ResourceLocation resource;

	private Repeat(boolean active, ResourceLocation resource) {
		this.active = active;
		this.resource = resource;
	}

	public ResourceLocation getResource() {
		return resource;
	}

	public boolean isActive() {
		return active;
	}

	public static Repeat forwardCycle(Repeat repeat) {
		if (repeat == NO) {
			repeat = PLAYLIST;
		} else if (repeat == PLAYLIST) {
			repeat = SINGLE;
		} else {
			repeat = NO;
		}
		return repeat;
	}

}
