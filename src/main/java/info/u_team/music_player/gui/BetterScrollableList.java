package info.u_team.music_player.gui;

import info.u_team.u_team_core.gui.elements.ScrollableList;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.util.math.MathHelper;

public class BetterScrollableList<T extends AbstractList.AbstractListEntry<T>> extends ScrollableList<T> {
	
	public BetterScrollableList(int width, int height, int top, int bottom, int left, int right, int slotHeight, int sideDistance) {
		super(width, height, top, bottom, left, right, slotHeight, sideDistance);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (getListener() != null && isDragging() && button == 0) {
			getListener().mouseDragged(mouseX, mouseY, button, dragX, dragY);
		}
		if (button == 0 && scrolling) {
			if (mouseY < y0) {
				setScrollAmount(0.0D);
			} else if (mouseY > y1) {
				setScrollAmount(getMaxScroll());
			} else {
				final double clampedMaxScroll = Math.max(1, getMaxScroll());
				final int diff = y1 - y0;
				final int clamped = MathHelper.clamp((diff * diff) / getMaxPosition(), 32, diff - 8);
				setScrollAmount(getScrollAmount() + dragY * Math.max(1.0D, clampedMaxScroll / (diff - clamped)));
			}
			return true;
		} else {
			return false;
		}
	}
}