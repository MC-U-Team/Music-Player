package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_add_all;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_added_all;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_header;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_load_file;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_load_folder;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_search_file;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_search_search;
import static info.u_team.music_player.init.MusicPlayerLocalization.gui_search_search_uri;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lwjgl.input.Keyboard;

import info.u_team.music_player.dependency.TinyFdHelper;
import info.u_team.music_player.gui.playlist.GuiMusicPlaylist;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackList;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.u_team_core.gui.elements.GuiButtonClick;
import info.u_team.u_team_core.gui.elements.GuiButtonClickImage;
import info.u_team.u_team_core.gui.elements.backport.GuiScreen1_13;
import info.u_team.u_team_core.gui.elements.backport.GuiTextFieldNew;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.util.text.TextFormatting;

public class GuiMusicSearch extends GuiScreen1_13 {
	
	private final Playlist playlist;
	
	private GuiTextFieldNew urlField;
	private GuiTextFieldNew searchField;
	
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
	public void initGui() {
		final GuiButtonClick backButton = addNewButton(new GuiButtonClickImage(1, 1, 15, 15, MusicPlayerResources.textureBack));
		backButton.setClickAction(() -> mc.displayGuiScreen(new GuiMusicPlaylist(playlist)));
		
		urlField = new GuiTextFieldNew(-1, mc.fontRenderer, 10, 35, width / 2 - 10, 20) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
		};
		urlField.setMaxStringLength(10000);
		children.add(urlField);
		
		final Language language = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage();
		final String lang = language != null ? language.getLanguageCode() : null;
		
		final GuiButtonClick openFileButton = addNewButton(new GuiButtonClick(width / 2 + 10, 34, width / 4 - 15, 22, getTranslation(gui_search_load_file)));
		openFileButton.setClickAction(() -> {
			final String response = TinyFdHelper.openFileDialog(getSearchLoadFileTitle(lang), null, null, getSearchLoadFiles(lang), false);
			if (response != null) {
				searchList.clear();
				addTrack(response);
			}
		});
		
		final GuiButtonClick openFolderButton = addNewButton(new GuiButtonClick((int) (width * 0.75) + 5, 34, width / 4 - 15, 22, getTranslation(gui_search_load_folder)));
		openFolderButton.setClickAction(() -> {
			final String response = TinyFdHelper.selectFolderDialog(getSearchLoadFolderTitle(lang), System.getProperty("user.home"));
			if (response != null) {
				searchList.clear();
				try (Stream<Path> stream = Files.list(Paths.get(response))) {
					stream.filter(path -> !Files.isDirectory(path)).forEach(path -> addTrack(path.toString()));
				} catch (IOException ex) {
					setInformation(TextFormatting.RED + ex.getMessage(), 150);
				}
			}
		});
		
		final GuiButtonClickImage searchButton = addNewButton(new GuiButtonClickImage(10, 76, 24, 24, searchProvider.getLogo()));
		searchButton.setClickAction(() -> {
			searchProvider = SearchProvider.toggle(searchProvider);
			searchButton.setResource(searchProvider.getLogo());
		});
		
		searchField = new GuiTextFieldNew(-1, mc.fontRenderer, 40, 78, width - 51, 20) {
			
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
		
		final GuiButtonClick addAllButton = addNewButton(new GuiButtonClick(width - 110, 105, 100, 20, getTranslation(gui_search_add_all)));
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
			setInformation(TextFormatting.GREEN + getTranslation(gui_search_added_all), 150);
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
	public void updateScreen() {
		urlField.tick();
		searchField.tick();
		informationTicks++;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		searchList.drawScreen(mouseX, mouseY, partialTicks);
		
		drawCenteredString(mc.fontRenderer, getTranslation(gui_search_header), width / 2, 5, 0xFFFFFF);
		drawString(mc.fontRenderer, getTranslation(gui_search_search_uri), 10, 20, 0xFFFFFF);
		drawString(mc.fontRenderer, getTranslation(gui_search_search_file), 10 + width / 2, 20, 0xFFFFFF);
		drawString(mc.fontRenderer, getTranslation(gui_search_search_search), 10, 63, 0xFFFFFF);
		
		if (information != null && informationTicks <= maxTicksInformation) {
			drawString(mc.fontRenderer, information, 15, 110, 0xFFFFFF);
		}
		
		urlField.drawTextField(mouseX, mouseY, partialTicks);
		searchField.drawTextField(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void setInformation(String information, int maxTicksInformation) {
		this.information = information;
		this.maxTicksInformation = maxTicksInformation;
		informationTicks = 0;
	}
	
	private void keyFromTextField(GuiTextFieldNew field, String text, int key) {
		if (field.getVisible() && field.isFocused() && (key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER)) {
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
	
	/**
	 * This method exists instead of a normal translation due to a vulnerability in the TinyFileDialogs library allowing for
	 * command injection.
	 */
	private static String getSearchLoadFileTitle(String lang) {
		switch (lang) {
		case "jp_jp":
			return "ファイルを読み込む";
		case "ko_kr":
			return "파일 불러오기";
		case "pt_br":
			return "Subir arquivo";
		case "ru_ru":
			return "Загрузить файл";
		case "zn_cn":
			return "加载文件";
		case "zn_tw":
			return "載入檔案";
		case "de_de":
			return "Lade eine Datei";
		default:
			return "Load file";
		}
	}
	
	/**
	 * This method exists instead of a normal translation due to a vulnerability in the TinyFileDialogs library allowing for
	 * command injection.
	 */
	private static String getSearchLoadFiles(String lang) {
		switch (lang) {
		case "jp_jp":
			return "ミュージックファイル";
		case "ko_kr":
			return "음악 파일";
		case "pt_br":
			return "arquivos de música";
		case "ru_ru":
			return "Файлы с музыкой";
		case "zn_cn":
			return "音乐文件";
		case "zn_tw":
			return "音樂檔案";
		case "de_de":
			return "Musikdatein";
		default:
			return "Music files";
		}
	}
	
	/**
	 * This method exists instead of a normal translation due to a vulnerability in the TinyFileDialogs library allowing for
	 * command injection.
	 */
	private static String getSearchLoadFolderTitle(String lang) {
		switch (lang) {
		case "jp_jp":
			return "フォルダを読み込む";
		case "ko_kr":
			return "폴더 불러오기";
		case "pt_br":
			return "Pasta de download";
		case "ru_ru":
			return "Загрузить папку";
		case "zn_cn":
			return "加载文件夹";
		case "zn_tw":
			return "載入資料夾";
		case "de_de":
			return "Lade einen Ordner";
		default:
			return "Load folder";
		}
	}
}