package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.gui.BetterScrollableListEntry;
import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;

public abstract class GuiMusicPlaylistListEntry extends BetterScrollableListEntry<GuiMusicPlaylistListEntry> {
	
	protected void addTrackInfo(IAudioTrack track, int entryX, int entryY, int entryWidth, int leftMargin, int titleColor) {
		GuiTrackUtils.addTrackInfo(track, entryX, entryY, entryWidth, leftMargin, titleColor);
	}
	
	protected void tick() {
	}
}
