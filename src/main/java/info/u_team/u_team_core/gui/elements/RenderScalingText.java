package info.u_team.u_team_core.gui.elements;

import java.util.Objects;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

public class RenderScalingText {
	
	protected final Supplier<FontRenderer> fontRenderSupplier;
	
	protected Supplier<String> textSupplier;
	
	private String text;
	private int textWidth;
	
	protected int color;
	protected boolean shadow;
	protected float scale;
	
	public RenderScalingText(Supplier<FontRenderer> fontRenderSupplier, Supplier<String> textSupplier) {
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
		if (textWidth == 0) { // If text width has never been set
			setText(textSupplier.get());
		}
		return textWidth * scale;
	}
	
	private void setText(String newText) {
		if ((newText != null && !newText.equals(text)) || newText == null) {
			this.text = newText;
			this.textWidth = fontRenderSupplier.get().getStringWidth(newText);
			updatedText();
		}
	}
	
	protected void updatedText() {
	}
	
	public void draw(float x, float y) {
		// Get new text and set if has changed
		setText(textSupplier.get());
		renderFont(x, y);
	}
	
	protected void renderFont(float x, float y) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glScalef(scale, scale, 0);
		if (shadow) {
			fontRenderSupplier.get().drawStringWithShadow(text, 0, 0, color);
		} else {
			fontRenderSupplier.get().drawString(text, 0, 0, color);
		}
		GL11.glPopMatrix();
	}
}
