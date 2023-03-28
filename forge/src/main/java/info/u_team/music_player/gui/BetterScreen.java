package info.u_team.music_player.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class BetterScreen extends Screen implements BetterNestedGui {
	
	public BetterScreen(Component title) {
		super(title);
	}
	
}
