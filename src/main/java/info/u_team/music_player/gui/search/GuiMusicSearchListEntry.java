package info.u_team.music_player.gui.search;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;

public class GuiMusicSearchListEntry extends IGuiListEntry<GuiMusicSearchListEntry> {

	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_,
			float partialTicks) {
		Minecraft.getInstance().fontRenderer.drawString("Hallo, test", getX(), getY(), 0xFF00FF);

	}

}
