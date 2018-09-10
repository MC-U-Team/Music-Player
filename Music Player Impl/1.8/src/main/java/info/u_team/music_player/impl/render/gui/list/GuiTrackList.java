package info.u_team.music_player.impl.render.gui.list;

import java.util.*;

import com.sedmelluq.discord.lavaplayer.track.*;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.impl.render.gui.GuiMusicPlayer;
import info.u_team.music_player.impl.util.TimeUtil;
import net.hycrafthd.basiclavamusicplayer.queue.TrackScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiTrackList extends GuiScrollingList {
	
	private GuiMusicPlayer gui;
	
	private TrackScheduler trackscheduler;
	
	private AudioTrack hovering;
	
	public GuiTrackList(GuiMusicPlayer gui, TrackScheduler trackscheduler, Minecraft minecraft, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
		super(minecraft, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		this.gui = gui;
		this.trackscheduler = trackscheduler;
	}
	
	@Override
	protected int getSize() {
		return trackscheduler.getQueue().size() + 1;
	}
	
	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		if (hovering != null) {
			gui.openLink(hovering.getInfo().uri);
		}
	}
	
	@Override
	protected boolean isSelected(int index) {
		return false;
	}
	
	@Override
	protected void drawBackground() {
	}
	
	@Override
	protected void drawSlot(int index, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		AudioTrack track = null;
		if (index == 0) {
			track = trackscheduler.getCurrentTrack();
			gui.mc.fontRendererObj.drawString(track == null ? I18n.format(MusicPlayerConstants.MODID + ":gui.queue.empty") : (track.getInfo().title.equals("Unknown title") ? track.getInfo().uri : track.getInfo().title), left + 5, slotTop, 0x00FF00);
		} else {
			track = trackscheduler.getQueue().get(index - 1);
			gui.mc.fontRendererObj.drawString(track == null ? I18n.format(MusicPlayerConstants.MODID + ":gui.queue.null") : (track.getInfo().title.equals("Unknown title") ? track.getInfo().uri : track.getInfo().title), left + 5, slotTop, 0xFF0000);
		}
		
		if (isHoveringSlot(slotTop)) {
			hovering = track;
		}
	}
	
	@Override
	protected void drawScreen(int mouseX, int mouseY) {
		if (isHoveringList() && hovering != null) {
			List<String> list = new ArrayList<>();
			
			AudioTrackInfo info = hovering.getInfo();
			
			if (info.author.equals("Unknown artist") && info.title.equals("Unknown title")) {
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.uri") + ": " + info.uri);
			} else {
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.author") + ": " + info.author);
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.title") + ": " + info.title);
			}
			
			String duration = "";
			String position = "";
			
			if (info.isStream) {
				duration = I18n.format(MusicPlayerConstants.MODID + ":overlay.playing.duration.infinite");
				position = I18n.format(MusicPlayerConstants.MODID + ":overlay.playing.position.undefined");
			} else {
				duration = TimeUtil.timeConversion(hovering.getDuration() / 1000);
			}
			
			list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.length") + ": " + duration);
			if (hovering.getPosition() > 0) {
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.position") + ": " + (position.isEmpty() ? TimeUtil.timeConversion(hovering.getPosition() / 1000) : position));
			}
			gui.drawHoverText(list, mouseX, mouseY);
		} else {
			hovering = null;
		}
	}
	
	private boolean isHoveringSlot(int slotTop) {
		return mouseX >= this.left && mouseX <= this.right && mouseY >= slotTop && mouseY <= slotTop + slotHeight;
	}
	
	private boolean isHoveringList() {
		return mouseX >= this.left && mouseX <= this.left + this.listWidth && mouseY >= this.top && mouseY <= this.bottom;
	}
}
