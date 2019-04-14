package info.u_team.music_player.gui.search;

import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiButtonAddPlaylist extends GuiButtonExt {
	
	private final Runnable run;
	
	private long timeStamp;
	
	public GuiButtonAddPlaylist(Runnable run) {
		super(-1, 0, 0, 50, 18, "Add");
		this.run = run;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		run.run();
		super.onClick(mouseX, mouseY);
		packedFGColor = 0xf47142;
		enabled = false;
		timeStamp = System.currentTimeMillis();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partial) {
		if (!enabled && timeStamp + 2500 <= System.currentTimeMillis()) {
			enabled = true;
		}
		super.render(mouseX, mouseY, partial);
	}
}
