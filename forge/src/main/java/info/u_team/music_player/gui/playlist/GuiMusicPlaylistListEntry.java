package info.u_team.music_player.gui.playlist;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.BetterScrollableListEntry;
import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class GuiMusicPlaylistListEntry extends BetterScrollableListEntry<GuiMusicPlaylistListEntry> {
	
	protected void addTrackInfo(PoseStack matrixStack, IAudioTrack track, int entryX, int entryY, int entryWidth, int leftMargin, int titleColor) {
		GuiTrackUtils.addTrackInfo(matrixStack, track, entryX, entryY, entryWidth, leftMargin, titleColor);
	}
	
	protected void tick() {
	}
	
	@Override
	public Component getNarration() {
		return CommonComponents.EMPTY;
	}
}
