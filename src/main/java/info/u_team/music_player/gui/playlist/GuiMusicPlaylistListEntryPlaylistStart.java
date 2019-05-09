package info.u_team.music_player.gui.playlist;

import java.util.*;

import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.music_player.util.WrappedObject;

public class GuiMusicPlaylistListEntryPlaylistStart extends GuiMusicPlaylistListEntryFunctions {
	
	private final String name;
	private final List<GuiMusicPlaylistListEntryPlaylistTrack> trackEntries;
	
	public GuiMusicPlaylistListEntryPlaylistStart(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri, String name) {
		super(guilist, playlist, uri);
		this.name = name;
		trackEntries = new ArrayList<>();
	}
	
	public void addEntry(GuiMusicPlaylistListEntryPlaylistTrack entry) {
		trackEntries.add(entry);
	}
	
	@Override
	public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		mc.fontRenderer.drawString(name, getX() + 5, getY() + 15, 0xF4E242);
	}
}
