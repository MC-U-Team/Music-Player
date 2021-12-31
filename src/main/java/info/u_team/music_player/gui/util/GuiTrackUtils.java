package info.u_team.music_player.gui.util;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_TRACK_DURATION_UNDEFINED;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import java.net.URI;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackInfo;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public final class GuiTrackUtils {
	
	private static final Minecraft MINECRAFT = Minecraft.getInstance();
	
	public static String trimToWith(String string, int width) {
		String newString = MINECRAFT.font.plainSubstrByWidth(string, width);
		if (!newString.equals(string)) {
			newString += "...";
		}
		return newString;
	}
	
	public static void addTrackInfo(PoseStack matrixStack, IAudioTrack track, int x, int y, int entryWidth, int leftMargin, int titleColor) {
		final int textSize = entryWidth - 150 - leftMargin;
		
		final IAudioTrackInfo info = track.getInfo();
		
		final String title = trimToWith(info.getFixedTitle(), textSize);
		final String author = trimToWith(info.getFixedAuthor(), textSize);
		final String duration = getFormattedDuration(track);
		
		MINECRAFT.font.draw(matrixStack, title, x + leftMargin, y + 5, titleColor);
		MINECRAFT.font.draw(matrixStack, author, x + leftMargin + 4, y + 25, 0xD86D1C);
		MINECRAFT.font.draw(matrixStack, duration, x + entryWidth - 140, y + 15, 0xFFFF00);
	}
	
	public static boolean openURI(String uri) {
		Style style = Component.nullToEmpty(null).getStyle();
		try {
			new URI(uri);
			style = style.withClickEvent(new ClickEvent(Action.OPEN_URL, uri));
		} catch (final Exception ex) {
			style = style.withClickEvent(new ClickEvent(Action.OPEN_FILE, uri));
		}
		return MINECRAFT.screen.handleComponentClicked(style);
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
