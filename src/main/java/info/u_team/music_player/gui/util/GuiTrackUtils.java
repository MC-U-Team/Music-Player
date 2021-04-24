package info.u_team.music_player.gui.util;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import java.net.URI;
import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.music_player.lavaplayer.api.audio.*;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

public final class GuiTrackUtils {
	
	private static final Minecraft MINECRAFT = Minecraft.getInstance();
	
	public static String trimToWith(String string, int width) {
		String newString = MINECRAFT.fontRenderer.trimStringToWidth(string, width);
		if (!newString.equals(string)) {
			newString += "...";
		}
		return newString;
	}
	
	public static void addTrackInfo(MatrixStack matrixStack, IAudioTrack track, int x, int y, int entryWidth, int leftMargin, int titleColor) {
		final int textSize = entryWidth - 150 - leftMargin;
		
		final IAudioTrackInfo info = track.getInfo();
		
		final String title = trimToWith(info.getFixedTitle(), textSize);
		final String author = trimToWith(info.getFixedAuthor(), textSize);
		final String duration = getFormattedDuration(track);
		
		MINECRAFT.fontRenderer.drawString(matrixStack, title, x + leftMargin, y + 5, titleColor);
		MINECRAFT.fontRenderer.drawString(matrixStack, author, x + leftMargin + 4, y + 25, 0xD86D1C);
		MINECRAFT.fontRenderer.drawString(matrixStack, duration, x + entryWidth - 140, y + 15, 0xFFFF00);
	}
	
	public static boolean openURI(String uri) {
		Style style = ITextComponent.getTextComponentOrEmpty(null).getStyle();
		try {
			new URI(uri);
			style = style.setClickEvent(new ClickEvent(Action.OPEN_URL, uri));
		} catch (final Exception ex) {
			style = style.setClickEvent(new ClickEvent(Action.OPEN_FILE, uri));
		}
		return MINECRAFT.currentScreen.handleComponentClicked(style);
	}
	
	public static String getFormattedDuration(IAudioTrack track) {
		final long seconds;
		if (track == null) {
			seconds = 0;
		} else {
			final IAudioTrackInfo info = track.getInfo();
			if (info.isStream()) {
				return getTranslation(GUI_TRACK_DURATION_UNDEFINED);
			}
			seconds = track.getDuration() / 1000;
		}
		return TimeUtil.timeConversion(seconds);
	}
	
	public static String getFormattedPosition(IAudioTrack track) {
		return TimeUtil.timeConversion(track == null ? 0 : track.getPosition() / 1000);
	}
	
	public static <T> T getValueOfPlayingTrack(Function<IAudioTrack, T> function) {
		return getValueOfNullableTrack(MusicPlayerManager.getPlayer().getTrackManager().getCurrentTrack(), function);
	}
	
	public static <T> T getValueOfNullableTrack(IAudioTrack track, Function<IAudioTrack, T> function) {
		if (track != null) {
			return function.apply(track);
		}
		return null;
	}
	
}
