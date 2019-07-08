package info.u_team.music_player.gui.util;

import static info.u_team.music_player.init.MusicPlayerLocalization.*;

import java.lang.reflect.*;
import java.net.URI;
import java.util.function.Function;

import info.u_team.music_player.lavaplayer.api.audio.*;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public final class GuiTrackUtils {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	private static Method handleComponentClickMethod;
	static {
		final String name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName("GuiScreen", "func_175276_a", "(Lnet/minecraft/util/IChatComponent;)Z");
		try {
			handleComponentClickMethod = GuiScreen.class.getDeclaredMethod(name, IChatComponent.class);
			handleComponentClickMethod.setAccessible(true);
		} catch (NoSuchMethodException ex) {
			throw new IllegalStateException("The method " + name + " must be found to work!");
		}
	}
	
	public static String trimToWith(String string, int width) {
		String newString = mc.fontRendererObj.trimStringToWidth(string, width);
		if (!newString.equals(string)) {
			newString += "...";
		}
		return newString;
	}
	
	public static void addTrackInfo(IAudioTrack track, int x, int y, int entryWidth, int leftMargin, int titleColor) {
		
		final int textSize = entryWidth - 150 - leftMargin;
		
		final IAudioTrackInfo info = track.getInfo();
		
		final String title = trimToWith(info.getFixedTitle(), textSize);
		final String author = trimToWith(info.getFixedAuthor(), textSize);
		final String duration = getFormattedDuration(track);
		
		mc.fontRendererObj.drawString(title, x + leftMargin, y + 5, titleColor);
		mc.fontRendererObj.drawString(author, x + leftMargin + 4, y + 25, 0xD86D1C);
		mc.fontRendererObj.drawString(duration, x + entryWidth - 140, y + 15, 0xFFFF00);
	}
	
	public static boolean openURI(String uri) {
		final ChatStyle style = new ChatStyle();
		try {
			new URI(uri);
			style.setChatClickEvent(new ClickEvent(Action.OPEN_URL, uri));
		} catch (Exception ex) {
			style.setChatClickEvent(new ClickEvent(Action.OPEN_FILE, uri));
		}
		try {
			return (Boolean) handleComponentClickMethod.invoke(mc.currentScreen, new ChatComponentText("").setChatStyle(style));
		} catch (Exception ex) {
			throw new IllegalStateException("The method handleComponentClick in GuiScreen must be accessable. This should never happen!");
		}
	}
	
	public static String getFormattedDuration(IAudioTrack track) {
		final long seconds;
		if (track == null) {
			seconds = 0;
		} else {
			final IAudioTrackInfo info = track.getInfo();
			if (info.isStream()) {
				return getTranslation(gui_track_duration_undefined);
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
