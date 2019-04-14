package info.u_team.music_player.gui.playlist.search;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.google.common.base.Splitter;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.to_export_to_u_team_core.gui.GuiButtonExtImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiMusicSearch extends GuiScreen {
	
	private final SearchList tracks;
	
	private int halfWidth;
	
	private GuiTextField urlfield, searchfield;
	
	private GuiMusicSearchList list;
	
	private SearchProvider searchprovider;
	
	public GuiMusicSearch() {
		tracks = new SearchList();
		searchprovider = SearchProvider.YOUTUBE;
		list = new GuiMusicSearchList(tracks, Minecraft.getInstance(), 0, 0, 0, 0, 20); // We need that this list persists even when rescaling because of some metadata
	}
	
	@Override
	protected void initGui() {
		mc.keyboardListener.enableRepeatEvents(true);
		
		if (halfWidth == 0) {
			halfWidth = width / 2;
		}
		
		urlfield = new GuiTextField(1, mc.fontRenderer, 10, 55, halfWidth - 10, 20);
		urlfield.setMaxStringLength(10000);
		children.add(urlfield);
		
		addButton(new GuiButtonExt(2, halfWidth + 10, 54, halfWidth - 20, 22, "Open local file") {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				String response = TinyFileDialogs.tinyfd_openFileDialog("Open files", null, null, "Music files", true);
				if (response != null) {
					Splitter.on('|').omitEmptyStrings().split(response).forEach(GuiMusicSearch.this::loadTrack);
				}
			}
		});
		
		addButton(new GuiButtonExtImage(3, 10, 98, 24, 24, searchprovider.getLogo()) {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				searchprovider = SearchProvider.toggle(searchprovider);
				setResource(searchprovider.getLogo());
			}
		});
		
		searchfield = new GuiTextField(4, mc.fontRenderer, 40, 100, width - 50, 20);
		searchfield.setMaxStringLength(1000);
		searchfield.setFocused(true);
		children.add(searchfield);
		
		addButton(new GuiButtonExt(5, width - 110, 130, 100, 20, "Add all") {
			
			@Override
			public void onClick(double mouseX, double mouseY) {
				enabled = false;
			}
			
			@Override
			public void render(int mouseX, int mouseY, float partial) {
				super.render(mouseX, mouseY, partial);
				if (!enabled && hovered) {
					drawHoveringText("You already added all songs", mouseX, mouseY);
					RenderHelper.disableStandardItemLighting(); // Fix darker gui
				}
			}
		});
		
		list.width = width - 24;
		list.height = height;
		list.top = 160;
		list.bottom = height - 10;
		list.right = width - 12;
		children.add(list);
		
		super.initGui();
	}
	
	@Override
	public void onGuiClosed() {
		mc.keyboardListener.enableRepeatEvents(false);
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		String urlfieldText = urlfield.getText();
		String searchFieldText = searchfield.getText();
		halfWidth = width / 2;
		this.setWorldAndResolution(minecraft, width, height);
		urlfield.setText(urlfieldText);
		searchfield.setText(searchFieldText);
	}
	
	@Override
	public void tick() {
		urlfield.tick();
		searchfield.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		list.drawScreen(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
		urlfield.drawTextField(mouseX, mouseY, partialTicks);
		searchfield.drawTextField(mouseX, mouseY, partialTicks);
		drawCenteredString(mc.fontRenderer, "Add new tracks", halfWidth, 10, 0xFFFFFF);
		drawString(mc.fontRenderer, "Enter url to track", 10, 40, 0xFFFFFF);
		drawString(mc.fontRenderer, "Open file explorer", 10 + halfWidth, 40, 0xFFFFFF);
		drawString(mc.fontRenderer, "Search for track", 10, 85, 0xFFFFFF);
	}
	
	@Override
	public boolean charTyped(char character, int p_charTyped_2_) {
		if (urlfield.isFocused()) {
			return urlfield.charTyped(character, p_charTyped_2_);
		} else if (searchfield.isFocused()) {
			return searchfield.charTyped(character, p_charTyped_2_);
		}
		return super.charTyped(character, p_charTyped_2_);
	}
	
	@Override
	public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
		if (urlfield.isFocused()) {
			if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
				loadTrack(urlfield.getText());
				urlfield.setText("");
				return false;
			} else if (urlfield.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_)) {
				return true;
			}
		} else if (searchfield.isFocused()) {
			if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
				searchTrack(searchfield.getText());
				searchfield.setText("");
				return false;
			} else if (searchfield.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_)) {
				return true;
			}
		}
		return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	private void loadTrack(String uri) {
		MusicPlayerManager.player.getTrackSearch().getTracks(uri, searchresult -> {
			if (searchresult.hasError()) {
				System.out.println(searchresult.getErrorMessage());
			} else {
				searchresult.getTracks().forEach(tracks::add);
			}
		});
	}
	
	private void searchTrack(String key) {
		String search = searchprovider.getPrefix() + key;
		loadTrack(search);
	}
}
