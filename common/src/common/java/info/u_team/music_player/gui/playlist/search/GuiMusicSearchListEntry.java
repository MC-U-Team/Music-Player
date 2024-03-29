package info.u_team.music_player.gui.playlist.search;

import info.u_team.music_player.gui.BetterScrollableListEntry;
import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.u_team_core.gui.elements.ImageButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

abstract class GuiMusicSearchListEntry extends BetterScrollableListEntry<GuiMusicSearchListEntry> {
	
	protected final ImageButton addTrackButton;
	
	GuiMusicSearchListEntry() {
		addTrackButton = addChildren(new ImageButton(0, 0, 20, 20, MusicPlayerResources.TEXTURE_ADD));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		addTrackButton.setX(entryWidth - 20);
		addTrackButton.setY(entryY + 8);
		addTrackButton.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	protected void addTrackInfo(GuiGraphics guiGraphics, IAudioTrack track, int entryX, int entryY, int entryWidth, int leftMargin, int titleColor) {
		GuiTrackUtils.addTrackInfo(guiGraphics, track, entryX, entryY, entryWidth, leftMargin, titleColor);
	}
	
	@Override
	public Component getNarration() {
		return CommonComponents.EMPTY;
	}
	
}
