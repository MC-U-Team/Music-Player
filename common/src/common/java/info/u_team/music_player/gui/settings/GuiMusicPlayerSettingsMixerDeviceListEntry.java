package info.u_team.music_player.gui.settings;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.BetterScrollableListEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

class GuiMusicPlayerSettingsMixerDeviceListEntry extends BetterScrollableListEntry<GuiMusicPlayerSettingsMixerDeviceListEntry> {
	
	private final String mixerName;
	
	public GuiMusicPlayerSettingsMixerDeviceListEntry(String mixerName) {
		this.mixerName = mixerName;
	}
	
	@Override
	public void render(PoseStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		minecraft.font.draw(matrixStack, mixerName, entryX + 5, entryY + 5, 0x0083FF);
	}
	
	public String getMixerName() {
		return mixerName;
	}
	
	@Override
	public Component getNarration() {
		return CommonComponents.EMPTY;
	}
	
}