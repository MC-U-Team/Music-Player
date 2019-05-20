package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.*;

public class GuiMusicPlaylistListEntryPlaylistTrack extends GuiMusicPlaylistListEntryPlayable {

	private final GuiMusicPlaylistListEntryPlaylistStart start;

	private final IAudioTrack track;

	public GuiMusicPlaylistListEntryPlaylistTrack(GuiMusicPlaylistListEntryPlaylistStart start, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track) {
		super(playlists, playlist, loadedTrack, track);
		this.start = start;
		this.track = track;
	}

	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		super.drawEntry(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
		addTrackInfo(track, entryWidth, 15, isPlaying() ? 0xe02626 : 0x42F4F1);
	}

	public GuiMusicPlaylistListEntryPlaylistStart getStart() {
		return start;
	}
}
