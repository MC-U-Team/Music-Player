package info.u_team.u_team_core.gui.elements;

import java.util.*;

import info.u_team.u_team_core.gui.elements.backport.GuiButtonNew;
import info.u_team.u_team_core.gui.elements.backport.GuiListExtendedNew.IGuiListEntryNew;
import net.minecraft.client.Minecraft;

public abstract class GuiScrollableListEntry<T extends GuiScrollableListEntry<T>> extends IGuiListEntryNew<T> {
	
	protected final Minecraft mc;
	
	private final List<GuiButtonNew> buttons;
	
	public GuiScrollableListEntry() {
		mc = Minecraft.getMinecraft();
		buttons = new ArrayList<>();
	}
	
	protected <B extends GuiButtonNew> B addButton(B button) {
		buttons.add(button);
		return button;
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		buttons.forEach(button -> button.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_));
		return true;
	}
	
	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		for (GuiButtonNew button : buttons) {
			if (button.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		for (GuiButtonNew button : buttons) {
			if (button.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public abstract void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks);
	
}