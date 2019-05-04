package info.u_team.music_player.gui.playlist;

import java.util.*;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylistListEntry.*;
import info.u_team.music_player.gui.playlist.GuiMusicPlaylistListEntry.Error;
import info.u_team.music_player.musicplayer.*;
import info.u_team.music_player.util.WrappedObject;
import info.u_team.to_export_to_u_team_core.gui.GuiScrollableList;

public class GuiMusicPlaylistList extends GuiScrollableList<GuiMusicPlaylistListEntry> {
	
	private final Playlist playlist;
	
	public GuiMusicPlaylistList(Playlist playlist, int width, int height, int top, int bottom, int left, int right) {
		super(width, height, top, bottom, left, right, 40, 20, 5);
		this.playlist = playlist;
		
		if (!playlist.isLoaded()) {
			playlist.load();
		} else {
			playlist.getLoadedTracks().forEach(this::addLoadedTrackToGui);
		}
	}
	
	/**
	 * This is used to asynchronically add the loaded tracks to our gui when they
	 * are loaded
	 */
	public void tick() {
		Queue<LoadedTracks> queue = playlist.getLoadQueue();
		for (LoadedTracks tracks = queue.poll(); tracks != null; tracks = queue.poll()) { // Remove all elements in the queue and add them to out gui
			addLoadedTrackToGui(tracks);
		}
	}
	
	/**
	 * Add a {@link LoadedTracks} to the gui list
	 * 
	 * @param tracks
	 *            Loaded Track
	 */
	private void addLoadedTrackToGui(LoadedTracks tracks) {
		List<GuiMusicPlaylistListEntry> list = new ArrayList<>();
		if (tracks.hasError()) {// Add error gui element
			list.add(new Error(playlist, tracks.getUri(), tracks.getErrorMessage()));
		} else if (tracks.isTrack()) { // Add track gui element
			list.add(new MusicTrack(playlist, tracks.getUri(), tracks.getTrack()));
		} else { // Add playlist start element and all track sub elements
			PlaylistStart start = new PlaylistStart(playlist, tracks.getUri(), tracks.getTitle());
			list.add(start);
			tracks.getTrackList().getTracks().forEach(track -> {
				PlaylistTrack trackentry = new PlaylistTrack(start, track);
				start.addEntry(trackentry);
				list.add(trackentry);
			});
		}
		list.forEach(this::addEntry);
	}
	
	@Override
	protected boolean isSelected(int index) {
		return index == selectedElement;
	}
	
}
