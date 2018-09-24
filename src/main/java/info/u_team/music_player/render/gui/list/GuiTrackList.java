package info.u_team.music_player.render.gui.list;

import java.util.*;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.render.gui.GuiMusicPlayer;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiTrackList extends GuiScrollingList {
	
	private GuiMusicPlayer gui;
	
	private ITrackScheduler trackscheduler;
	
	private IAudioTrack hovering;
	
	public GuiTrackList(GuiMusicPlayer gui, ITrackScheduler trackscheduler, Minecraft minecraft, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
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
			gui.openLink(hovering.getInfo().getURI());
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
		IAudioTrack track = null;
		if (index == 0) {
			track = trackscheduler.getCurrentTrack();
			gui.mc.fontRenderer.drawString(track == null ? I18n.format(MusicPlayerConstants.MODID + ":gui.queue.empty") : (track.getInfo().getTitle().equals("Unknown title") ? track.getInfo().getURI() : track.getInfo().getTitle()), left + 5, slotTop, 0x00FF00);
		} else {
			track = trackscheduler.getQueue().get(index - 1);
			gui.mc.fontRenderer.drawString(track == null ? I18n.format(MusicPlayerConstants.MODID + ":gui.queue.null") : (track.getInfo().getTitle().equals("Unknown title") ? track.getInfo().getURI() : track.getInfo().getTitle()), left + 5, slotTop, 0xFF0000);
		}
		
		if (isHoveringSlot(slotTop)) {
			hovering = track;
		}
	}
	
	@Override
	protected void drawScreen(int mouseX, int mouseY) {
		if (isHoveringList() && hovering != null) {
			List<String> list = new ArrayList<>();
			
			IAudioTrackInfo info = hovering.getInfo();
			
			if (info.getAuthor().equals("Unknown artist") && info.getTitle().equals("Unknown title")) {
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.uri") + ": " + info.getURI());
			} else {
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.author") + ": " + info.getAuthor());
				list.add(I18n.format(MusicPlayerConstants.MODID + ":gui.queue.title") + ": " + info.getTitle());
			}
			
			String duration = "";
			String position = "";
			
			if (info.isStream()) {
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
