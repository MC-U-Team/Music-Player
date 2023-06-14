package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;
import net.minecraft.client.gui.GuiGraphics;

public class GuiMusicPlaylistListEntryMusicTrack extends GuiMusicPlaylistListEntryFunctions {
	
	private final IAudioTrack track;
	
	public GuiMusicPlaylistListEntryMusicTrack(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack) {
		super(guilist, playlists, playlist, loadedTrack, loadedTrack.getTrack());
		track = loadedTrack.getTrack();
	}
	
	@Override
	public void drawEntryExtended(GuiGraphics guiGraphics, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		addTrackInfo(guiGraphics, track, entryX, entryY, entryWidth, 5, isPlaying() ? 0xe02626 : 0x419BF4);
	}
}
