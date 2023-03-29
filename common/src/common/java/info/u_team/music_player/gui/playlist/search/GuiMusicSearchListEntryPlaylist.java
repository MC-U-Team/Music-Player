package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADDED_LIST;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_TRACK_DURATION_UNDEFINED;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.gui.util.GuiTrackUtils;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackList;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.ChatFormatting;

public class GuiMusicSearchListEntryPlaylist extends GuiMusicSearchListEntry {
	
	private final IAudioTrackList trackList;
	
	private final String name;
	private final String duration;
	
	public GuiMusicSearchListEntryPlaylist(GuiMusicSearch gui, Playlist playlist, IAudioTrackList trackList) {
		this.trackList = trackList;
		name = trackList.getName();
		
		final List<IAudioTrack> tracks = trackList.getTracks();
		
		if (!tracks.parallelStream().anyMatch(track -> track.getInfo().isStream())) {
			duration = TimeUtil.timeConversion(tracks.parallelStream().mapToLong(track -> track.getDuration()).sum() / 1000);
		} else {
			duration = getTranslation(GUI_TRACK_DURATION_UNDEFINED);
		}
		
		addTrackButton.setPressable(() -> {
			playlist.add(trackList);
			gui.setInformation(ChatFormatting.GREEN + getTranslation(GUI_SEARCH_ADDED_LIST), 150);
		});
	}
	
	@Override
	public void render(PoseStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		super.render(matrixStack, slotIndex, entryY, entryX, entryWidth, entryHeight, mouseX, mouseY, hovered, partialTicks);
		minecraft.font.draw(matrixStack, name, entryX + 5, entryY + 15, 0xF4E242);
		minecraft.font.draw(matrixStack, duration, entryX + entryWidth - 135, entryY + 5, 0xFFFF00);
	}
	
	public IAudioTrackList getTrackList() {
		return trackList;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 2) {
			final String uri = trackList.getUri();
			if (GuiTrackUtils.openURI(uri)) {
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
