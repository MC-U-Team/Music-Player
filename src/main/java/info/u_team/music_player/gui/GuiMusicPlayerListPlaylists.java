package info.u_team.music_player.gui;

import info.u_team.music_player.musicplayer.*;
import info.u_team.to_export_to_u_team_core.gui.GuiScrollableList;

public class GuiMusicPlayerListPlaylists extends GuiScrollableList<GuiMusicPlayerListPlaylistsEntry> {
	
	private final Playlists playlists;
	
	public GuiMusicPlayerListPlaylists(int width, int height, int top, int bottom, int left, int right) {
		super(width, height, top, bottom, left, right, 50, 20, 5);
		
		playlists = MusicPlayerManager.getPlaylistManager().getPlaylists();
		playlists.forEach(playlist -> addEntry(new GuiMusicPlayerListPlaylistsEntry(this, playlist)));
	}
	
	public void addPlaylist(String name) {
		Playlist playlist = new Playlist(name);
		playlists.add(playlist);
		addEntry(new GuiMusicPlayerListPlaylistsEntry(this, playlist));
	}
	
	public void removePlaylist(GuiMusicPlayerListPlaylistsEntry entry) {
		playlists.remove(entry.getPlaylist());
		removeEntry(entry);
	}
	
	public Playlists getPlaylists() {
		return playlists;
	}
	
	@Override
	protected boolean isSelected(int index) {
		return index == selectedElement;
	}
	
}
