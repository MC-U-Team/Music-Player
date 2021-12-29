package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLIST_LOADING;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import com.mojang.blaze3d.vertex.PoseStack;

public class GuiMusicPlaylistListEntryLoading extends GuiMusicPlaylistListEntry {
	
	@Override
	public void render(PoseStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		final String text = getTranslation(GUI_PLAYLIST_LOADING);
		minecraft.font.draw(matrixStack, text, entryX + (entryWidth / 2) - (minecraft.font.width(text) / 2), entryY + 20, 0xFF0000);
	}
	
}
