package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.apache.commons.lang3.tuple.Pair;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylist;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;

class GuiMusicPlayerListEntry extends GuiScrollableListEntry<GuiMusicPlayerListEntry> {
	
	private final Playlists playlists;
	private final Playlist playlist;
	
	private final GuiButtonClickImageToggle playPlaylistButton;
	private final GuiButtonClickImage openPlaylistButton;
	private final GuiButtonClickImage deletePlaylistButton;
	
	public GuiMusicPlayerListEntry(GuiMusicPlayerList gui, Playlists playlists, Playlist playlist) {
		this.playlists = playlists;
		this.playlist = playlist;
		
		playPlaylistButton = addButton(new GuiButtonClickImageToggle(0, 0, 20, 20, MusicPlayerResources.texturePlay, MusicPlayerResources.textureStop));
		playPlaylistButton.toggle(playlist.equals(playlists.getPlaying()));
		playPlaylistButton.enabled = !playlists.isPlayingLock();
		
		playPlaylistButton.setToggleClickAction((play) -> {
			if (playlists.isPlayingLock()) {
				return;
			}
			playlists.setPlaying(null);
			gui.getChildren().stream().filter(entry -> entry != this).forEach(entry -> entry.playPlaylistButton.toggle(false)); // Reset all playlist buttons except this one
			
			final Runnable runnable = () -> {
				final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
				
				// Start playlist
				if (play) {
					if (!playlist.isEmpty()) {
						playlists.setPlaying(playlist);
						Pair<LoadedTracks, IAudioTrack> pair = playlist.getFirstTrack();
						playlist.setPlayable(pair.getLeft(), pair.getRight());
						manager.setTrackQueue(playlist);
						manager.start();
					} else {
						playlists.setPlaying(null);
						playlist.setStopable();
						manager.stop();
						playPlaylistButton.toggle(false);
					}
				} else {
					playlists.setPlaying(null);
					playlist.setStopable();
					manager.stop();
				}
				
				playlists.removePlayingLock();
				
				if (mc.currentScreen instanceof GuiMusicPlayer) {
					final GuiMusicPlayer musicplayergui = (GuiMusicPlayer) mc.currentScreen;
					final GuiMusicPlayerList newGui = musicplayergui.getPlaylistsList();
					newGui.getChildren().forEach(entry -> entry.playPlaylistButton.enabled = true);
				} else if (mc.currentScreen instanceof GuiMusicPlaylist) {
					final GuiMusicPlaylist musicplaylistgui = (GuiMusicPlaylist) mc.currentScreen;
					musicplaylistgui.getTrackList().updateAllEntries();
				}
			};
			
			gui.getChildren().forEach(entry -> entry.playPlaylistButton.enabled = false);
			playlists.setPlayingLock();
			
			if (!playlist.isLoaded()) {
				playlist.load(runnable);
			} else {
				runnable.run();
			}
		});
		
		openPlaylistButton = addButton(new GuiButtonClickImage(0, 0, 20, 20, MusicPlayerResources.textureOpen));
		openPlaylistButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicPlaylist(playlist)));
		
		deletePlaylistButton = addButton(new GuiButtonClickImage(0, 0, 20, 20, MusicPlayerResources.textureClear));
		deletePlaylistButton.setClickAction(() -> gui.removePlaylist(this));
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		String name = playlist.getName();
		if (name.isEmpty()) {
			name = "\u00A7o" + getTranslation(gui_playlists_no_name);
		}
		mc.fontRendererObj.drawString(name, getX() + 5, getY() + 5, playlist.equals(playlists.getPlaying()) ? 0x0083FF : 0xFFF00F);
		mc.fontRendererObj.drawString(playlist.getEntrySize() + " " + getTranslation(playlist.getEntrySize() > 1 ? gui_playlists_entries : gui_playlists_entry), getX() + 5, getY() + 30, 0xFFFFFF);
		
		playPlaylistButton.xPosition = entryWidth - 65;
		playPlaylistButton.yPosition = getY() + 12;
		playPlaylistButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		
		openPlaylistButton.xPosition = entryWidth - 40;
		openPlaylistButton.yPosition = getY() + 12;
		openPlaylistButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		
		deletePlaylistButton.xPosition = entryWidth - 15;
		deletePlaylistButton.yPosition = getY() + 12;
		deletePlaylistButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
	}
	
	public Playlist getPlaylist() {
		return playlist;
	}
}