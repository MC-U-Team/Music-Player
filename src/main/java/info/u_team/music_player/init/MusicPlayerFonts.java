package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class MusicPlayerFonts {
	
	public static FontRenderer roboto;
	
	public static void complete() {
		roboto = Minecraft.getInstance().getFontResourceManager().getFontRenderer(new ResourceLocation(MusicPlayerMod.modid, "roboto-medium"));
	}
}
