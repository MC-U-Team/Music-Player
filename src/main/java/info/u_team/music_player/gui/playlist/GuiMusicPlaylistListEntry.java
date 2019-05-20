package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.u_team_core.gui.elements.GuiScrollableListEntry;

public abstract class GuiMusicPlaylistListEntry extends GuiScrollableListEntry<GuiMusicPlaylistListEntry> {

	protected void addTrackInfo(IAudioTrack track, int entryWidth, int leftMargin, int titleColor) {
		GuiTrackUtils.addTrackInfo(track, getX(), getY(), entryWidth, leftMargin, titleColor);
	}

	protected void tick() {
	}
}
