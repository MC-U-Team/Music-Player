package info.u_team.music_player.musicplayer.settings;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_DOWN_LEFT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_DOWN_RIGHT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_UP_LEFT;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_UP_RIGHT;

public enum IngameOverlayPosition {
	
	UP_LEFT(GUI_SETTINGS_POSITION_UP_LEFT),
	UP_RIGHT(GUI_SETTINGS_POSITION_UP_RIGHT),
	DOWN_RIGHT(GUI_SETTINGS_POSITION_DOWN_RIGHT),
	DOWN_LEFT(GUI_SETTINGS_POSITION_DOWN_LEFT);
	
	private final String localization;
	
	private IngameOverlayPosition(String localization) {
		this.localization = localization;
	}
	
	public String getLocalization() {
		return localization;
	}
	
	public boolean isUp() {
		return this == UP_LEFT || this == UP_RIGHT;
	}
	
	public boolean isLeft() {
		return this == UP_LEFT || this == DOWN_LEFT;
	}
	
	public static IngameOverlayPosition forwardCycle(IngameOverlayPosition position) {
		if (position == UP_LEFT) {
			position = UP_RIGHT;
		} else if (position == UP_RIGHT) {
			position = DOWN_RIGHT;
		} else if (position == DOWN_RIGHT) {
			position = DOWN_LEFT;
		} else {
			position = UP_LEFT;
		}
		return position;
	}
	
}
