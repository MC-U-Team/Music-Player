package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import info.u_team.music_player.gui.playlist.GuiMusicPlaylist;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.*;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.*;

public class GuiMusicSearch extends Screen {
	
	private final Playlist playlist;
	
	private TextFieldWidget urlField;
	private TextFieldWidget searchField;
	
	private final GuiMusicSearchList searchList;
	
	private SearchProvider searchProvider;
	
	private String information;
	private int informationTicks;
	private int maxTicksInformation;
	
	public GuiMusicSearch(Playlist playlist) {
		super(new StringTextComponent("musicsearch"));
		this.playlist = playlist;
		searchList = new GuiMusicSearchList();
		searchProvider = SearchProvider.YOUTUBE;
	}
	
	@Override
	protected void init() {
		final GuiButtonClick backButton = addButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> minecraft.displayGuiScreen(new GuiMusicPlaylist(playlist)));
		
		urlField = new TextFieldWidget(font, 10, 35, width / 2 - 10, 20, null) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
		};
		urlField.setMaxStringLength(10000);
		children.add(urlField);
		
		final GuiButtonClick openFileButton = addButton(new GuiButtonClick(width / 2 + 10, 34, width / 4 - 15, 22, getTranslation(gui_search_load_file)));
		openFileButton.setClickAction(() -> {
			String response = TinyFileDialogs.tinyfd_openFileDialog(getTranslation(gui_search_load_file), null, null, getTranslation(gui_search_music_files), false);
			if (response != null) {
				searchList.clear();
				addTrack(response);
			}
		});
		
		final GuiButtonClick openFolderButton = addButton(new GuiButtonClick((int) (width * 0.75) + 5, 34, width / 4 - 15, 22, getTranslation(gui_search_load_folder)));
		openFolderButton.setClickAction(() -> {
			String response = TinyFileDialogs.tinyfd_selectFolderDialog(getTranslation(gui_search_load_folder), System.getProperty("user.home"));
			if (response != null) {
				searchList.clear();
				try (Stream<Path> stream = Files.list(Paths.get(response))) {
					stream.filter(path -> !Files.isDirectory(path)).forEach(path -> addTrack(path.toString()));
				} catch (IOException ex) {
					setInformation(TextFormatting.RED + ex.getMessage(), 150);
				}
			}
		});
		
		final GuiButtonClickImage searchButton = addButton(new GuiButtonClickImage(10, 76, 24, 24, searchProvider.getLogo()));
		searchButton.setClickAction(() -> {
			searchProvider = SearchProvider.toggle(searchProvider);
			searchButton.setResource(searchProvider.getLogo());
		});
		
		searchField = new  TextFieldWidget(font, 40, 78, width - 51, 20, null) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, searchProvider.getPrefix() + getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
		};
		searchField.setMaxStringLength(1000);
		searchField.setFocused2(true);
		setFocused(searchField);
		children.add(searchField);
		
		final GuiButtonClick addAllButton = addButton(new GuiButtonClick(width - 110, 105, 100, 20, getTranslation(gui_search_add_all)));
		addAllButton.setClickAction(() -> {
			List<GuiMusicSearchListEntryPlaylist> list = searchList.children().stream().filter(entry -> entry instanceof GuiMusicSearchListEntryPlaylist).map(entry -> (GuiMusicSearchListEntryPlaylist) entry).collect(Collectors.toList());
			if (list.size() > 0) {
				list.forEach(entry -> {
					playlist.add(entry.getTrackList());
				});
			} else {
				searchList.children().stream().filter(entry -> entry instanceof GuiMusicSearchListEntryMusicTrack).map(entry -> (GuiMusicSearchListEntryMusicTrack) entry).filter(entry -> !entry.isPlaylistEntry()).forEach(entry -> {
					playlist.add(entry.getTrack());
				});
			}
			setInformation(TextFormatting.GREEN + getTranslation(gui_search_added_all), 150);
		});
		
		searchList.updateSettings(width - 24, height, 130, height - 10, 12, width - 12);
		children.add(searchList);
	}
	
	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		final String urlFieldText = urlField.getText();
		final boolean urlFieldFocus = urlField.isFocused() && getFocused() == urlField;
		
		final String searchFieldText = searchField.getText();
		final boolean searchFieldFocus = searchField.isFocused() && getFocused() == searchField;
		
		init(minecraft, width, height);
		
		urlField.setText(urlFieldText);
		urlField.setFocused2(urlFieldFocus);
		if (urlFieldFocus) {
			setFocused(urlField);
		}
		
		searchField.setText(searchFieldText);
		searchField.setFocused2(searchFieldFocus);
		if (searchFieldFocus) {
			setFocused(searchField);
		}
		
	}
	
	@Override
	public void tick() {
		urlField.tick();
		searchField.tick();
		informationTicks++;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		searchList.render(mouseX, mouseY, partialTicks);
		
		drawCenteredString(minecraft.fontRenderer, getTranslation(gui_search_header), width / 2, 5, 0xFFFFFF);
		drawString(minecraft.fontRenderer, getTranslation(gui_search_search_uri), 10, 20, 0xFFFFFF);
		drawString(minecraft.fontRenderer, getTranslation(gui_search_search_file), 10 + width / 2, 20, 0xFFFFFF);
		drawString(minecraft.fontRenderer, getTranslation(gui_search_search_search), 10, 63, 0xFFFFFF);
		
		if (information != null && informationTicks <= maxTicksInformation) {
			drawString(minecraft.fontRenderer, information, 15, 110, 0xFFFFFF);
		}
		
		urlField.render(mouseX, mouseY, partialTicks);
		searchField.render(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	public void setInformation(String information, int maxTicksInformation) {
		this.information = information;
		this.maxTicksInformation = maxTicksInformation;
		informationTicks = 0;
	}
	
	private void keyFromTextField(TextFieldWidget field, String text, int key) {
		if (field.getVisible() && field.isFocused() && (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER)) {
			searchList.clear();
			addTrack(text);
			field.setText("");
		}
	}
	
	private void addTrack(String uri) {
		MusicPlayerManager.getPlayer().getTrackSearch().getTracks(uri, result -> {
			minecraft.execute(() -> {
				if (result.hasError()) {
					setInformation(TextFormatting.RED + result.getErrorMessage(), 150);
				} else if (result.isList()) {
					final IAudioTrackList list = result.getTrackList();
					if (!list.isSearch()) {
						searchList.add(new GuiMusicSearchListEntryPlaylist(this, playlist, list));
					}
					list.getTracks().forEach(track -> searchList.add(new GuiMusicSearchListEntryMusicTrack(this, playlist, track, !list.isSearch())));
				} else {
					final IAudioTrack track = result.getTrack();
					searchList.add(new GuiMusicSearchListEntryMusicTrack(this, playlist, track, false));
				}
			});
		});
	}
}
