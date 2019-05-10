package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImage;

abstract class GuiMusicPlaylistListEntryPlayable extends GuiMusicPlaylistListEntry {
	
	protected final GuiButtonClickImage playTrackButton;
	
	GuiMusicPlaylistListEntryPlayable() {
		playTrackButton = addButton(new GuiButtonClickImage(0, 0, 20, 20, MusicPlayerResources.texturePlay));
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		playTrackButton.x = entryWidth - 65;
		playTrackButton.y = getY() + 8;
		playTrackButton.render(mouseX, mouseY, partialTicks);
	}
}
