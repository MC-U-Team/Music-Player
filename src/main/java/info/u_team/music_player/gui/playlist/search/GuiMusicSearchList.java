package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.to_export_to_u_team_core.gui.GuiScrollableList;

public class GuiMusicSearchList extends GuiScrollableList<GuiMusicSearchListEntry> {
	
	private final Playlist playlist;
	
	public GuiMusicSearchList(Playlist playlist) {
		super(0, 0, 0, 0, 0, 0, 40, 20, 5);
		this.playlist = playlist;
	}
}
