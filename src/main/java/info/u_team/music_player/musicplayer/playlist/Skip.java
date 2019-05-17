package info.u_team.music_player.musicplayer.playlist;

public enum Skip {

	FORWARD(1), PREVIOUS(-1);

	private final int value;

	private Skip(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
