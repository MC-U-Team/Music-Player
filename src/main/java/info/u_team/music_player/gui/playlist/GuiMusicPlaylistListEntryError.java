package info.u_team.music_player.gui.playlist;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;

public class GuiMusicPlaylistListEntryError extends GuiMusicPlaylistListEntryFunctions {
	
	private final String error;
	
	public GuiMusicPlaylistListEntryError(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, String error) {
		super(guilist, playlists, playlist, loadedTrack, null);
		this.error = error;
	}
	
	@Override
	public void drawEntryExtended(MatrixStack matrixStack, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		minecraft.fontRenderer.drawString(matrixStack, error, entryX + 5, entryY + 5, 0xFF0000);
		minecraft.fontRenderer.drawString(matrixStack, uri.get(), entryX + 5, entryY + 25, 0xFF0000);
	}
}
