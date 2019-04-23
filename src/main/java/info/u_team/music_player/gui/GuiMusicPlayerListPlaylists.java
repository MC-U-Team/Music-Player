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
		
		private final GuiButtonExtImage openPlaylistButton;
		private final GuiButtonExtImage deletePlaylistButton;
		
		public GuiMusicPlayerListPlaylistsEntry(Playlist playlist) {
			this.playlist = playlist;
			openPlaylistButton = new GuiButtonExtImage(0, 0, 20, 20, MusicPlayerResources.textureOpen, button -> {
			});
			deletePlaylistButton = new GuiButtonExtImage(0, 0, 20, 20, MusicPlayerResources.textureClear, button -> removePlaylist(this));
		}
		
		@Override
		public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
			String name = playlist.getName();
			if (name.isEmpty()) {
				name = "\u00A7oNo name";
			}
			mc.fontRenderer.drawString(name, getX(), getY(), 0xFFF00F);
			mc.fontRenderer.drawString(playlist.getTrackSize() + " Songs", getX() + 5, getY() + 25, 0xFFFFFF);
			
			openPlaylistButton.x = entryWidth - 50;
			openPlaylistButton.y = getY() + 20;
			openPlaylistButton.render(mouseX, mouseY, partialTicks);
			
			deletePlaylistButton.x = entryWidth - 20;
			deletePlaylistButton.y = getY() + 20;
			deletePlaylistButton.render(mouseX, mouseY, partialTicks);
		}
		
		@Override
		public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
			boolean flag = openPlaylistButton.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
			if (!flag) {
				return deletePlaylistButton.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
			}
			return true;
		}
		
		@Override
		public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
			boolean flag = openPlaylistButton.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
			if (!flag) {
				return deletePlaylistButton.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
			}
			return true;
		}
		
		@Override
		public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
			boolean flag = openPlaylistButton.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
			if (!flag) {
				return deletePlaylistButton.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
			}
			return true;
		}
		
	}
	
}
