package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

public class GuiMusicPlaylistListEntryLoading extends GuiMusicPlaylistListEntry {
	
	@Override
	public void render(int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		String text = getTranslation(gui_playlist_loading);
		mc.fontRenderer.drawString(text, entryX + (entryWidth / 2) - (mc.fontRenderer.getStringWidth(text) / 2), entryY + 20, 0xFF0000);
	}
	
}
