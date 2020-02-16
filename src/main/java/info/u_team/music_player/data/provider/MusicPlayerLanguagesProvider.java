package info.u_team.music_player.data.provider;

import info.u_team.u_team_core.data.*;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

public class MusicPlayerLanguagesProvider extends CommonLanguagesProvider {
	
	public MusicPlayerLanguagesProvider(GenerationData data) {
		super(data);
	}
	
	@Override
	public void addTranslations() {
		// English
		
		// Keys
		add(KEY_CATEGORY, "Music Player");
		add(KEY_OPEN, "Open Music Player");
		add(KEY_PAUSE, "Pause Music Player");
		add(KEY_SKIP_FORWARD, "Skip Forward");
		add(KEY_SKIP_BACK, "Skip Back");
		
		// Guis
		add(GUI_CREATE_PLAYLIST_INSERT_NAME, "Enter a name");
		add(GUI_CREATE_PLAYLIST_ADD_LIST, "Add playlist");
		
		add(GUI_PLAYLISTS_NO_NAME, "No name");
		add(GUI_PLAYLISTS_ENTRY, "Entry");
		add(GUI_PLAYLISTS_ENTRIES, "Entries");
		
		add(GUI_CONTROLS_VOLUME, "Volume");
		
		add(GUI_PLAYLIST_LOADING, "Loading tracks ...");
		
		add(GUI_TRACK_DURATION_UNDEFINED, "undefined");
		
		add(GUI_SEARCH_HEADER, "Add new tracks");
		add(GUI_SEARCH_LOAD_FILE, "Load file");
		add(GUI_SEARCH_LOAD_FOLDER, "Load folder");
		add(GUI_SEARCH_MUSIC_FILES, "Music files");
		add(GUI_SEARCH_ADD_ALL, "Add all");
		add(GUI_SEARCH_ADDED, "Added track");
		add(GUI_SEARCH_ADDED_LIST, "Added track list");
		add(GUI_SEARCH_ADDED_ALL, "Added all tracks");
		add(GUI_SEARCH_SEARCH_URI, "Enter url to track");
		add(GUI_SEARCH_SEARCH_FILE, "Open file explorer");
		add(GUI_SEARCH_SEARCH_SEARCH, "Search for track");
		
		add(GUI_SETTINGS_TOGGLE_INGAME_OVERLAY, "Toggle ingame overlay");
		add(GUI_SETTINGS_TOGGLE_MENUE_OVERLAY, "Toggle menue overlay");
		add(GUI_SETTINGS_TOGGLE_KEY_IN_GUI, "Toggle keybinds in gui");
		add(GUI_SETTINGS_POSITION_OVERLAY, "Ingame overlay position");
		add(GUI_SETTINGS_POSITION_UP_LEFT, "Top left");
		add(GUI_SETTINGS_POSITION_UP_RIGHT, "Top right");
		add(GUI_SETTINGS_POSITION_DOWN_RIGHT, "Bottom right");
		add(GUI_SETTINGS_POSITION_DOWN_LEFT, "Bottom left");
	}
	
}
