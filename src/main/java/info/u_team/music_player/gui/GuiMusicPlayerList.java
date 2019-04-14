package info.u_team.music_player.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiMusicPlayerList extends GuiListExtended<GuiMusicPlayerListEntry> {
	
	public GuiMusicPlayerList(Minecraft mc, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
		super(mc, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}
	
}
