package info.u_team.u_team_core.gui.elements;

import java.util.function.*;

import info.u_team.u_team_core.gui.elements.backport.IGuiEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;

public class GuiProgressBar extends Gui implements IGuiEventListener {

	private Supplier<Double> progress;
	private Consumer<Double> click;

	private int width = 200;
	private int height = 5;
	private int x;
	private int y;

	private int backGroundColor;
	private int progressColor;

	private boolean enabled = true;
	private boolean visible = true;

	private boolean hovered;

	public GuiProgressBar(int x, int y, int width, int height, int backGroundColor, int progressColor, Supplier<Double> progress, Consumer<Double> click) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.backGroundColor = backGroundColor;
		this.progressColor = progressColor;
		this.progress = progress;
		this.click = click;
	}

	public void render(int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

			drawRect(x, y, x + width, y + height, backGroundColor);
			drawRect(x, y, (int) (x + (progress.get() * width)), y + height, progressColor);
		}
	}

	public void onClick(double mouseX, double mouseY) {
		if (click != null) {
			click.accept((double) (mouseX - x) / width);
		}
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			if (isPressable(mouseX, mouseY)) {
				playPressSound(Minecraft.getMinecraft().getSoundHandler());
				onClick(mouseX, mouseY);
				return true;
			}
		}
		return false;
	}

	protected boolean isPressable(double mouseX, double mouseY) {
		return enabled && visible && mouseX >= (double) this.x && mouseY >= (double) this.y && mouseX < (double) (this.x + this.width) && mouseY < (double) (this.y + this.height);
	}

	public void playPressSound(SoundHandler soundHandler) {
		soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public void setProgressSupplier(Supplier<Double> progress) {
		this.progress = progress;
	}

	public double getProgress() {
		return progress.get();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isHovered() {
		return hovered;
	}

	public int getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(int backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public int getProgressColor() {
		return progressColor;
	}

	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
	}

	public void setClick(Consumer<Double> click) {
		this.click = click;
	}

}
