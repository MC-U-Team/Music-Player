package info.u_team.music_player.init;

import net.minecraft.client.resources.I18n;

public class MusicPlayerLocalization {
	
	// Keys
	public static final String key_category = "key.musicplayer.category";
	public static final String key_open = "key.musicplayer.open";
	public static final String key_pause = "key.musicplayer.pause";
	public static final String key_skip_forward = "key.musicplayer.skip.forward";
	public static final String key_skip_back = "key.musicplayer.skip.back";
	
	// Guis
	public static final String gui_create_playlist_insert_name = "gui.musicplayer.create_playlist.insert_name";
	public static final String gui_create_playlist_add_list = "gui.musicplayer.create_playlist.add_list";
	
	public static final String gui_playlists_no_name = "gui.musicplayer.playlists.no_name";
	public static final String gui_playlists_entry = "gui.musicplayer.playlists.entry";
	public static final String gui_playlists_entries = "gui.musicplayer.playlists.entries";
	
	public static final String gui_controls_volume = "gui.controls.volume";
	
	public static final String gui_playlist_loading = "gui.playlist.loading";
	
	public static final String gui_track_duration_undefined = "gui.track.duration_undefined";
	
	public static final String gui_search_header = "gui.search.header";
	public static final String gui_search_load_file = "gui.search.load.file";
	public static final String gui_search_load_folder = "gui.search.load.folder";
	public static final String gui_search_music_files = "gui.search.music_files";
	public static final String gui_search_add_all = "gui.search.add_all";
	public static final String gui_search_added = "gui.search.added";
	public static final String gui_search_added_list = "gui.search.added_list";
	public static final String gui_search_added_all = "gui.search.added_all";
	public static final String gui_search_search_uri = "gui.search.search.uri";
	public static final String gui_search_search_file = "gui.search.search.file";
	public static final String gui_search_search_search = "gui.search.search.search";
	
	public static final String gui_settings_toggle_ingame_overlay = "gui.settings.toggle.ingame_overlay";
	public static final String gui_settings_toggle_menue_overlay = "gui.settings.toggle.menue_overlay";
	public static final String gui_settings_toggle_key_in_gui = "gui.settings.toggle.key_in_gui";
	public static final String gui_settings_position_overlay = "gui.settings.position.overlay";
	public static final String gui_settings_position_up_left = "gui.settings.position.up_left";
	public static final String gui_settings_position_up_right = "gui.settings.position.up_right";
	public static final String gui_settings_position_down_right = "gui.settings.position.down_right";
	public static final String gui_settings_position_down_left = "gui.settings.position.down_left";
	
	public static String getTranslation(String key, Object... parameters) {
		return I18n.format(key, parameters);
	}
	
}
