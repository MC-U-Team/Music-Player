package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImageToggle;
import net.minecraft.client.Minecraft;

public abstract class GuiMusicPlaylistListEntryPlayable extends GuiMusicPlaylistListEntry {
	
	private final ITrackManager manager;
	private final IAudioTrack track;
	
	private final LoadedTracks loadedTrack;
	
	protected final GuiButtonClickImageToggle playTrackButton;
	
	GuiMusicPlaylistListEntryPlayable(Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track) {
		this.track = track;
		this.loadedTrack = loadedTrack;
		manager = MusicPlayerManager.getPlayer().getTrackManager();
		
		playTrackButton = addButton(new GuiButtonClickImageToggle(0, 0, 20, 20, MusicPlayerResources.texturePlay, MusicPlayerResources.texturePause));
		
		if (loadedTrack.hasError() || track == null) {
			playTrackButton.visible = false;
		} else {
			playTrackButton.toggle(track == getCurrentlyPlaying());
			playTrackButton.setToggleClickAction((play) -> {
				if (play) {
					if (manager.isPaused() && getCurrentlyPlaying() == track) {
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
			});
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 2) {
			final String uri = this instanceof GuiMusicPlaylistListEntryPlaylistStart || this instanceof GuiMusicPlaylistListEntryError ? loadedTrack.getUri().get() : track.getInfo().getURI();
			if (GuiTrackUtils.openURI(uri)) {
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		playTrackButton.x = entryWidth - 65;
		playTrackButton.y = getY() + 8;
		playTrackButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void tick() {
		if (isPlaying()) {
			playTrackButton.toggle(!manager.isPaused());
		} else {
			playTrackButton.toggle(false);
		}
	}
	
	protected IAudioTrack getCurrentlyPlaying() {
		return manager.getCurrentTrack() == null ? null : manager.getCurrentTrack().getOriginalTrack();
	}
	
	protected boolean isPlaying() {
		return getCurrentlyPlaying() == track;
	}
	
	public IAudioTrack getTrack() {
		return track;
	}
	
	public GuiButtonClickImageToggle getPlayTrackButton() {
		return playTrackButton;
	}
}
