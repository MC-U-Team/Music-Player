package info.u_team.music_player.impl.render.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiButtonSilder extends GuiSlider {
	
	public GuiButtonSilder(int id, int x, int y, int width, int height, String prefix, String suffix, double min, double max, double current, boolean decimal, ISlider islider) {
		super(id, x, y, width, height, prefix, suffix, min, max, current, decimal, true, islider);
	}
	
	@Override
	protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
		if (visible) {
			if (dragging) {
				if (!isMouseOver()) {
					dragging = false;
				}
				this.sliderValue = (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
				updateSlider();
			}
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			drawScaledCustomSizeModalRect(this.xPosition + (int) (this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20, 4, height, 256, 256);
			drawScaledCustomSizeModalRect(this.xPosition + (int) (this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20, 4, height, 256, 256);
		}
	}
}
