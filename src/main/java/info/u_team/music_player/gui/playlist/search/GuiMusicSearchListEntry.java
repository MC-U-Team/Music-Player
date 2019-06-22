package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.to_u_team_core.gui.GuiScrollableListEntry;
import info.u_team.u_team_core.gui.elements.*;

abstract class GuiMusicSearchListEntry extends GuiScrollableListEntry<GuiMusicSearchListEntry> {
	
	protected final ImageButton addTrackButton;
	
	GuiMusicSearchListEntry() {
		addTrackButton = addButton(new ImageButton(0, 0, 20, 20, MusicPlayerResources.textureAdd));
	}
	
	@Override
	public void render(int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		addTrackButton.x = entryWidth - 20;
		addTrackButton.y = entryY + 8;
		addTrackButton.render(mouseX, mouseY, partialTicks);
	}
	
	protected void addTrackInfo(IAudioTrack track, int entryX, int entryY, int entryWidth, int leftMargin, int titleColor) {
		GuiTrackUtils.addTrackInfo(track, entryX, entryY, entryWidth, leftMargin, titleColor);
	}
	
}
