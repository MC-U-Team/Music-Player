package info.u_team.music_player.gui.search;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiMusicSearchList extends GuiListExtended<GuiMusicSearchListEntry> {
	
	private final SearchList tracks;
	
	public GuiMusicSearchList(SearchList tracks, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
		super(mc, width, height, top, bottom, slotHeight);
		this.tracks = tracks;
		addAllTracks();
		tracks.isChangedAndReset();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (tracks.isChangedAndReset()) {
			getChildren().clear();
			addAllTracks();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public int getListWidth() {
		return width - 20;
	}
	
	@Override
	protected int getScrollBarX() {
		return width + 5;
	}
	
	@Override
	protected boolean isSelected(int slotIndex) {
		return slotIndex == selectedElement;
	}
	
	private void addAllTracks() {
		tracks.forEach(track -> addEntry(new GuiMusicSearchListEntry(track)));
	}
	
}
