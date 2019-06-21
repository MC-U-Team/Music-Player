package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.musicplayer.playlist.*;

public class GuiMusicPlaylistListEntryError extends GuiMusicPlaylistListEntryFunctions {
	
	private final String error;
	
	public GuiMusicPlaylistListEntryError(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, String error) {
		super(guilist, playlists, playlist, loadedTrack, null);
		this.error = error;
	}
	
	@Override
	public void drawEntryExtended(int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		mc.fontRenderer.drawString(error, entryX + 5, entryY + 5, 0xFF0000);
		mc.fontRenderer.drawString(uri.get(), entryX + 5, entryY + 25, 0xFF0000);
	}
}
