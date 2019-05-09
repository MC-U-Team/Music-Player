package info.u_team.music_player.gui.playlist;

public class GuiMusicPlaylistListEntryLoading extends GuiMusicPlaylistListEntry {
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		String text = "Loading tracks ...";
		mc.fontRenderer.drawString(text, getX() + (entryWidth / 2) - (mc.fontRenderer.getStringWidth(text) / 2), getY() + 20, 0xFF0000);
	}
	
}
