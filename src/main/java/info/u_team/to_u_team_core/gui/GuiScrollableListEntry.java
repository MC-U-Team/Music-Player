package info.u_team.to_u_team_core.gui;

import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.ExtendedList;

public abstract class GuiScrollableListEntry<T extends GuiScrollableListEntry<T>> extends ExtendedList.AbstractListEntry<T> {
	
	protected final Minecraft mc;
	
	private final List<Widget> widget;
	
	public GuiScrollableListEntry() {
		mc = Minecraft.getInstance();
		widget = new ArrayList<>();
	}
	
	protected <B extends Widget> B addButton(B button) {
		widget.add(button);
		return button;
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		widget.forEach(button -> button.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_));
		return true;
	}
	
	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		for (Widget button : widget) {
			if (button.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		for (Widget button : widget) {
			if (button.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public abstract void render(int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks);
}