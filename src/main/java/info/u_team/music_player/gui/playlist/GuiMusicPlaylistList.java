package info.u_team.music_player.gui.playlist;

import java.util.ArrayList;
import java.util.List;

import info.u_team.music_player.gui.BetterScrollableList;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;

public class GuiMusicPlaylistList extends BetterScrollableList<GuiMusicPlaylistListEntry> {
	
	private final Playlist playlist;
	
	private boolean tracksLoaded;
	
	private int selectIndex;
	
	public GuiMusicPlaylistList(Playlist playlist) {
		super(0, 0, 0, 0, 0, 0, 40, 20);
		this.playlist = playlist;
		addEntry(new GuiMusicPlaylistListEntryLoading());
	}
	
	private void addLoadedTrackToGui(LoadedTracks loadedTracks) {
		final Playlists playlists = MusicPlayerManager.getPlaylistManager().getPlaylists();
		final List<GuiMusicPlaylistListEntry> list = new ArrayList<>();
		if (loadedTracks.hasError()) {// Add error gui element
			list.add(new GuiMusicPlaylistListEntryError(this, playlists, playlist, loadedTracks, loadedTracks.getErrorMessage()));
		} else if (loadedTracks.isTrack()) { // Add track gui element
			list.add(new GuiMusicPlaylistListEntryMusicTrack(this, playlists, playlist, loadedTracks));
		} else if (loadedTracks.isTrackList()) { // Add playlist start element and all track sub elements
			final GuiMusicPlaylistListEntryPlaylistStart start = new GuiMusicPlaylistListEntryPlaylistStart(this, playlists, playlist, loadedTracks);
			list.add(start);
			loadedTracks.getTrackList().getTracks().forEach(track -> {
				final GuiMusicPlaylistListEntryPlaylistTrack entry = new GuiMusicPlaylistListEntryPlaylistTrack(start, playlists, playlist, loadedTracks, track);
				start.addEntry(entry);
				list.add(entry);
			});
		}
		list.forEach(this::addEntry);
	}
	
	public void addAllEntries() {
		if (!playlist.isLoaded()) {
			return;
		}
		if (!tracksLoaded) {
			clearEntries();
			playlist.getLoadedTracks().forEach(this::addLoadedTrackToGui);
			tracksLoaded = true;
		}
	}
	
	public void removeAllEntries() {
		clearEntries();
		tracksLoaded = false;
	}
	
	public void updateAllEntries() {
		removeAllEntries();
		addAllEntries();
	}
	
	public void setSelectedEntryWhenMove(GuiMusicPlaylistListEntry entry, int indexOffset) {
		final int index = children().lastIndexOf(entry) + indexOffset;
		if (index >= 0 && index < children().size()) {
			selectIndex = index;
		}
	}
	
	@Override
	protected boolean isSelectedItem(int index) {
		return index == selectIndex;
	}
	
	@Override
	public void setSelected(GuiMusicPlaylistListEntry entry) {
		if (entry != null) {
			selectIndex = children().indexOf(entry);
		}
		super.setSelected(entry);
	}
	
	public void tick() {
		children().forEach(GuiMusicPlaylistListEntry::tick);
	}
}