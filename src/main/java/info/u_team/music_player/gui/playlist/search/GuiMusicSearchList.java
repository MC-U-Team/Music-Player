package info.u_team.music_player.gui.playlist.search;

import info.u_team.u_team_core.gui.elements.GuiScrollableList;

public class GuiMusicSearchList extends GuiScrollableList<GuiMusicSearchListEntry> {

	public GuiMusicSearchList() {
		super(0, 0, 0, 0, 0, 0, 40, 20, 5);
	}

	public void clear() {
		clearEntries();
	}

	public void add(GuiMusicSearchListEntry entry) {
		addEntry(entry);
	}

}
