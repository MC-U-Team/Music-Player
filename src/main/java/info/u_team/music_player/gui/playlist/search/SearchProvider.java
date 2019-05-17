package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.init.MusicPlayerResources;
import net.minecraft.util.ResourceLocation;

public enum SearchProvider {

	YOUTUBE("ytsearch:", MusicPlayerResources.textureSocialYoutube), SOUNDCLOUD("scsearch:", MusicPlayerResources.textureSocialSoundcloud);

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
