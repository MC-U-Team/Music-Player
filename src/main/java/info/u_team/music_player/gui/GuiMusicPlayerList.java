package info.u_team.music_player.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public class GuiMusicPlayerList extends GuiSlot {

	public GuiMusicPlayerList() {
		super(Minecraft.getInstance(), 0, 0, 0, 0, 50);
	}

	@Override
	public int getListWidth() {
		return width - 20;
	}

	@Override
	protected int getScrollBarX() {
		return width + 5;
	}

	@Override
	protected int getSize() {
		return 20;
	}

	@Override
	protected boolean isSelected(int slotIndex) {
		return false;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int index, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		mc.fontRenderer.drawString("Playlist: " + index, xPos, yPos, 0xFFF00F);
	}

}
