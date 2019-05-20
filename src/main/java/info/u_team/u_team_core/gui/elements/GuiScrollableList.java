package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.gui.elements.backport.GuiListExtendedNew;
import info.u_team.u_team_core.gui.elements.backport.GuiListExtendedNew.IGuiListEntryNew;
import net.minecraft.client.Minecraft;

public abstract class GuiScrollableList<T extends IGuiListEntryNew<T>> extends GuiListExtendedNew<T> {
	
	protected int listWidth;
	protected int scrollbarPos;
	
	public GuiScrollableList(int width, int height, int top, int bottom, int left, int right, int slotHeight, int listWidth, int scrollbarPos) {
		super(Minecraft.getMinecraft(), 0, 0, 0, 0, slotHeight);
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
