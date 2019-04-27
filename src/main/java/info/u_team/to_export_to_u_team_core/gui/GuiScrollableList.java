package info.u_team.to_export_to_u_team_core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public abstract class GuiScrollableList<T extends IGuiListEntry<T>> extends GuiListExtended<T> {
	
	protected int listWidth;
	protected int scrollbarPos;
	
	public GuiScrollableList(int width, int height, int top, int bottom, int left, int right, int slotHeight, int listWidth, int scrollbarPos) {
		super(Minecraft.getInstance(), 0, 0, 0, 0, slotHeight);
		updateSettings(width, height, top, bottom, left, right);
		this.listWidth = listWidth;
		this.scrollbarPos = scrollbarPos;
	}
	
	public void updateSettings(int width, int height, int top, int bottom, int left, int right) {
		this.width = width;
		this.height = height;
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
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
	
	protected final void removeEntry(T entry) {
		getChildren().remove(entry);
	}
}
