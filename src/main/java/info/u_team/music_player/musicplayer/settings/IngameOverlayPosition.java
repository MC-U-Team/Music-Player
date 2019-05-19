package info.u_team.music_player.musicplayer.settings;

public enum IngameOverlayPosition {
	
	UP_LEFT("up_left"),
	UP_RIGHT("up_right"),
	DOWN_RIGHT("down_right"),
	DOWN_LEFT("down_left");
	
	private final String name;
	
	private IngameOverlayPosition(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
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
