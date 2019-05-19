package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.util.TimeUtil;

public class GuiMusicPlaylistListEntryPlaylistStart extends GuiMusicPlaylistListEntryFunctions {
	
	private final String name;
	private final String duration;
	
	private final List<GuiMusicPlaylistListEntryPlaylistTrack> entries;
	
	public GuiMusicPlaylistListEntryPlaylistStart(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTracks) {
		super(guilist, playlists, playlist, loadedTracks, loadedTracks.getFirstTrack());
		name = loadedTracks.getTitle();
		
		final List<IAudioTrack> tracks = loadedTracks.getTrackList().getTracks();
		
		if (!tracks.parallelStream().anyMatch(track -> track.getInfo().isStream())) {
			duration = TimeUtil.timeConversion(tracks.parallelStream().mapToLong(track -> track.getDuration()).sum() / 1000);
		} else {
			duration = getTranslation(gui_track_duration_undefined);
		}
		
		entries = new ArrayList<>();
	}
	
	@Override
	public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		mc.fontRenderer.drawString(name, getX() + 5, getY() + 15, 0xF4E242);
		mc.fontRenderer.drawString(duration, getX() + entryWidth - 140, getY() + 15, 0xFFFF00);
	}
	
	public void addEntry(GuiMusicPlaylistListEntryPlaylistTrack entry) {
		entries.add(entry);
	}
	
	@Override
	protected boolean isPlaying() {
		return entries.stream().anyMatch(entry -> entry.getStart() == this && entry.getTrack() == getCurrentlyPlaying());
	}
}
