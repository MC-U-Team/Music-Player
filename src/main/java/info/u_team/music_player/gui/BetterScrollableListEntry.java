package info.u_team.music_player.gui;

import info.u_team.u_team_core.gui.elements.ScrollableListEntry;

public abstract class BetterScrollableListEntry<T extends ScrollableListEntry<T>> extends ScrollableListEntry<T> {
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		list.setSelected((T) this);
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
}
