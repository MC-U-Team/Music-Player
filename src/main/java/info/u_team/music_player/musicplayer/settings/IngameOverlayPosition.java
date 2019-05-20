package info.u_team.music_player.musicplayer.settings;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

public enum IngameOverlayPosition {
	
	UP_LEFT(gui_settings_position_up_left),
	UP_RIGHT(gui_settings_position_up_right),
	DOWN_RIGHT(gui_settings_position_down_right),
	DOWN_LEFT(gui_settings_position_down_left);
	
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
