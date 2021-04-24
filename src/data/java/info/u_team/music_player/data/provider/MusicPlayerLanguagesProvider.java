package info.u_team.music_player.data.provider;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CONTROLS_VOLUME;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CREATE_PLAYLIST_ADD_LIST;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CREATE_PLAYLIST_INSERT_NAME;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLISTS_ENTRIES;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLISTS_ENTRY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLISTS_NO_NAME;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLIST_LOADING;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADDED;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADDED_ALL;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADDED_LIST;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADD_ALL;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_HEADER;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_LOAD_FILE;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_LOAD_FOLDER;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_MUSIC_FILES;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_SEARCH_FILE;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_SEARCH_SEARCH;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_SEARCH_URI;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_MIXER_DEVICE_SELECTION;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_DOWN_LEFT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_DOWN_RIGHT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_UP_LEFT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_UP_RIGHT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_INGAME_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_KEY_IN_GUI;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_MENUE_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_TRACK_DURATION_UNDEFINED;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_CATEGORY;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_OPEN;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_PAUSE;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_BACK;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_FORWARD;

import info.u_team.u_team_core.data.CommonLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;

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
		add(GUI_SETTINGS_MIXER_DEVICE_SELECTION, "Select audio device");
		
		// German
		
		// Keys
		add("de_de", KEY_CATEGORY, "Musikplayer");
		add("de_de", KEY_OPEN, "Öffne den Musikplayer");
		add("de_de", KEY_PAUSE, "Pausiere den Musikplayer");
		add("de_de", KEY_SKIP_FORWARD, "Nach vorne spulen");
		add("de_de", KEY_SKIP_BACK, "Zurück spulen");
		
		// Guis
		add("de_de", GUI_CREATE_PLAYLIST_INSERT_NAME, "Gebe einen Namen an");
		add("de_de", GUI_CREATE_PLAYLIST_ADD_LIST, "Füge eine Wiedergabeliste hinzu");
		
		add("de_de", GUI_PLAYLISTS_NO_NAME, "Kein Name");
		add("de_de", GUI_PLAYLISTS_ENTRY, "Eintrag");
		add("de_de", GUI_PLAYLISTS_ENTRIES, "Einträge");
		
		add("de_de", GUI_CONTROLS_VOLUME, "Lautstärke");
		
		add("de_de", GUI_PLAYLIST_LOADING, "Lade Audioeinträge ...");
		
		add("de_de", GUI_TRACK_DURATION_UNDEFINED, "nicht definiert");
		
		add("de_de", GUI_SEARCH_HEADER, "Füge neue Audioeinträge hinzu");
		add("de_de", GUI_SEARCH_LOAD_FILE, "Lade eine Datei");
		add("de_de", GUI_SEARCH_LOAD_FOLDER, "Lade einen Ordner");
		add("de_de", GUI_SEARCH_MUSIC_FILES, "Musikdatein");
		add("de_de", GUI_SEARCH_ADD_ALL, "Füge alle Audioeinträge hinzu");
		add("de_de", GUI_SEARCH_ADDED, "Audioeintrag hinzugefügt");
		add("de_de", GUI_SEARCH_ADDED_LIST, "Audioeintragliste hinzugefügt");
		add("de_de", GUI_SEARCH_ADDED_ALL, "Alle Audioeinträge hinzugefügt");
		add("de_de", GUI_SEARCH_SEARCH_URI, "Gebe eine URL zum Musikstück an");
		add("de_de", GUI_SEARCH_SEARCH_FILE, "Öffne den Dateimanager");
		add("de_de", GUI_SEARCH_SEARCH_SEARCH, "Suche nach Audiodatein");
		
		add("de_de", GUI_SETTINGS_TOGGLE_INGAME_OVERLAY, "Ingame-Overlay umschalten");
		add("de_de", GUI_SETTINGS_TOGGLE_MENUE_OVERLAY, "Menü-Overlay umschalten");
		add("de_de", GUI_SETTINGS_TOGGLE_KEY_IN_GUI, "Tastenkombinationen im Gui umschalten");
		add("de_de", GUI_SETTINGS_POSITION_OVERLAY, "Ingame-Overlay Position");
		add("de_de", GUI_SETTINGS_POSITION_UP_LEFT, "Oben Links");
		add("de_de", GUI_SETTINGS_POSITION_UP_RIGHT, "Oben Rechts");
		add("de_de", GUI_SETTINGS_POSITION_DOWN_RIGHT, "Unten Rechts");
		add("de_de", GUI_SETTINGS_POSITION_DOWN_LEFT, "Unten Links");
		add("de_de", GUI_SETTINGS_MIXER_DEVICE_SELECTION, "Wähle das Audiogerät");
	}
	
}
