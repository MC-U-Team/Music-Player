package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.music_player.util.WrappedObject;

public class GuiMusicPlaylistListEntryError extends GuiMusicPlaylistListEntryFunctions {
	
	private final String error;
	
	public GuiMusicPlaylistListEntryError(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri, String error) {
		super(guilist, playlist, uri);
		this.error = error;
	}
	
	@Override
	public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		mc.fontRenderer.drawString(error, getX() + 5, getY() + 5, 0xFF0000);
		mc.fontRenderer.drawString(uri.get(), getX() + 5, getY() + 25, 0xFF0000);
	}
}
