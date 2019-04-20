package info.u_team.to_export_to_u_team_core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public abstract class GuiScrollableList extends GuiSlot {
	
	protected int listWidth;
	protected int scrollbarPos;
	
	public GuiScrollableList(int width, int height, int top, int bottom, int left, int right, int slotHeight, int listWidth, int scrollbarPos) {
		super(Minecraft.getInstance(), width, height, top, bottom, slotHeight);
		this.left = left;
		this.right = right;
		this.listWidth = listWidth;
		this.scrollbarPos = scrollbarPos;
	}
	
	@Override
	public int getListWidth() {
		return width - listWidth;
	}
	
	@Override
	protected int getScrollBarX() {
		return width + scrollbarPos;
	}
	
	@Override
	protected boolean isSelected(int index) {
		return false;
	}
	
	@Override
	protected void drawBackground() {
	}
	
}
