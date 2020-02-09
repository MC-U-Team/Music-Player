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

class GuiMusicPlayerListEntry extends BetterScrollableListEntry<GuiMusicPlayerListEntry> {
	
	private final Playlists playlists;
	private final Playlist playlist;
	
	private final ToggleImageButton playPlaylistButton;
	private final ImageButton openPlaylistButton;
	private final ImageButton deletePlaylistButton;
	
	public GuiMusicPlayerListEntry(GuiMusicPlayerList gui, Playlists playlists, Playlist playlist) {
		this.playlists = playlists;
		this.playlist = playlist;
		
		playPlaylistButton = addButton(new ToggleImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_PLAY, MusicPlayerResources.TEXTURE_STOP));
		playPlaylistButton.toggle(playlist.equals(playlists.getPlaying()));
		playPlaylistButton.active = !playlists.isPlayingLock();
		
		playPlaylistButton.setPressable(() -> {
			final boolean play = playPlaylistButton.isToggled();
			if (playlists.isPlayingLock()) {
				return;
			}
			playlists.setPlaying(null);
			gui.children().stream().filter(entry -> entry != this).forEach(entry -> entry.playPlaylistButton.toggle(false)); // Reset all playlist buttons except this one
			
			final Runnable runnable = () -> {
				final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
				
				// Start playlist
				if (play) {
					if (!playlist.isEmpty()) {
						playlists.setPlaying(playlist);
						final Pair<LoadedTracks, IAudioTrack> pair = playlist.getFirstTrack();
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
				
				if (minecraft.currentScreen instanceof GuiMusicPlayer) {
					final GuiMusicPlayer musicplayergui = (GuiMusicPlayer) minecraft.currentScreen;
					final GuiMusicPlayerList newGui = musicplayergui.getPlaylistsList();
					newGui.children().forEach(entry -> entry.playPlaylistButton.active = true);
				} else if (minecraft.currentScreen instanceof GuiMusicPlaylist) {
					final GuiMusicPlaylist musicplaylistgui = (GuiMusicPlaylist) minecraft.currentScreen;
					musicplaylistgui.getTrackList().updateAllEntries();
				}
			};
			
			gui.children().forEach(entry -> entry.playPlaylistButton.active = false);
			playlists.setPlayingLock();
			
			if (!playlist.isLoaded()) {
				playlist.load(runnable);
			} else {
				runnable.run();
			}
		});
		
		openPlaylistButton = addButton(new ImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_OPEN));
		openPlaylistButton.setPressable(() -> minecraft.displayGuiScreen(new GuiMusicPlaylist(playlist)));
		
		deletePlaylistButton = addButton(new ImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_CLEAR));
		deletePlaylistButton.setPressable(() -> gui.removePlaylist(this));
	}
	
	@Override
	public void render(int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		String name = playlist.getName();
		if (name.isEmpty()) {
			name = "\u00A7o" + getTranslation(GUI_PLAYLISTS_NO_NAME);
		}
		minecraft.fontRenderer.drawString(name, entryX + 5, entryY + 5, playlist.equals(playlists.getPlaying()) ? 0x0083FF : 0xFFF00F);
		minecraft.fontRenderer.drawString(playlist.getEntrySize() + " " + getTranslation(playlist.getEntrySize() > 1 ? GUI_PLAYLISTS_ENTRIES : GUI_PLAYLISTS_ENTRY), entryX + 5, entryY + 30, 0xFFFFFF);
		
		playPlaylistButton.x = entryWidth - 65;
		playPlaylistButton.y = entryY + 12;
		playPlaylistButton.render(mouseX, mouseY, partialTicks);
		
		openPlaylistButton.x = entryWidth - 40;
		openPlaylistButton.y = entryY + 12;
		openPlaylistButton.render(mouseX, mouseY, partialTicks);
		
		deletePlaylistButton.x = entryWidth - 15;
		deletePlaylistButton.y = entryY + 12;
		deletePlaylistButton.render(mouseX, mouseY, partialTicks);
	}
	
	public Playlist getPlaylist() {
		return playlist;
	}
}