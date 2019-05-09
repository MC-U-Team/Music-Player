package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.Playlist;
import info.u_team.music_player.util.WrappedObject;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImage;

abstract class GuiMusicPlaylistListEntryFunctions extends GuiMusicPlaylistListEntryPlayable {
	
	protected final Playlist playlist;
	protected final WrappedObject<String> uri;
	
	protected final GuiButtonClickImage deleteTrackButton;
	protected final GuiButtonClickImage upButton, downButton;
	
	GuiMusicPlaylistListEntryFunctions(GuiMusicPlaylistList guilist, Playlist playlist, WrappedObject<String> uri) {
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
		super.drawEntry(entryWidth, entryHeight, mouseX, mouseY, mouseInList, partialTicks);
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
