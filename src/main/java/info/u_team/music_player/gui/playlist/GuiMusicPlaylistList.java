package info.u_team.music_player.gui.playlist;

import java.util.*;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.u_team_core.gui.elements.GuiScrollableList;

public class GuiMusicPlaylistList extends GuiScrollableList<GuiMusicPlaylistListEntry> {

	private final Playlist playlist;

	private boolean tracksLoaded;

	private boolean canSelectNext;

	public GuiMusicPlaylistList(Playlist playlist) {
		super(0, 0, 0, 0, 0, 0, 40, 20, 5);
		this.playlist = playlist;
		canSelectNext = true;
		addEntry(new GuiMusicPlaylistListEntryLoading());
	}

	private void addLoadedTrackToGui(LoadedTracks loadedTracks) {
		final Playlists playlists = MusicPlayerManager.getPlaylistManager().getPlaylists();
		final List<GuiMusicPlaylistListEntry> list = new ArrayList<>();
		if (loadedTracks.hasError()) {// Add error gui element
			list.add(new GuiMusicPlaylistListEntryError(this, playlists, playlist, loadedTracks, loadedTracks.getErrorMessage()));
		} else if (loadedTracks.isTrack()) { // Add track gui element
			list.add(new GuiMusicPlaylistListEntryMusicTrack(this, playlists, playlist, loadedTracks));
		} else { // Add playlist start element and all track sub elements
			GuiMusicPlaylistListEntryPlaylistStart start = new GuiMusicPlaylistListEntryPlaylistStart(this, playlists, playlist, loadedTracks);
			list.add(start);
			loadedTracks.getTrackList().getTracks().forEach(track -> {
				GuiMusicPlaylistListEntryPlaylistTrack entry = new GuiMusicPlaylistListEntryPlaylistTrack(start, playlists, playlist, loadedTracks, track);
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

	@Override
	protected boolean isSelected(int index) {
		return index == selectedElement;
	}

	public void setSelectedEntryWhenMove(int index) {
		if (index >= 0 || index < getSize()) {
			super.setSelectedEntry(index);
			canSelectNext = false;
		}
	}

	@Override
	public void setSelectedEntry(int index) {
		if (canSelectNext) {
			super.setSelectedEntry(index);
		} else {
			canSelectNext = true;
		}
	}

	public void tick() {
		getChildren().forEach(GuiMusicPlaylistListEntry::tick);
	}

}
