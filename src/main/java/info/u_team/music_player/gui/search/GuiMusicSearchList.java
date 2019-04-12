package info.u_team.music_player.gui.search;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiMusicSearchList extends GuiListExtended<GuiMusicSearchListEntry> {

	public GuiMusicSearchList(Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
		super(mc, width, height, top, bottom, slotHeight);
		addEntry(new GuiMusicSearchListEntry());
		addEntry(new GuiMusicSearchListEntry());
		left = 0;
		top = 0;
		width = 600;
		height = 400;
		right = 20;
		bottom = 0;
		
		System.out.println(left);
	}

}
