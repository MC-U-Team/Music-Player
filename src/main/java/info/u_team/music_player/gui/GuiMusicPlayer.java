package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.apache.commons.lang3.StringUtils;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.u_team_core.gui.elements.*;
import info.u_team.u_team_core.gui.elements.backport.*;
import net.minecraft.client.Minecraft;

public class GuiMusicPlayer extends GuiScreen1_13 {
	
	private GuiTextFieldNew namePlaylistField;
	
	private GuiMusicPlayerList playlistsList;
	
	private GuiControls controls;
	
	@Override
	public void initGui() {
		final GuiButtonClick backButton = addNewButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> mc.displayGuiScreen(null));
		
		namePlaylistField = new GuiTextFieldNew(-1, mc.fontRenderer, 100, 60, width - 150, 20);
		namePlaylistField.setMaxStringLength(500);
		children.add(namePlaylistField);
		
		final GuiButtonClickImage addPlaylistButton = addNewButton(new GuiButtonClickImage(width - 41, 59, 22, 22, MusicPlayerResources.textureCreate));
		addPlaylistButton.setClickAction(() -> {
			String name = namePlaylistField.getText();
			if (StringUtils.isBlank(name) || name.equals(getTranslation(gui_create_playlist_insert_name))) {
				namePlaylistField.setText(getTranslation(gui_create_playlist_insert_name));
				return;
			}
			playlistsList.addPlaylist(name);
			namePlaylistField.setText("");
		});
		
		playlistsList = new GuiMusicPlayerList(width - 24, height, 90, height - 10, 12, width - 12);
		children.add(playlistsList);
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
		super.initGui();
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		final String text = namePlaylistField.getText();
		final RenderScrollingText titleRender = controls.getTitleRender();
		final RenderScrollingText authorRender = controls.getAuthorRender();
		this.setWorldAndResolution(minecraft, width, height);
		namePlaylistField.setText(text);
		controls.setTitleRender(titleRender);
		controls.setAuthorRender(authorRender);
	}
	
	@Override
	public void updateScreen() {
		namePlaylistField.tick();
		controls.tick();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		playlistsList.drawScreen(mouseX, mouseY, partialTicks);
		mc.fontRenderer.drawString(getTranslation(gui_create_playlist_add_list), 20, 65, 0xFFFFFF);
		namePlaylistField.drawTextField(mouseX, mouseY, partialTicks);
		controls.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlayerList getPlaylistsList() {
		return playlistsList;
	}
	
}
