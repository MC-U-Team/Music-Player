package info.u_team.music_player.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public abstract class BetterScreen extends Screen implements BetterNestedGui {
	
	public BetterScreen(ITextComponent title) {
		super(title);
	}
	
}
