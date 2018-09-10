package info.u_team.music_player.impl.render.text;

import java.math.BigDecimal;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class ScrollingTextRender extends ScaledTextRender {
	
	protected int width;
	protected int speedtime;
	protected int waittime;
	
	protected float movedifference = 0F;
	protected long lasttime = 0;
	protected State state = State.WAITING;
	
	public ScrollingTextRender(FontRenderer fontrender) {
		super(fontrender);
		this.width = 100;
		this.speedtime = 20;
		this.waittime = 4000;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setSpeedTime(int speedtime) {
		this.speedtime = speedtime;
	}
	
	public void setWaitTime(int waittime) {
		this.waittime = waittime;
	}
	
	@Override
	public void draw(float x, float y) {
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
		
		int scaledwidth = (int) (width * scaledresolution.getScaleFactor() * scale);
		int scaledheight = (int) (fontrender.FONT_HEIGHT * scaledresolution.getScaleFactor() * scale);
		
		int scaledtextwidth = (int) (textwidth * scaledresolution.getScaleFactor() * scale);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor((int) (x * scaledresolution.getScaleFactor()), (int) (minecraft.displayHeight - (y * scaledresolution.getScaleFactor() + scaledheight)), scaledwidth, scaledheight);
		// Gui.drawRect(0, 0, scaledresolution.getScaledWidth(),
		// scaledresolution.getScaledHeight(), 0xFF00FF00); // test scissor
		x = move(x * scaledresolution.getScaleFactor(), scaledwidth, scaledtextwidth) / scaledresolution.getScaleFactor();
		BigDecimal bigdecimal = new BigDecimal(x).setScale(1, BigDecimal.ROUND_DOWN); // Why the fuck is lwjgl so dump (or fontrender) to have problems with to long
																						// decimals ....
		x = bigdecimal.floatValue();
		renderFont(x, y);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
	}
	
	private float move(float x, int scaledwidth, int scaledtextwidth) {
		if (scaledwidth < scaledtextwidth) {
			int maxmove = scaledwidth - scaledtextwidth;
			
			if (lasttime == 0) {
				lasttime = System.currentTimeMillis();
			}
			
			if (state == State.WAITING) {
				if (System.currentTimeMillis() - waittime >= lasttime) {
					if (movedifference >= 0) {
						state = State.LEFT;
					} else {
						state = State.RIGHT;
					}
					lasttime = 0;
				}
			} else if (state == State.LEFT) {
				if (System.currentTimeMillis() - speedtime >= lasttime) {
					if (movedifference >= maxmove) {
						movedifference -= 1;
					} else {
						state = State.WAITING;
					}
					lasttime = 0;
				}
			} else if (state == State.RIGHT) {
				if (System.currentTimeMillis() - speedtime >= lasttime) {
					if (movedifference <= 0) {
						movedifference += 1;
					} else {
						state = State.WAITING;
					}
					lasttime = 0;
				}
			}
			return x + movedifference;
		}
		return x;
	}
	
	private enum State {
		WAITING,
		LEFT,
		RIGHT;
	}
	
}
