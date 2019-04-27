package info.u_team.music_player.gui.playlist;

import java.util.*;

import info.u_team.music_player.lavaplayer.api.IAudioTrack;
import info.u_team.to_export_to_u_team_core.gui.GuiScrollableListEntry;

abstract class GuiMusicPlaylistListEntry extends GuiScrollableListEntry<GuiMusicPlaylistListEntry> {
	
	static class MusicTrack extends GuiMusicPlaylistListEntry {
		
		private final IAudioTrack track;
		
		public MusicTrack(IAudioTrack track) {
			this.track = track;
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			
		}
	}
	
	static class PlaylistTrack extends MusicTrack {
		
		public PlaylistTrack(IAudioTrack track) {
			super(track);
		}
		
	}
	
	static class PlaylistStart extends GuiMusicPlaylistListEntry {
		
		private final String name;
		
		private final List<PlaylistTrack> trackEntries;
		
		public PlaylistStart(String name) {
			this.name = name;
			trackEntries = new ArrayList<>();
		}
		
		public void addEntry(PlaylistTrack entry) {
			trackEntries.add(entry);
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			
		}
	}
	
	static class Error extends GuiMusicPlaylistListEntry {
		
		private final String uri;
		private final String error;
		
		public Error(String uri, String error) {
			super();
			this.uri = uri;
			this.error = error;
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			
		}
	}
}
