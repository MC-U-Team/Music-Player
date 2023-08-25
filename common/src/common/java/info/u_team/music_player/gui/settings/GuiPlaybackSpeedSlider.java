package info.u_team.music_player.gui.settings;

import org.lwjgl.glfw.GLFW;

import info.u_team.u_team_core.gui.elements.USlider;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GuiPlaybackSpeedSlider extends USlider {
	
	private float stepSize = 0.25F;
	
	public GuiPlaybackSpeedSlider(int x, int y, int width, int height, Component prefix, Component suffix, double value, boolean drawDescription, OnSliderChange slider) {
		super(x, y, width, height, prefix, suffix, 0.25F, 2, value, true, drawDescription, slider);
	}
	
	@Override
	public void setValue(double newValue) {
		value = snap((newValue - minValue) / (maxValue - minValue));
		updateSliderText();
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		setValueFromMouse(mouseX);
	}
	
	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		if (active && visible) {
			setValueFromMouse(mouseX);
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean flag = keyCode == GLFW.GLFW_KEY_LEFT;
		if (flag || keyCode == GLFW.GLFW_KEY_RIGHT) {
			if (minValue > maxValue)
				flag = !flag;
			float f = flag ? -1F : 1F;
			if (stepSize <= 0D)
				this.setSliderValue(this.value + (f / (this.width - 8)));
			else
				this.setValue(this.getValue() + f * this.stepSize);
		}
		
		return false;
	}
	
	private void setValueFromMouse(double mouseX) {
		setSliderValue((mouseX - (this.getX() + 4)) / (this.width - 8));
	}
	
	private void setSliderValue(double value) {
		double oldValue = this.value;
		this.value = this.snap(value);
		if (!Mth.equal(oldValue, this.value))
			this.applyValue();
		
		this.updateMessage();
	}
	
	private double snap(double value) {
		value = Mth.lerp(Mth.clamp(value, 0, 1), minValue, maxValue);
		
		value = (stepSize * Math.round(value / stepSize));
		
		if (minValue > maxValue) {
			value = Mth.clamp(value, maxValue, minValue);
		} else {
			value = Mth.clamp(value, minValue, maxValue);
		}
		
		return Mth.map(value, minValue, maxValue, 0, 1);
	}
	
}
