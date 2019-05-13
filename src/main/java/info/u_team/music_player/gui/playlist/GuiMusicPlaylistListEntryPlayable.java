package info.u_team.music_player.gui.playlist;

import java.util.function.*;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImageToggle;

public abstract class GuiMusicPlaylistListEntryPlayable extends GuiMusicPlaylistListEntry {
	
	private final ITrackManager manager;
	private final IAudioTrack track;
	
	protected final GuiButtonClickImageToggle playTrackButton;
	
	GuiMusicPlaylistListEntryPlayable(Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track) {
		this(playlists, playlist, loadedTrack, track, play -> {
		});
	}
	
	GuiMusicPlaylistListEntryPlayable(Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track, Consumer<Boolean> callback) {
		this.track = track;
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		playTrackButton = addButton(new GuiButtonClickImageToggle(0, 0, 20, 20, MusicPlayerResources.texturePlay, MusicPlayerResources.texturePause));
		
		if (loadedTrack.hasError() || track == null) {
			playTrackButton.visible = false;
		} else {
			IAudioTrack currentlyPlaying = getCurrentlyPlaying();
			
			playTrackButton.toggle(track == currentlyPlaying);
			playTrackButton.setToggleClickAction((play) -> {
				getList().getChildren().stream().filter(entry -> entry instanceof GuiMusicPlaylistListEntryPlayable).filter(entry -> entry != this).map(entry -> (GuiMusicPlaylistListEntryPlayable) entry).forEach(entry -> entry.playTrackButton.toggle(false));
				if (play) {
					if (manager.isPaused() && currentlyPlaying == track) {
						manager.setPaused(false);
					} else {
						playlists.setPlaying(playlist);
						playlist.setPlayable(loadedTrack, track);
						manager.setTrackQueue(playlist);
						manager.start();
					}
				} else {
					manager.setPaused(true);
				}
				callback.accept(play);
			});
		}
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		playTrackButton.x = entryWidth - 65;
		playTrackButton.y = getY() + 8;
		playTrackButton.render(mouseX, mouseY, partialTicks);
	}
	
	private IAudioTrack getCurrentlyPlaying() {
		return manager.getCurrentTrack() == null ? null : manager.getCurrentTrack().getOriginalTrack();
	}
	
	protected final boolean isPlaying() {
		return getCurrentlyPlaying() == track;
	}
	
	public IAudioTrack getTrack() {
		return track;
	}
	
	public GuiButtonClickImageToggle getPlayTrackButton() {
		return playTrackButton;
	}
}
