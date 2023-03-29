package info.u_team.music_player.gui;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;

public class GuiMusicPlayerList extends BetterScrollableList<GuiMusicPlayerListEntry> {
	
	private final Playlists playlists;
	
	public GuiMusicPlayerList(int width, int height, int top, int bottom, int left, int right) {
		super(width, height, top, bottom, left, right, 50, 20);
		
		playlists = MusicPlayerManager.getPlaylistManager().getPlaylists();
		playlists.forEach(playlist -> addEntry(new GuiMusicPlayerListEntry(this, playlists, playlist)));
	}
	
	public void addPlaylist(String name) {
		final Playlist playlist = new Playlist(name);
		playlists.add(playlist);
		addEntry(new GuiMusicPlayerListEntry(this, playlists, playlist));
	}
	
	public void removePlaylist(GuiMusicPlayerListEntry entry) {
		playlists.remove(entry.getPlaylist());
		removeEntry(entry);
	}
	
	public Playlists getPlaylists() {
		return playlists;
	}
}
