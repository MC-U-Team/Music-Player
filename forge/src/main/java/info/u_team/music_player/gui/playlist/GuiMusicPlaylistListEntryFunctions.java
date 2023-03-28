package info.u_team.music_player.gui.playlist;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;
import info.u_team.music_player.util.WrappedObject;
import info.u_team.u_team_core.gui.elements.ImageButton;

abstract class GuiMusicPlaylistListEntryFunctions extends GuiMusicPlaylistListEntryPlayable {
	
	protected final Playlist playlist;
	protected final WrappedObject<String> uri;
	
	protected final ImageButton deleteTrackButton;
	protected final ImageButton upButton, downButton;
	
	GuiMusicPlaylistListEntryFunctions(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, IAudioTrack track) {
		super(playlists, playlist, loadedTrack, track);
		this.playlist = playlist;
		uri = loadedTrack.getUri();
		deleteTrackButton = addChildren(new ImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_CLEAR));
		upButton = addChildren(new ImageButton(0, 0, 20, 10, MusicPlayerResources.TEXTURE_UP));
		downButton = addChildren(new ImageButton(0, 0, 20, 10, MusicPlayerResources.TEXTURE_DOWN));
		
		deleteTrackButton.setPressable(() -> {
			playlist.remove(uri);
			guilist.updateAllEntries();
		});
		upButton.setPressable(() -> {
			playlist.move(uri, 1);
			
			guilist.setSelectedEntryWhenMove(this, -1);
			guilist.updateAllEntries();
		});
		downButton.setPressable(() -> {
			playlist.move(uri, -1);
			guilist.setSelectedEntryWhenMove(this, 1);
			guilist.updateAllEntries();
		});
	}
	
	@Override
	public void render(PoseStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		super.render(matrixStack, slotIndex, entryY, entryX, entryWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
		drawEntryExtended(matrixStack, entryX, entryY, entryWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
		
		deleteTrackButton.setX(entryWidth - 15);
		deleteTrackButton.setY(entryY + 8);
		deleteTrackButton.render(matrixStack, mouseX, mouseY, partialTicks);
		
		upButton.setX(entryWidth - 40);
		upButton.setY(entryY + 8);
		upButton.render(matrixStack, mouseX, mouseY, partialTicks);
		
		downButton.setX(entryWidth - 40);
		downButton.setY(entryY + 18);
		downButton.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public abstract void drawEntryExtended(PoseStack matrixStack, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks);
	
}
