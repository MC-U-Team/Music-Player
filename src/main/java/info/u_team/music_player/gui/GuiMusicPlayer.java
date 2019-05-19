package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import org.apache.commons.lang3.StringUtils;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.to_u_team_core.export.RenderScrollingText;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class GuiMusicPlayer extends GuiScreen {
	
	private GuiTextField namePlaylistField;
	
	private GuiMusicPlayerList playlistsList;
	
	private GuiControls controls;
	
	@Override
	protected void initGui() {
		final GuiButtonClick backButton = addButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> mc.displayGuiScreen(null));
		
		namePlaylistField = new GuiTextField(-1, mc.fontRenderer, 100, 60, width - 150, 20);
		namePlaylistField.setMaxStringLength(500);
		children.add(namePlaylistField);
		
		final GuiButtonClickImage addPlaylistButton = addButton(new GuiButtonClickImage(width - 41, 59, 22, 22, MusicPlayerResources.textureCreate));
		addPlaylistButton.setClickAction(() -> {
			String name = namePlaylistField.getText();
			if (StringUtils.isBlank(name) || name.equals(getTranslation(gui_create_playlist_insert_name))) {
				namePlaylistField.setText("Enter a name");
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
	public void tick() {
		namePlaylistField.tick();
		controls.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		playlistsList.drawScreen(mouseX, mouseY, partialTicks);
		mc.fontRenderer.drawString(getTranslation(gui_create_playlist_add_list), 20, 65, 0xFFFFFF);
		namePlaylistField.drawTextField(mouseX, mouseY, partialTicks);
		controls.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlayerList getPlaylistsList() {
		return playlistsList;
	}
	
}
