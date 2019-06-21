package info.u_team.music_player.gui.playlist.search;

import info.u_team.to_u_team_core.gui.GuiScrollableList;

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
