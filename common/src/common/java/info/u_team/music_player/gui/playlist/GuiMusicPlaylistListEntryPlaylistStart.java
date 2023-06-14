package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_TRACK_DURATION_UNDEFINED;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import java.util.ArrayList;
import java.util.List;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.client.gui.GuiGraphics;

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
			duration = getTranslation(GUI_TRACK_DURATION_UNDEFINED);
		}
		
		entries = new ArrayList<>();
	}
	
	@Override
	public void drawEntryExtended(GuiGraphics guiGraphics, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		guiGraphics.drawString(minecraft.font, name, entryX + 5, entryY + 15, 0xF4E242, false);
		guiGraphics.drawString(minecraft.font, duration, entryX + entryWidth - 140, entryY + 15, 0xFFFF00, false);
	}
	
	public void addEntry(GuiMusicPlaylistListEntryPlaylistTrack entry) {
		entries.add(entry);
	}
	
	@Override
	protected boolean isPlaying() {
		return entries.stream().anyMatch(entry -> entry.getStart() == this && entry.getTrack() == getCurrentlyPlaying());
	}
}
