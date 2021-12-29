package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.init.MusicPlayerResources;
import net.minecraft.resources.ResourceLocation;

public enum SearchProvider {
	
	YOUTUBE("ytsearch:", MusicPlayerResources.TEXTURE_SOCIAL_YOUTUBE),
	SOUNDCLOUD("scsearch:", MusicPlayerResources.TEXTURE_SOCIAL_SOUNDCLOUD);
	
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
