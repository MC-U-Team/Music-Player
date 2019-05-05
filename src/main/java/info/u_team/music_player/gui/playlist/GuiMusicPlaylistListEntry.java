package info.u_team.music_player.gui.playlist;

import java.util.*;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.music_player.util.*;
import info.u_team.to_export_to_u_team_core.gui.*;

abstract class GuiMusicPlaylistListEntry extends GuiScrollableListEntry<GuiMusicPlaylistListEntry> {
	
	protected String trimToWith(String string, int width) {
		String newString = mc.fontRenderer.trimStringToWidth(string, width);
		if (!newString.equals(string)) {
			newString += "...";
		}
		return newString;
	}
	
	protected void addTrackInfo(IAudioTrack track, int entryWidth, int leftMargin, int titleColor) {
		
		int textSize = entryWidth - 150 - leftMargin;
		
		IAudioTrackInfo info = track.getInfo();
		
		String title = trimToWith(info.getTitle(), textSize);
		String author = trimToWith(info.getAuthor(), textSize);
		
		String duration;
		if (info.isStream()) {
			duration = "undefined";
		} else {
			duration = TimeUtil.timeConversion(track.getDuration() / 1000);
		}
		mc.fontRenderer.drawString(title, getX() + leftMargin, getY() + 5, titleColor);
		mc.fontRenderer.drawString(author, getX() + leftMargin + 4, getY() + 25, 0xD86D1C);
		mc.fontRenderer.drawString(duration, getX() + entryWidth - 135, getY() + 5, 0xFFFF00);
	}
	
	static class Loading extends GuiMusicPlaylistListEntry {
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			String text = "Loading tracks ...";
			mc.fontRenderer.drawString(text, getX() + (entryWidth / 2) - (mc.fontRenderer.getStringWidth(text) / 2), getY() + 20, 0xFF0000);
		}
		
	}
	
	private abstract static class Functions extends GuiMusicPlaylistListEntry {
		
		protected final Playlist playlist;
		protected final WrappedObject<String> uri;
		
		protected final GuiButtonClickImage deleteTrackButton;
		protected final GuiButtonClickImage upButton, downButton;
		
		private Functions(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri) {
			this.playlist = playlist;
			this.uri = uri;
			deleteTrackButton = addButton(new GuiButtonClickImage(0, 0, 20, 20, MusicPlayerResources.textureClear));
			upButton = addButton(new GuiButtonClickImage(0, 0, 20, 10, MusicPlayerResources.textureUp));
			downButton = addButton(new GuiButtonClickImage(0, 0, 20, 10, MusicPlayerResources.textureDown));
			
			deleteTrackButton.setClickAction(() -> {
				playlist.remove(uri);
				guilist.updateAllEntries();
			});
			upButton.setClickAction(() -> {
				playlist.move(uri, 1);
				guilist.setSelectedEntryWhenMove(index - 1);
				guilist.updateAllEntries();
			});
			downButton.setClickAction(() -> {
				playlist.move(uri, -1);
				guilist.setSelectedEntryWhenMove(index + 1);
				guilist.updateAllEntries();
			});
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			
			drawEntryExtended(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
			
			deleteTrackButton.x = entryWidth - 20;
			deleteTrackButton.y = getY() + 8;
			deleteTrackButton.render(mouseX, mouseY, partialTicks);
			
			upButton.x = entryWidth - 50;
			upButton.y = getY() + 8;
			upButton.render(mouseX, mouseY, partialTicks);
			
			downButton.x = entryWidth - 50;
			downButton.y = getY() + 18;
			downButton.render(mouseX, mouseY, partialTicks);
		}
		
		public abstract void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks);
		
	}
	
	static class Error extends Functions {
		
		private final String error;
		
		public Error(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri, String error) {
			super(guilist, playlist, uri);
			this.error = error;
		}
		
		@Override
		public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			mc.fontRenderer.drawString(error, getX() + 5, getY() + 5, 0xFF0000);
			mc.fontRenderer.drawString(uri.get(), getX() + 5, getY() + 25, 0xFF0000);
		}
	}
	
	static class MusicTrack extends Functions {
		
		private final IAudioTrack track;
		
		public MusicTrack(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri, IAudioTrack track) {
			super(guilist, playlist, uri);
			this.track = track;
		}
		
		@Override
		public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			addTrackInfo(track, entryWidth, 5, 0x419BF4);
		}
	}
	
	static class PlaylistStart extends Functions {
		
		private final String name;
		private final List<PlaylistTrack> trackEntries;
		
		public PlaylistStart(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri, String name) {
			super(guilist, playlist, uri);
			this.name = name;
			trackEntries = new ArrayList<>();
		}
		
		public void addEntry(PlaylistTrack entry) {
			trackEntries.add(entry);
		}
		
		@Override
		public void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			mc.fontRenderer.drawString(name, getX() + 5, getY() + 15, 0xF4E242);
		}
	}
	
	static class PlaylistTrack extends GuiMusicPlaylistListEntry {
		
		private final PlaylistStart start;
		private final IAudioTrack track;
		
		public PlaylistTrack(PlaylistStart start, IAudioTrack track) {
			this.start = start;
			this.track = track;
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			addTrackInfo(track, entryWidth, 15, 0x42F4F1);
		}
		
	}
}
