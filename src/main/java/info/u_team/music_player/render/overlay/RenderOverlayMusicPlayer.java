package info.u_team.music_player.render.overlay;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.render.text.*;
import info.u_team.music_player.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;

public class RenderOverlayMusicPlayer {
	
	private IMusicPlayer musicplayer;
	
	private Minecraft minecraft;
	private ScrollingTextRender title, author;
	private ScaledTextRender position, duration;
	
	private float scale;
	private float progress;
	
	private boolean visible;
	
	public RenderOverlayMusicPlayer(IMusicPlayer musicplayer) {
		this.musicplayer = musicplayer;
		
		this.minecraft = Minecraft.getMinecraft();
		
		FontRenderer fontrender = minecraft.fontRenderer;
		this.title = new ScrollingTextRender(fontrender);
		this.author = new ScrollingTextRender(fontrender);
		this.position = new ScaledTextRender(fontrender);
		this.duration = new ScaledTextRender(fontrender);
		
		this.title.setColor(0xFFFF00);
		this.author.setColor(0xFFFF00);
		this.position.setColor(0xFFFF00);
		this.duration.setColor(0xFFFF00);
		
		this.title.setSpeedTime(20);
		this.author.setSpeedTime(20);
		
		this.setScale(1F);
		this.progress = 0F;
		
		this.visible = false;
		
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		this.title.setScale(1F * scale);
		this.title.setWidth(114);
		this.author.setScale(0.75F * scale);
		this.author.setWidth((int) (114 / 0.75F));
		this.position.setScale(0.5F * scale);
		this.duration.setScale(0.5F * scale);
	}
	
	private void setProgress(IAudioTrack track) {
		if (track == null || track.getInfo() == null) {
			return;
		}
		IAudioTrackInfo info = track.getInfo();
		if (info.isStream()) {
			position.setText(I18n.format(MusicPlayerConstants.MODID + ":overlay.playing.position.undefined"));
			duration.setText(I18n.format(MusicPlayerConstants.MODID + ":overlay.playing.duration.infinite"));
			progress = 0.5F;
		} else {
			position.setText(TimeUtil.timeConversion(track.getPosition() / 1000));
			duration.setText(TimeUtil.timeConversion(track.getDuration() / 1000));
			progress = Math.min((float) track.getPosition() / (float) track.getDuration(), 1F);
		}
	}
	
	private void setSong(IAudioTrack track) {
		if (track == null || track.getInfo() == null) {
			return;
		}
		IAudioTrackInfo info = track.getInfo();
		if (info.getAuthor().equals("Unknown artist") && info.getTitle().equals("Unknown title")) {
			title.setText(info.getURI());
			author.setText("");
		} else {
			title.setText(info.getTitle());
			author.setText(info.getAuthor());
		}
		setProgress(track);
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void draw() {
		// Visible
		if (!visible) {
			return;
		}
		
		// Update music
		IAudioTrack track = musicplayer.getTrackScheduler().getCurrentTrack();
		if (track == null) {
			return;
		} else {
			setSong(track);
		}
		
		// Scaled
		ScaledResolution scaledresolution = new ScaledResolution(minecraft);
		
		// Background
		Gui.drawRect(scaledresolution.getScaledWidth() - (int) (123 * scale), scaledresolution.getScaledHeight() - (int) (38 * scale), scaledresolution.getScaledWidth() - 3, scaledresolution.getScaledHeight() - 3, 0xFF212121);
		
		// Progressbar
		Gui.drawRect(scaledresolution.getScaledWidth() - (int) (117 * scale), scaledresolution.getScaledHeight() - (int) (15 * scale), scaledresolution.getScaledWidth() - (int) (10 * scale), scaledresolution.getScaledHeight() - (int) (12 * scale), 0xFF555555);
		Gui.drawRect(scaledresolution.getScaledWidth() - (int) (117 * scale), scaledresolution.getScaledHeight() - (int) (15 * scale), scaledresolution.getScaledWidth() - (int) (117 * scale) + (int) (107 * scale * progress), scaledresolution.getScaledHeight() - (int) (12 * scale), 0xFF3e9100);
		
		// Text
		title.draw(scaledresolution.getScaledWidth() - (int) (120 * scale), scaledresolution.getScaledHeight() - (int) (35 * scale));
		author.draw(scaledresolution.getScaledWidth() - (int) (120 * scale), scaledresolution.getScaledHeight() - (int) (25 * scale));
		position.draw(scaledresolution.getScaledWidth() - (int) (117 * scale), scaledresolution.getScaledHeight() - (int) (10 * scale));
		duration.draw(scaledresolution.getScaledWidth() - (int) (10 * scale) - duration.getTextWidth(), scaledresolution.getScaledHeight() - (int) (10 * scale));
	}
	
}
