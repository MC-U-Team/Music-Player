package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.gui.BetterScrollableList;

public class GuiMusicSearchList extends BetterScrollableList<GuiMusicSearchListEntry> {
	
	public GuiMusicSearchList() {
		super(0, 0, 0, 0, 0, 0, 40, 20);
	}
	
	public void clear() {
		clearEntries();
	}
	
	public void add(GuiMusicSearchListEntry entry) {
		addEntry(entry);
	}
	
}
