package info.u_team.music_player.gui.playlist.searchOLD;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraft.util.ResourceLocation;

public enum SearchProvider {
	
	YOUTUBE("ytsearch:", new ResourceLocation(MusicPlayerMod.modid, "textures/gui/socialmedia/youtube-logo.png")),
	SOUNDCLOUD("scsearch:", new ResourceLocation(MusicPlayerMod.modid, "textures/gui/socialmedia/soundcloud-logo.png"));
	
	private final String prefix;
	private final ResourceLocation logo;
	
	private SearchProvider(String prefix, ResourceLocation logo) {
		this.prefix = prefix;
		this.logo = logo;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ResourceLocation getLogo() {
		return logo;
	}
	
	public static SearchProvider toggle(SearchProvider provider) {
		if (provider == YOUTUBE) {
			return SOUNDCLOUD;
		}
		return YOUTUBE;
	}
	
}
