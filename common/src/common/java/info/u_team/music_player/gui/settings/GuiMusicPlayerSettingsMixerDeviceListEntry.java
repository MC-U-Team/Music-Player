package info.u_team.music_player.gui.settings;

import info.u_team.music_player.gui.BetterScrollableListEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

class GuiMusicPlayerSettingsMixerDeviceListEntry extends BetterScrollableListEntry<GuiMusicPlayerSettingsMixerDeviceListEntry> {
	
	private final String mixerName;
	
	public GuiMusicPlayerSettingsMixerDeviceListEntry(String mixerName) {
		this.mixerName = mixerName;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		guiGraphics.drawString(minecraft.font, mixerName, entryX + 5, entryY + 5, 0x0083FF, false);
	}
	
	public String getMixerName() {
		return mixerName;
	}
	
	@Override
	public Component getNarration() {
		return CommonComponents.EMPTY;
	}
	
}