package info.u_team.music_player.gui;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.*;
import info.u_team.to_export_to_u_team_core.gui.*;

public class GuiMusicPlayerListPlaylists extends GuiScrollableList {
	
	private final Playlists playlists;
	
	public GuiMusicPlayerListPlaylists(int width, int height, int top, int bottom, int left, int right) {
		super(width, height, top, bottom, left, right, 50, 20, 5);
		
		playlists = MusicPlayerManager.getPlaylistManager().getPlaylists();
		playlists.forEach(playlist -> addEntry(new GuiMusicPlayerListPlaylistsEntry(playlist)));
	}
	
	public void addPlaylist(String name) {
		Playlist playlist = new Playlist(name);
		playlists.add(playlist);
		addEntry(new GuiMusicPlayerListPlaylistsEntry(playlist));
	}
	
	public void removePlaylist(GuiMusicPlayerListPlaylistsEntry entry) {
		playlists.remove(entry.playlist);
		removeEntry(entry);
	}
	
	@Override
	protected boolean isSelected(int index) {
		return index == selectedElement;
	}
	
	private class GuiMusicPlayerListPlaylistsEntry extends GuiScrollableListEntry {
		
		private final Playlist playlist;
		
		private final GuiButtonExtImage playPlaylistButton;
		private final GuiButtonExtImage openPlaylistButton;
		private final GuiButtonExtImage deletePlaylistButton;
		
		public GuiMusicPlayerListPlaylistsEntry(Playlist playlist) {
			this.playlist = playlist;
			playPlaylistButton = addButton(new GuiButtonExtImage(0, 0, 20, 20, MusicPlayerResources.texturePlay, button -> {
				System.out.println("PLAY");
			}));
			openPlaylistButton = addButton(new GuiButtonExtImage(0, 0, 20, 20, MusicPlayerResources.textureOpen, button -> {
				System.out.println("OPEN");
			}));
			deletePlaylistButton = addButton(new GuiButtonExtImage(0, 0, 20, 20, MusicPlayerResources.textureClear, button -> removePlaylist(this)));
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			String name = playlist.getName();
			if (name.isEmpty()) {
				name = "\u00A7oNo name";
			}
			mc.fontRenderer.drawString(name, getX() + 5, getY() + 5, 0xFFF00F);
			mc.fontRenderer.drawString(playlist.getTrackSize() + " Songs", getX() + 5, getY() + 30, 0xFFFFFF);
			
			playPlaylistButton.x = entryWidth - 80;
			playPlaylistButton.y = getY() + 12;
			playPlaylistButton.render(mouseX, mouseY, partialTicks);
			
			openPlaylistButton.x = entryWidth - 50;
			openPlaylistButton.y = getY() + 12;
			openPlaylistButton.render(mouseX, mouseY, partialTicks);
			
			deletePlaylistButton.x = entryWidth - 20;
			deletePlaylistButton.y = getY() + 12;
			deletePlaylistButton.render(mouseX, mouseY, partialTicks);
		}
	}
}
