package info.u_team.music_player.gui.playlist;

import java.util.function.*;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.*;
import info.u_team.music_player.util.WrappedObject;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImage;

abstract class GuiMusicPlaylistListEntryFunctions extends GuiMusicPlaylistListEntryPlayable {
	
	protected final Playlist playlist;
	protected final WrappedObject<String> uri;
	
	protected final GuiButtonClickImage deleteTrackButton;
	protected final GuiButtonClickImage upButton, downButton;
	
	GuiMusicPlaylistListEntryFunctions(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track) {
		this(guilist, playlists, playlist, loadedTrack, track, play -> {
		});
	}
	
	GuiMusicPlaylistListEntryFunctions(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track, Consumer<Boolean> callback) {
		super(playlists, playlist, loadedTrack, track, callback);
		this.playlist = playlist;
		this.uri = loadedTrack.getUri();
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
		super.drawEntry(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
		drawEntryExtended(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
		
		deleteTrackButton.x = entryWidth - 15;
		deleteTrackButton.y = getY() + 8;
		deleteTrackButton.render(mouseX, mouseY, partialTicks);
		
		upButton.x = entryWidth - 40;
		upButton.y = getY() + 8;
		upButton.render(mouseX, mouseY, partialTicks);
		
		downButton.x = entryWidth - 40;
		downButton.y = getY() + 18;
		downButton.render(mouseX, mouseY, partialTicks);
	}
	
	public abstract void drawEntryExtended(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks);
	
}
