package info.u_team.music_player.gui.settings;

import info.u_team.music_player.gui.BetterScrollableList;
import info.u_team.music_player.lavaplayer.api.IMusicPlayer;
import info.u_team.music_player.lavaplayer.api.util.AudioUtil;
import info.u_team.music_player.musicplayer.MusicPlayerManager;

public class GuiMusicPlayerSettingsMixerDeviceList extends BetterScrollableList<GuiMusicPlayerSettingsMixerDeviceListEntry> {
	
	private final IMusicPlayer player;
	
	public GuiMusicPlayerSettingsMixerDeviceList(int width, int height, int top, int bottom, int left, int right) {
		super(width, height, top, bottom, left, right, 20, 20);
		player = MusicPlayerManager.getPlayer();
		AudioUtil.findAudioDevices(player.getSpeakerInfo()).stream().map(GuiMusicPlayerSettingsMixerDeviceListEntry::new).peek(entry -> {
			if (entry.getMixerName().equals(player.getMixer())) {
				super.setSelected(entry);
			}
		}).forEach(this::addEntry);
	}
	
	@Override
	public void setSelected(GuiMusicPlayerSettingsMixerDeviceListEntry entry) {
		super.setSelected(entry);
		player.setMixer(entry.getMixerName());
	}
}
