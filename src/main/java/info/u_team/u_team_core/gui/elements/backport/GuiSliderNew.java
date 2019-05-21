package info.u_team.u_team_core.gui.elements.backport;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiSliderNew extends GuiButtonExtNew {
	
	/** The value of this slider control. */
	public double sliderValue = 1.0F;
	
	public String dispString = "";
	
	/** Is this slider control being dragged. */
	public boolean dragging = false;
	public boolean showDecimal = true;
	
	public double minValue = 0.0D;
	public double maxValue = 5.0D;
	public int precision = 1;
	
	@Nullable
	public ISlider parent = null;
	
	public String suffix = "";
	
	public boolean drawString = true;
	
	public GuiSliderNew(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
		this(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
	}
	
	public GuiSliderNew(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, @Nullable ISlider par) {
		super(id, xPos, yPos, width, height, prefix);
		minValue = minVal;
		maxValue = maxVal;
		sliderValue = (currentVal - minValue) / (maxValue - minValue);
		dispString = prefix;
		parent = par;
		suffix = suf;
		showDecimal = showDec;
		String val;
		
		if (showDecimal) {
			val = Double.toString(sliderValue * (maxValue - minValue) + minValue);
			precision = Math.min(val.substring(val.indexOf(".") + 1).length(), 4);
		} else {
			val = Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue));
			precision = 0;
		}
		
		displayString = dispString + val + suffix;
		
		drawString = drawStr;
		if (!drawString) {
			displayString = "";
		}
	}
	
	public GuiSliderNew(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par) {
		this(id, xPos, yPos, 150, 20, displayStr, "", minVal, maxVal, currentVal, true, true, par);
	}
	
	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
	 * this button.
	 */
	/**
	 * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
	 * this button.
	 */
	@Override
	public int getHoverState(boolean par1) {
		return 0;
	}
	
	/**
	 * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
	 */
	/**
	 * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
	 */
	@Override
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
		if (this.visible) {
			if (this.dragging) {
				this.sliderValue = (par2 - (this.x + 4)) / (float) (this.width - 8);
				updateSlider();
			}
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x + (int) (this.sliderValue * (float) (this.width - 8)), this.y, 0, 66, 8, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
		}
	}
	
	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			this.sliderValue = (float) (par2 - (this.x + 4)) / (float) (this.width - 8);
			updateSlider();
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}
	
	public void updateSlider() {
		if (this.sliderValue < 0.0F) {
			this.sliderValue = 0.0F;
		}
		
		if (this.sliderValue > 1.0F) {
			this.sliderValue = 1.0F;
		}
		
		String val;
		
		if (showDecimal) {
			val = Double.toString(sliderValue * (maxValue - minValue) + minValue);
			
			if (val.substring(val.indexOf(".") + 1).length() > precision) {
				val = val.substring(0, val.indexOf(".") + precision + 1);
				
				if (val.endsWith(".")) {
					val = val.substring(0, val.indexOf(".") + precision);
				}
			} else {
				while (val.substring(val.indexOf(".") + 1).length() < precision) {
					val = val + "0";
				}
			}
		} else {
			val = Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue));
		}
		
		if (drawString) {
			displayString = dispString + val + suffix;
		}
		
		if (parent != null) {
			parent.onChangeSliderValue(this);
		}
	}
	
	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	@Override
	public void mouseReleased(int par1, int par2) {
		this.dragging = false;
	}
	
	public int getValueInt() {
		return (int) Math.round(sliderValue * (maxValue - minValue) + minValue);
	}
	
	public double getValue() {
		return sliderValue * (maxValue - minValue) + minValue;
	}
	
	public void setValue(double d) {
		this.sliderValue = (d - minValue) / (maxValue - minValue);
	}
	
	public static interface ISlider {
		
		void onChangeSliderValue(GuiSliderNew slider);
	}
}