package info.u_team.to_u_team_core.export;

import java.util.Objects;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

public class ScalingTextRender {
	
	protected final Supplier<FontRenderer> fontRenderSupplier;
	
	protected Supplier<String> textSupplier;
	
	private String text;
	private int textWidth;
	
	protected int color;
	protected boolean shadow;
	protected float scale;
	
	public ScalingTextRender(Supplier<FontRenderer> fontRenderSupplier, Supplier<String> textSupplier) {
		this.fontRenderSupplier = fontRenderSupplier;
		this.textSupplier = textSupplier;
		this.scale = 1;
	}
	
	public void setTextSupplier(Supplier<String> textSupplier) {
		Objects.requireNonNull(textSupplier);
		this.textSupplier = textSupplier;
	}
	
	public Supplier<String> getTextSupplier() {
		return textSupplier;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}
	
	public boolean isShadow() {
		return shadow;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getScale() {
		return scale;
	}
	
	public float getTextWidth() {
		return textWidth * scale;
	}
	
	private void setText(String newText) {
		if ((newText != null && !newText.equals(text)) || newText == null) {
			this.text = newText;
			this.textWidth = fontRenderSupplier.get().getStringWidth(newText);
		}
	}
	
	public void draw(float x, float y) {
		// Get new text and set if has changed
		setText(textSupplier.get());
		renderFont(x, y);
	}
	
	protected void renderFont(float x, float y) {
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, 0);
		if (shadow) {
			fontRenderSupplier.get().drawStringWithShadow(text, x, y, color);
		} else {
			fontRenderSupplier.get().drawString(text, x, y, color);
		}
		GL11.glPopMatrix();
	}
}
