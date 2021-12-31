package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLISTS_ENTRIES;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLISTS_ENTRY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLISTS_NO_NAME;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylist;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;
import info.u_team.music_player.musicplayer.playlist.Skip;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.ImageToggleButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

class GuiMusicPlayerListEntry extends BetterScrollableListEntry<GuiMusicPlayerListEntry> {
	
	private final Playlists playlists;
	private final Playlist playlist;
	
	private final ImageToggleButton playPlaylistButton;
	private final ImageButton openPlaylistButton;
	private final ImageButton deletePlaylistButton;
	
	public GuiMusicPlayerListEntry(GuiMusicPlayerList gui, Playlists playlists, Playlist playlist) {
		this.playlists = playlists;
		this.playlist = playlist;
		
		playPlaylistButton = addChildren(new ImageToggleButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_PLAY, MusicPlayerResources.TEXTURE_STOP, playlist.equals(playlists.getPlaying())));
		playPlaylistButton.active = !playlists.isPlayingLock();
		
		playPlaylistButton.setPressable(() -> {
			final boolean play = playPlaylistButton.isToggled();
			if (playlists.isPlayingLock()) {
				return;
			}
			playlists.setPlaying(null);
			gui.children().stream().filter(entry -> entry != this).forEach(entry -> entry.playPlaylistButton.setToggled(false)); // Reset all playlist buttons except this one
			
			final Runnable runnable = () -> {
				final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
				
				// Start playlist
				if (play) {
					if (!playlist.isEmpty()) {
						playlists.setPlaying(playlist);
						final Pair<LoadedTracks, IAudioTrack> pair = playlist.getFirstTrack();
						playlist.setPlayable(pair.getLeft(), pair.getRight());
						if (pair.getLeft().hasError() || pair.getRight() == null) {
							playlist.skip(Skip.FORWARD);
						}
						manager.setTrackQueue(playlist);
						manager.start();
					} else {
						playlists.setPlaying(null);
						playlist.setStopable();
						manager.stop();
						playPlaylistButton.setToggled(false);
					}
				} else {
					playlists.setPlaying(null);
					playlist.setStopable();
					manager.stop();
				}
				
				playlists.removePlayingLock();
				
				if (minecraft.screen instanceof GuiMusicPlayer musicplayergui) {
					final GuiMusicPlayerList newGui = musicplayergui.getPlaylistsList();
					newGui.children().forEach(entry -> entry.playPlaylistButton.active = true);
				} else if (minecraft.screen instanceof GuiMusicPlaylist musicplaylistgui) {
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
		
		openPlaylistButton = addChildren(new ImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_OPEN));
		openPlaylistButton.setPressable(() -> minecraft.setScreen(new GuiMusicPlaylist(playlist)));
		
		deletePlaylistButton = addChildren(new ImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_CLEAR));
		deletePlaylistButton.setPressable(() -> gui.removePlaylist(this));
	}
	
	@Override
	public void render(PoseStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		String name = playlist.getName();
		if (name.isEmpty()) {
			name = "\u00A7o" + getTranslation(GUI_PLAYLISTS_NO_NAME);
		}
		minecraft.font.draw(matrixStack, name, entryX + 5, entryY + 5, playlist.equals(playlists.getPlaying()) ? 0x0083FF : 0xFFF00F);
		minecraft.font.draw(matrixStack, playlist.getEntrySize() + " " + getTranslation(playlist.getEntrySize() > 1 ? GUI_PLAYLISTS_ENTRIES : GUI_PLAYLISTS_ENTRY), entryX + 5, entryY + 30, 0xFFFFFF);
		
		playPlaylistButton.x = entryWidth - 65;
		playPlaylistButton.y = entryY + 12;
		playPlaylistButton.render(matrixStack, mouseX, mouseY, partialTicks);
		
		openPlaylistButton.x = entryWidth - 40;
		openPlaylistButton.y = entryY + 12;
		openPlaylistButton.render(matrixStack, mouseX, mouseY, partialTicks);
		
		deletePlaylistButton.x = entryWidth - 15;
		deletePlaylistButton.y = entryY + 12;
		deletePlaylistButton.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public Playlist getPlaylist() {
		return playlist;
	}
	
	@Override
	public Component getNarration() {
		return TextComponent.EMPTY;
	}
}