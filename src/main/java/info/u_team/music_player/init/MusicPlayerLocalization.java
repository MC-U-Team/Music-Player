package info.u_team.music_player.init;

import net.minecraft.client.resources.language.I18n;

public class MusicPlayerLocalization {
	
	// Keys
	public static final String KEY_CATEGORY = "key.musicplayer.category";
	public static final String KEY_OPEN = "key.musicplayer.open";
	public static final String KEY_PAUSE = "key.musicplayer.pause";
	public static final String KEY_SKIP_FORWARD = "key.musicplayer.skip.forward";
	public static final String KEY_SKIP_BACK = "key.musicplayer.skip.back";
	
	// Guis
	public static final String GUI_CREATE_PLAYLIST_INSERT_NAME = "gui.musicplayer.create_playlist.insert_name";
	public static final String GUI_CREATE_PLAYLIST_ADD_LIST = "gui.musicplayer.create_playlist.add_list";
	
	public static final String GUI_PLAYLISTS_NO_NAME = "gui.musicplayer.playlists.no_name";
	public static final String GUI_PLAYLISTS_ENTRY = "gui.musicplayer.playlists.entry";
	public static final String GUI_PLAYLISTS_ENTRIES = "gui.musicplayer.playlists.entries";
	
	public static final String GUI_CONTROLS_VOLUME = "gui.controls.volume";
	
	public static final String GUI_PLAYLIST_LOADING = "gui.playlist.loading";
	
	public static final String GUI_TRACK_DURATION_UNDEFINED = "gui.track.duration_undefined";
	
	public static final String GUI_SEARCH_HEADER = "gui.search.header";
	public static final String GUI_SEARCH_LOAD_FILE = "gui.search.load.file";
	public static final String GUI_SEARCH_LOAD_FOLDER = "gui.search.load.folder";
	public static final String GUI_SEARCH_MUSIC_FILES = "gui.search.music_files";
	public static final String GUI_SEARCH_ADD_ALL = "gui.search.add_all";
	public static final String GUI_SEARCH_ADDED = "gui.search.added";
	public static final String GUI_SEARCH_ADDED_LIST = "gui.search.added_list";
	public static final String GUI_SEARCH_ADDED_ALL = "gui.search.added_all";
	public static final String GUI_SEARCH_SEARCH_URI = "gui.search.search.uri";
	public static final String GUI_SEARCH_SEARCH_FILE = "gui.search.search.file";
	public static final String GUI_SEARCH_SEARCH_SEARCH = "gui.search.search.search";
	
	public static final String GUI_SETTINGS_TOGGLE_INGAME_OVERLAY = "gui.settings.toggle.ingame_overlay";
	public static final String GUI_SETTINGS_TOGGLE_MENUE_OVERLAY = "gui.settings.toggle.menue_overlay";
	public static final String GUI_SETTINGS_TOGGLE_KEY_IN_GUI = "gui.settings.toggle.key_in_gui";
	public static final String GUI_SETTINGS_POSITION_OVERLAY = "gui.settings.position.overlay";
	public static final String GUI_SETTINGS_POSITION_UP_LEFT = "gui.settings.position.up_left";
	public static final String GUI_SETTINGS_POSITION_UP_RIGHT = "gui.settings.position.up_right";
	public static final String GUI_SETTINGS_POSITION_DOWN_RIGHT = "gui.settings.position.down_right";
	public static final String GUI_SETTINGS_POSITION_DOWN_LEFT = "gui.settings.position.down_left";
	public static final String GUI_SETTINGS_MIXER_DEVICE_SELECTION = "gui.settings.mixer_device_selection";
	
	public static String getTranslation(String key, Object... parameters) {
		return I18n.get(key, parameters);
	}
	
}
