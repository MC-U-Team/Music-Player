package info.u_team.music_player.config;

import static info.u_team.music_player.MusicPlayerConstants.MODID;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
@Config(modid = MODID, category = "")
public class ClientConfig {
	
	public static Settings settings = new Settings();
	
	public static class Settings {
		
		@RangeInt(min = 0, max = 100)
		@Name("Music Volume")
		public int music_volume = 10;
		
		@Comment("True if the game overlay is displayed")
		@Name("Display Overlay")
		public boolean game_overlay = true;
		
	}
	
}
