package info.u_team.music_player.gui.playlist.search;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.musicplayer.*;
import info.u_team.u_team_core.gui.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.text.TextFormatting;

public class GuiMusicSearch extends GuiScreen {
	
	private final Playlist playlist;
	
	private GuiTextField urlField;
	private GuiTextField searchField;
	
	private final GuiMusicSearchList searchList;
	
	private SearchProvider searchProvider;
	
	private String information;
	private int informationTicks;
	private int maxTicksInformation;
	
	public GuiMusicSearch(Playlist playlist) {
		this.playlist = playlist;
		searchList = new GuiMusicSearchList();
		searchProvider = SearchProvider.YOUTUBE;
	}
	
	@Override
	protected void initGui() {
		
		urlField = new GuiTextField(-1, mc.fontRenderer, 10, 35, width / 2 - 10, 20) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
		};
		urlField.setMaxStringLength(10000);
		children.add(urlField);
		
		final GuiButtonClick openFileButton = addButton(new GuiButtonClick(width / 2 + 10, 34, width / 4 - 15, 22, "Load file"));
		openFileButton.setClickAction(() -> {
			String response = TinyFileDialogs.tinyfd_openFileDialog("Load file", null, null, "Music files", false);
			if (response != null) {
				searchList.clear();
				addTrack(response);
			}
		});
		
		final GuiButtonClick openFolderButton = addButton(new GuiButtonClick((int) (width * 0.75) + 5, 34, width / 4 - 15, 22, "Load folder"));
		openFolderButton.setClickAction(() -> {
			String response = TinyFileDialogs.tinyfd_selectFolderDialog("Load folder", System.getProperty("user.home"));
			if (response != null) {
				searchList.clear();
				try {
					Files.walk(Paths.get(response)).filter(path -> !Files.isDirectory(path)).forEach(path -> addTrack(path.toString()));
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
		
		searchField = new GuiTextField(-1, mc.fontRenderer, 40, 78, width - 51, 20) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, searchProvider.getPrefix() + getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
		};
		searchField.setMaxStringLength(1000);
		searchField.setFocused(true);
		setFocused(searchField);
		children.add(searchField);
		
		final GuiButtonClick addAllButton = addButton(new GuiButtonClick(width - 110, 105, 100, 20, "Add all"));
		addAllButton.setClickAction(() -> {
			List<GuiMusicSearchListEntryPlaylist> list = searchList.getChildren().stream().filter(entry -> entry instanceof GuiMusicSearchListEntryPlaylist).map(entry -> (GuiMusicSearchListEntryPlaylist) entry).collect(Collectors.toList());
			if (list.size() > 0) {
				list.forEach(entry -> {
					playlist.add(entry.getTrackList());
				});
			} else {
				searchList.getChildren().stream().filter(entry -> entry instanceof GuiMusicSearchListEntryMusicTrack).map(entry -> (GuiMusicSearchListEntryMusicTrack) entry).filter(entry -> !entry.isPlaylistEntry()).forEach(entry -> {
					playlist.add(entry.getTrack());
				});
			}
			setInformation(TextFormatting.GREEN + "Added all tracks", 150);
		});
		
		searchList.updateSettings(width - 24, height, 130, height - 10, 12, width - 12);
		children.add(searchList);
		super.initGui();
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		final String urlFieldText = urlField.getText();
		final boolean urlFieldFocus = urlField.isFocused() && getFocused() == urlField;
		
		final String searchFieldText = searchField.getText();
		final boolean searchFieldFocus = searchField.isFocused() && getFocused() == searchField;
		
		setWorldAndResolution(minecraft, width, height);
		
		urlField.setText(urlFieldText);
		urlField.setFocused(urlFieldFocus);
		if (urlFieldFocus) {
			setFocused(urlField);
		}
		
		searchField.setText(searchFieldText);
		searchField.setFocused(searchFieldFocus);
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
		drawBackground(0);
		searchList.drawScreen(mouseX, mouseY, partialTicks);
		
		drawCenteredString(mc.fontRenderer, "Add new tracks", width / 2, 5, 0xFFFFFF);
		drawString(mc.fontRenderer, "Enter url to track", 10, 20, 0xFFFFFF);
		drawString(mc.fontRenderer, "Open file explorer", 10 + width / 2, 20, 0xFFFFFF);
		drawString(mc.fontRenderer, "Search for track", 10, 63, 0xFFFFFF);
		
		if (information != null && informationTicks <= maxTicksInformation) {
			drawString(mc.fontRenderer, information, 15, 110, 0xFFFFFF);
		}
		
		urlField.drawTextField(mouseX, mouseY, partialTicks);
		searchField.drawTextField(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	public void setInformation(String information, int maxTicksInformation) {
		this.information = information;
		this.maxTicksInformation = maxTicksInformation;
		informationTicks = 0;
	}
	
	private void keyFromTextField(GuiTextField field, String text, int key) {
		if (field.getVisible() && field.isFocused() && (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER)) {
			searchList.clear();
			addTrack(text);
			field.setText("");
		}
	}
	
	private void addTrack(String uri) {
		MusicPlayerManager.getPlayer().getTrackSearch().getTracks(uri, result -> {
			mc.addScheduledTask(() -> {
				if (result.hasError()) {
					setInformation(TextFormatting.RED + result.getErrorMessage(), 150);
					System.out.println(result.getErrorMessage());
					System.out.println(result.getUri());
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
