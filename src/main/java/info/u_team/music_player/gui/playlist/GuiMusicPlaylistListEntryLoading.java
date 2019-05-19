package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

public class GuiMusicPlaylistListEntryLoading extends GuiMusicPlaylistListEntry {
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		String text = getTranslation(gui_playlist_loading);
		mc.fontRenderer.drawString(text, getX() + (entryWidth / 2) - (mc.fontRenderer.getStringWidth(text) / 2), getY() + 20, 0xFF0000);
	}
	
}
