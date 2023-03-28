package info.u_team.music_player.util;

public class TimeUtil {
	
	private final static int MINUTES_IN_AN_HOUR = 60;
	private final static int SECONDS_IN_A_MINUTE = 60;
	
	public static String timeConversion(long seconds) {
		long minutes = seconds / SECONDS_IN_A_MINUTE;
		seconds -= minutes * SECONDS_IN_A_MINUTE;
		
		final long hours = minutes / MINUTES_IN_AN_HOUR;
		minutes -= hours * MINUTES_IN_AN_HOUR;
		
		return (hours > 0 ? (hours + ":") : "") + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
	}
	
}
