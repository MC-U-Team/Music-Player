package info.u_team.music_player.render.text;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

public class ScaledTextRender {
	
	protected FontRenderer fontrender;
	protected String text;
	protected float textwidth;
	protected int color;
	protected boolean shadow;
	protected float scale;
	
	public ScaledTextRender(FontRenderer fontrender) {
		this.fontrender = fontrender;
		this.setText("default");
		this.color = 0x000;
		this.shadow = false;
		this.scale = 1F;
	}
	
	public void setText(String text) {
		this.text = text;
		this.textwidth = fontrender.getStringWidth(text);
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getTextWidth() {
		return textwidth * scale;
	}
	
	public void draw(float x, float y) {
		GL11.glPushMatrix();
		renderFont(x, y);
		GL11.glPopMatrix();
	}
	
	protected void renderFont(float x, float y) {
		GL11.glTranslatef(x, y, 0);
		GL11.glScalef(scale, scale, 0);
		fontrender.drawString(text, 0, 0, color, shadow);
	}
	
}
