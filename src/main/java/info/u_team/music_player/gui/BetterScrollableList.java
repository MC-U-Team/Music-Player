package info.u_team.music_player.gui;

import info.u_team.u_team_core.gui.elements.ScrollableList;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.util.Mth;

public class BetterScrollableList<T extends ObjectSelectionList.Entry<T>> extends ScrollableList<T> {
	
	public BetterScrollableList(int width, int height, int top, int bottom, int left, int right, int slotHeight, int sideDistance) {
		super(width, height, top, bottom, left, right, slotHeight, sideDistance);
		setRenderTopAndBottom(false);
		setUseScissor(true);
		setRenderTransparentBorder(true);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (getFocused() != null && isDragging() && button == 0) {
			getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}
		if (button == 0 && scrolling) {
			if (mouseY < y0) {
				setScrollAmount(0.0D);
			} else if (mouseY > y1) {
				setScrollAmount(getMaxScroll());
			} else {
				final double clampedMaxScroll = Math.max(1, getMaxScroll());
				final int diff = y1 - y0;
				final int clamped = Mth.clamp((diff * diff) / getMaxPosition(), 32, diff - 8);
				setScrollAmount(getScrollAmount() + dragY * Math.max(1.0D, clampedMaxScroll / (diff - clamped)));
			}
			return true;
		} else {
			return false;
		}
	}
}