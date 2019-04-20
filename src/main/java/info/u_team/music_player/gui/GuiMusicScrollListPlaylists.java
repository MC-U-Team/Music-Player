package info.u_team.music_player.gui;

import info.u_team.to_export_to_u_team_core.gui.GuiScrollableList;

public class GuiMusicScrollListPlaylists extends GuiScrollableList {
	
	public GuiMusicScrollListPlaylists(int width, int height, int top, int bottom, int left, int right, int listWidth, int scrollbarPos) {
		super(width, height, top, bottom, left, right, 50, listWidth, scrollbarPos);
	}
	
	// @Override
	// public int getListWidth() {
	// return width - 20;
	// }
	//
	// @Override
	// protected int getScrollBarX() {
	// return width + 5;
	// }
	
	@Override
	protected int getSize() {
		return 20;
	}
	
	@Override
	protected void drawSlot(int index, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		mc.fontRenderer.drawString("Playlist: " + index, xPos, yPos, 0xFFF00F);
	}
	
}
