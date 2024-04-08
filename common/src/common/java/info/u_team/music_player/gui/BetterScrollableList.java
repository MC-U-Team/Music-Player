package info.u_team.music_player.gui;

import info.u_team.u_team_core.gui.elements.ScrollableList;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.util.Mth;

public class BetterScrollableList<T extends ObjectSelectionList.Entry<T>> extends ScrollableList<T> {
	
	public BetterScrollableList(int x, int y, int width, int height, int slotHeight, int sideDistance) {
		super(x, y, width, height, slotHeight, sideDistance);
		setRenderHeader(false, 0);
		setRenderTransparentBorder(true);
	}
	
	public void updateSettings(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (getFocused() != null && isDragging() && button == 0) {
			getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}
		if (button != 0 || !scrolling) {
			return false;
		}
		if (mouseY < getY()) {
			setScrollAmount(0.0);
		} else if (mouseY > getBottom()) {
			setScrollAmount(getMaxScroll());
		} else {
			final double maxScroll = Math.max(1, getMaxScroll());
			final int height = this.height;
			final int clamped = Mth.clamp((int) ((float) (height * height) / (float) getMaxPosition()), 32, height - 8);
			final double scoll = Math.max(1.0, maxScroll / (height - clamped));
			setScrollAmount(getScrollAmount() + dragY * scoll);
		}
		return true;
	}
}