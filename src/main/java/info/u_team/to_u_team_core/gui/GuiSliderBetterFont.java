package info.u_team.to_u_team_core.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.*;

public class GuiSliderBetterFont extends GuiSlider {
	
	protected float scale;
	
	public GuiSliderBetterFont(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, float scale, ISlider par) {
		super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, slider -> {
		}, par);
		this.scale = scale;
	}
	
	@Override
	public void renderButton(int mouseX, int mouseY, float partial) {
		GuiUtils.drawContinuousTexturedBox(WIDGETS_LOCATION, this.x, this.y, 0, 46 + getYImage(isHovered()) * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, 0);
		final Minecraft minecraft = Minecraft.getInstance();
		renderBg(minecraft, mouseX, mouseY);
		final int color = getFGColor();
		String buttonText = getMessage();
		int strWidth = MathHelper.ceil(scale * minecraft.fontRenderer.getStringWidth(buttonText));
		int ellipsisWidth = MathHelper.ceil(scale * minecraft.fontRenderer.getStringWidth("..."));
		
		if (strWidth > width - 6 && strWidth > ellipsisWidth)
			buttonText = minecraft.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x + width / 2, y + (height - 8 * scale) / 2, 0);
		GL11.glScalef(scale, scale, 0);
		drawCenteredString(minecraft.fontRenderer, buttonText, 0, 0, color);
		GL11.glPopMatrix();
	}
}
