package info.u_team.to_u_team_core.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.*;

public abstract class GuiScrollableList<T extends AbstractList.AbstractListEntry<T>> extends ExtendedList<T> {
	
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
		this.y0 = top;
		this.y1 = bottom;
		this.x0 = left;
		this.x1 = right;
	}
	
	@Override
	public int getRowWidth() {
		return width - listWidth;
	}
	
	@Override
	protected int getScrollbarPosition() {
		return width + scrollbarPos;
	}
}
