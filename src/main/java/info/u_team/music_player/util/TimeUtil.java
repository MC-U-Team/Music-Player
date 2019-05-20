package info.u_team.music_player.util;

public class TimeUtil {

	public static String timeConversion(long seconds) {

		final int MINUTES_IN_AN_HOUR = 60;
		final int SECONDS_IN_A_MINUTE = 60;

		long minutes = seconds / SECONDS_IN_A_MINUTE;
		seconds -= minutes * SECONDS_IN_A_MINUTE;

		long hours = minutes / MINUTES_IN_AN_HOUR;
		minutes -= hours * MINUTES_IN_AN_HOUR;

		return (hours > 0 ? (hours + ":") : "") + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
	}

}
