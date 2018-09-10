package info.u_team.music_player.impl.render.gui.button.special;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.config.Config;
import info.u_team.music_player.impl.render.gui.button.GuiButtonSilder;
import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiButtonSliderVolume extends GuiButtonSilder {
	
	public GuiButtonSliderVolume(MusicPlayer musicplayer, int id, int x, int y, int width, int height) {
		super(id, x, y, width, height, I18n.format(MusicPlayerConstants.MODID + ":button.volume") + ": ", "", 0, 100, musicplayer.getVolume(), false, new ISlider() {
			
			@Override
			public void onChangeSliderValue(GuiSlider slider) {
				musicplayer.setVolume(slider.getValueInt());
				Config.setVolume(slider.getValueInt());
			}
		});
	}
}
