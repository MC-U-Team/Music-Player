package info.u_team.music_player.gui.settings;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.*;
import info.u_team.u_team_core.gui.elements.*;
import info.u_team.u_team_core.gui.elements.backport.GuiScreen1_13;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlayerSettings extends GuiScreen1_13 {
	
	private final GuiScreen previousGui;
	
	private GuiControls controls;
	
	public GuiMusicPlayerSettings(GuiScreen previousGui) {
		this.previousGui = previousGui;
	}
	
	@Override
	public void initGui() {
		final GuiButtonClick backButton = addNewButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> mc.displayGuiScreen(previousGui));
		
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		
		final GuiButtonClickActivated toggleKeyWorkInGuiButton = addNewButton(new GuiButtonClickActivated(12, 60, width / 2 - 24, 20, getTranslation(gui_settings_toggle_key_in_gui), 0x80FF00FF));
		toggleKeyWorkInGuiButton.setActive(settings.isKeyWorkInGui());
		toggleKeyWorkInGuiButton.setClickAction(() -> {
			settings.setKeyWorkInGui(!settings.isKeyWorkInGui());
			toggleKeyWorkInGuiButton.setActive(settings.isKeyWorkInGui());
		});
		
		final GuiButtonClickActivated toggleIngameMenueDisplayButton = addNewButton(new GuiButtonClickActivated(width / 2 + 14, 60, width / 2 - 24, 20, getTranslation(gui_settings_toggle_menue_overlay), 0x80FF00FF));
		toggleIngameMenueDisplayButton.setActive(settings.isShowIngameMenueOverlay());
		toggleIngameMenueDisplayButton.setClickAction(() -> {
			settings.setShowIngameMenueOverlay(!settings.isShowIngameMenueOverlay());
			toggleIngameMenueDisplayButton.setActive(settings.isShowIngameMenueOverlay());
		});
		
		final GuiButtonClickActivated toggleIngameDisplayButton = addNewButton(new GuiButtonClickActivated(12, 90, width / 2 - 24, 20, getTranslation(gui_settings_toggle_ingame_overlay), 0x80FF00FF));
		toggleIngameDisplayButton.setActive(settings.isShowIngameOverlay());
		toggleIngameDisplayButton.setClickAction(() -> {
			settings.setShowIngameOverlay(!settings.isShowIngameOverlay());
			toggleIngameDisplayButton.setActive(settings.isShowIngameOverlay());
		});
		
		final GuiButtonClick ingameOverlayPositionButton = addNewButton(new GuiButtonClick(width / 2 + 14, 90, width / 2 - 24, 20, getTranslation(gui_settings_position_overlay) + ": " + getTranslation(settings.getIngameOverlayPosition().getLocalization())));
		ingameOverlayPositionButton.setClickAction(() -> {
			settings.setIngameOverlayPosition(IngameOverlayPosition.forwardCycle(settings.getIngameOverlayPosition()));
			ingameOverlayPositionButton.displayString = getTranslation(gui_settings_position_overlay) + ": " + getTranslation(settings.getIngameOverlayPosition().getLocalization());
		});
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
		super.initGui();
	}
	
	@Override
	public void updateScreen() {
		controls.tick();
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		final RenderScrollingText titleRender = controls.getTitleRender();
		final RenderScrollingText authorRender = controls.getAuthorRender();
		this.setWorldAndResolution(minecraft, width, height);
		controls.setTitleRender(titleRender);
		controls.setAuthorRender(authorRender);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		controls.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
}
