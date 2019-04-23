package info.u_team.to_export_to_u_team_core.gui;

import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public abstract class GuiScrollableListEntry extends IGuiListEntry<GuiScrollableListEntry> {
	
	@Override
	public abstract void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks);
	
}