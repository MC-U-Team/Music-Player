package info.u_team.music_player.gui.playlist;

import java.util.*;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylistListEntry.*;
import info.u_team.music_player.gui.playlist.GuiMusicPlaylistListEntry.Error;
import info.u_team.music_player.musicplayer.*;
import info.u_team.to_export_to_u_team_core.gui.GuiScrollableList;

public class GuiMusicPlaylistList extends GuiScrollableList<GuiMusicPlaylistListEntry> {
	
	private final Playlist playlist;
	
	public GuiMusicPlaylistList(Playlist playlist, int width, int height, int top, int bottom, int left, int right) {
		super(width, height, top, bottom, left, right, 25, 20, 5);
		this.playlist = playlist;
	}
	
	public void tick() {
		for (LoadedTracks tracks = playlist.getLoadQueue().poll(); tracks != null;) { // Runs this iteration as long as the queue has elements
			List<GuiMusicPlaylistListEntry> list = new ArrayList<>();
			if (tracks.hasError() || tracks.size() < 1) {
				list.add(new Error(tracks.getUri(), tracks.getName()));
			} else if (tracks.size() == 1) {
				list.add(new MusicTrack(tracks.getTracks().get(0)));
			} else {
				PlaylistStart start = new PlaylistStart(tracks.getName());
				list.add(start);
				tracks.getTracks().forEach(track -> {
					PlaylistTrack trackentry = new PlaylistTrack(track);
					start.addEntry(trackentry);
					list.add(start);
				});
			}
			list.forEach(this::addEntry);
		}
	}
	
}
