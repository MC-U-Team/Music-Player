package info.u_team.music_player.gui.playlist;

import java.util.List;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.util.TimeUtil;

public class GuiMusicPlaylistListEntryPlaylistStart extends GuiMusicPlaylistListEntryFunctions {
	
	private final String name;
	private final String duration;
	
	public GuiMusicPlaylistListEntryPlaylistStart(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTracks) {
		super(guilist, playlists, playlist, loadedTracks, loadedTracks.getFirstTrack(), play -> {
			guilist.getChildren().stream() //
					.filter(entry -> entry instanceof GuiMusicPlaylistListEntryPlaylistTrack) //
					.map(entry -> (GuiMusicPlaylistListEntryPlaylistTrack) entry) //
					.filter(entry -> entry.getTrack() == loadedTracks.getFirstTrack()) //
					.findFirst() //
					.ifPresent(entry -> {
						entry.getPlayTrackButton().toggle(play);
					});
		});
		name = loadedTracks.getTitle();
		
		final List<IAudioTrack> tracks = loadedTracks.getTrackList().getTracks();
		
		if (!tracks.parallelStream().anyMatch(track -> track.getInfo().isStream())) {
			duration = TimeUtil.timeConversion(tracks.parallelStream().mapToLong(track -> track.getDuration()).sum() / 1000);
		} else {
			duration = "undefined";
		}
	}
	
	@Override
	public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		mc.fontRenderer.drawString(name, getX() + 5, getY() + 15, 0xF4E242);
		mc.fontRenderer.drawString(duration, getX() + entryWidth - 135, getY() + 5, 0xFFFF00);
	}
}
