package info.u_team.music_player.gui.controls;

import info.u_team.u_team_core.gui.elements.ScalableSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public class GuiVolumeSlider extends ScalableSlider {
	
	private boolean clicked;
	
	public GuiVolumeSlider(int x, int y, int width, int height, ITextComponent prefix, ITextComponent suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, float scale, ISlider slider) {
		super(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, scale, slider);
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		clicked = true;
	}
	
	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (isHovered() && clicked) {
			Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1));
		}
		clicked = false;
	}
}
