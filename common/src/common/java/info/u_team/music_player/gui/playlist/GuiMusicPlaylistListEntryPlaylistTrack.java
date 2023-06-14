package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;
import net.minecraft.client.gui.GuiGraphics;

public class GuiMusicPlaylistListEntryPlaylistTrack extends GuiMusicPlaylistListEntryPlayable {
	
	private final GuiMusicPlaylistListEntryPlaylistStart start;
	
	private final IAudioTrack track;
	
	public GuiMusicPlaylistListEntryPlaylistTrack(GuiMusicPlaylistListEntryPlaylistStart start, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track) {
		super(playlists, playlist, loadedTrack, track);
		this.start = start;
		this.track = track;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		super.render(guiGraphics, slotIndex, entryY, entryX, entryWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
		addTrackInfo(guiGraphics, track, entryX, entryY, entryWidth, 15, isPlaying() ? 0xe02626 : 0x42F4F1);
	}
	
	public GuiMusicPlaylistListEntryPlaylistStart getStart() {
		return start;
	}
}
