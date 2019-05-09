package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.u_team_core.gui.elements.GuiScrollableList;

public class GuiMusicSearchList extends GuiScrollableList<GuiMusicSearchListEntry> {
	
	private final Playlist playlist;
	
	public GuiMusicSearchList(Playlist playlist) {
		super(0, 0, 0, 0, 0, 0, 40, 20, 5);
		this.playlist = playlist;
	}
}
