package info.u_team.music_player.gui;

import info.u_team.u_team_core.gui.elements.ScrollableListEntry;

public abstract class BetterScrollableListEntry<T extends ScrollableListEntry<T>> extends ScrollableListEntry<T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		getList().setSelected((T) this);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
}
