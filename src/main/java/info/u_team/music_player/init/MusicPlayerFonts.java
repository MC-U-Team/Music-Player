package info.u_team.music_player.init;

import info.u_team.music_player.MusicPlayerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class MusicPlayerFonts {
	
	public static FontRenderer roboto;
	
	public static void setup() {
		System.out.println(Minecraft.getInstance());
		System.out.println(Minecraft.getInstance().getFontResourceManager());
		roboto = Minecraft.getInstance().getFontResourceManager().getFontRenderer(new ResourceLocation(MusicPlayerMod.modid, "roboto-medium"));
	}
}
