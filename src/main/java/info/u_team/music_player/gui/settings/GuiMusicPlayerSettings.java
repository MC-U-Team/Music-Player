package info.u_team.music_player.gui.settings;

import info.u_team.music_player.gui.playing.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.to_u_team_core.export.GuiButtonClickActivated;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlayerSettings extends GuiScreen {
	
	private final GuiScreen previousGui;
	
	private GuiControls controls;
	
	public GuiMusicPlayerSettings(GuiScreen previousGui) {
		this.previousGui = previousGui;
	}
	
	@Override
	protected void initGui() {
		final GuiButtonClick backButton = addButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> mc.displayGuiScreen(previousGui));
		
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		// TODO
		final GuiButtonClickActivated toggleIngameDisplay = addButton(new GuiButtonClickActivated(12, 50, 200, 20, "Toggle ingame display", 0x80FF00FF));
		toggleIngameDisplay.setActive(settings.isShowIngameOverlay());
		toggleIngameDisplay.setClickAction(() -> {
			settings.setShowIngameOverlay(!settings.isShowIngameOverlay());
			toggleIngameDisplay.setActive(settings.isShowIngameOverlay());
		});
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
		super.initGui();
	}
	
	@Override
	public void tick() {
		controls.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		controls.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
