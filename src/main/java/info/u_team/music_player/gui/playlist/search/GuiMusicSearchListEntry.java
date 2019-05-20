package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;

abstract class GuiMusicSearchListEntry extends GuiScrollableListEntry<GuiMusicSearchListEntry> {
	
	protected final GuiButtonClickImage addTrackButton;
	
	GuiMusicSearchListEntry() {
		addTrackButton = addButton(new GuiButtonClickImage(0, 0, 20, 20, MusicPlayerResources.textureAdd));
	}
	
	@Override
	public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		addTrackButton.x = entryWidth - 20;
		addTrackButton.y = getY() + 8;
		addTrackButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, partialTicks);
	}
	
	protected void addTrackInfo(IAudioTrack track, int entryWidth, int leftMargin, int titleColor) {
		GuiTrackUtils.addTrackInfo(track, getX(), getY(), entryWidth, leftMargin, titleColor);
	}
	
}
