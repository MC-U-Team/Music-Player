package info.u_team.music_player.render.gui.button.special;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.config.ClientConfig;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.render.gui.button.GuiButtonSilder;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiButtonSliderVolume extends GuiButtonSilder {
	
	public GuiButtonSliderVolume(IMusicPlayer musicplayer, int id, int x, int y, int width, int height) {
		super(id, x, y, width, height, I18n.format(MusicPlayerConstants.MODID + ":button.volume") + ": ", "", 0, 100, musicplayer.getVolume(), false, new ISlider() {
			
			@Override
			public void onChangeSliderValue(GuiSlider slider) {
				musicplayer.setVolume(slider.getValueInt());
				ClientConfig.settings.music_volume = slider.getValueInt();
			}
		});
	}
}
