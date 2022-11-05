package info.u_team.music_player.gui.playlist;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;

public class GuiMusicPlaylistListEntryMusicTrack extends GuiMusicPlaylistListEntryFunctions {
	
	private final IAudioTrack track;
	
	public GuiMusicPlaylistListEntryMusicTrack(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack) {
		super(guilist, playlists, playlist, loadedTrack, loadedTrack.getTrack());
		track = loadedTrack.getTrack();
	}
	
	@Override
	public void drawEntryExtended(PoseStack matrixStack, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		addTrackInfo(matrixStack, track, entryX, entryY, entryWidth, 5, isPlaying() ? 0xe02626 : 0x419BF4);
	}
}
