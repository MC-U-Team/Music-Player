package info.u_team.u_team_core.gui.elements;

import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.math.MathHelper;

public class RenderScrollingText extends RenderScalingText {
	
	protected int width;
	protected float stepSize;
	protected int speedTime;
	protected int waitTime;
	
	protected float moveDifference = 0;
	protected long lastTime = 0;
	protected State state = State.WAITING;
	
	public RenderScrollingText(Supplier<FontRenderer> fontRenderSupplier, Supplier<String> textSupplier) {
		super(fontRenderSupplier, textSupplier);
		width = 100;
		stepSize = 1;
		speedTime = 20;
		waitTime = 4000;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}
	
	public float getStepSize() {
		return stepSize;
	}
	
	public void setSpeedTime(int speedtime) {
		this.speedTime = speedtime;
	}
	
	public int getSpeedTime() {
		return speedTime;
	}
	
	public void setWaitTime(int waittime) {
		this.waitTime = waittime;
	}
	
	public int getWaitTime() {
		return waitTime;
	}
	
	@Override
	protected void updatedText() {
		state = State.WAITING;
		moveDifference = 0;
		lastTime = 0;
	}
	
	@Override
	public void draw(float x, float y) {
		final Minecraft mc = Minecraft.getMinecraft();
		final ScaledResolution scaledresolution = new ScaledResolution(mc);
		
		final double scaleFactor = scaledresolution.getScaleFactor();
		
		final int nativeX = MathHelper.ceil(x * scaleFactor);
		final int nativeY = MathHelper.ceil(y * scaleFactor);
		
		final int nativeWidth = MathHelper.ceil(width * scaleFactor);
		final int nativeHeight = MathHelper.ceil((fontRenderSupplier.get().FONT_HEIGHT + 1) * scale * scaleFactor);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		GL11.glScissor(nativeX, mc.displayHeight - (nativeY + nativeHeight), nativeWidth, nativeHeight);
		// Gui.drawRect(0, 0, window.getScaledWidth(), window.getScaledHeight(), 0xFF00FF00); // test scissor
		
		super.draw(getMovingX(x), y + 2);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
	}
	
	protected float getMovingX(float x) {
		final float textWidth = getTextWidth();
		if (width < textWidth) {
			final float maxMove = width - textWidth;
			
			if (lastTime == 0) {
				lastTime = System.currentTimeMillis();
			}
			
			if (state == State.WAITING) {
				if (hasWaitTimePassed()) {
					state = moveDifference >= 0 ? State.LEFT : State.RIGHT;
					lastTime = 0;
				}
			} else {
				if (hasSpeedTimePassed()) {
					if (state == State.LEFT ? moveDifference >= maxMove : moveDifference <= 0) {
						moveDifference += state == State.LEFT ? -stepSize : +stepSize;
					} else {
						state = State.WAITING;
					}
					lastTime = 0;
				}
			}
			return x + moveDifference;
		}
		return x;
	}
	
	protected boolean hasWaitTimePassed() {
		return System.currentTimeMillis() - waitTime >= lastTime;
	}
	
	protected boolean hasSpeedTimePassed() {
		return System.currentTimeMillis() - speedTime >= lastTime;
	}
	
	private enum State {
		WAITING,
		LEFT,
		RIGHT;
	}
	
}
