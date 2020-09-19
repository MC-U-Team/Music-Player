package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import com.mojang.blaze3d.matrix.MatrixStack;

public class GuiMusicPlaylistListEntryLoading extends GuiMusicPlaylistListEntry {
	
	@Override
	public void render(MatrixStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		final String text = getTranslation(GUI_PLAYLIST_LOADING);
		minecraft.fontRenderer.drawString(matrixStack, text, entryX + (entryWidth / 2) - (minecraft.fontRenderer.getStringWidth(text) / 2), entryY + 20, 0xFF0000);
	}
	
}
