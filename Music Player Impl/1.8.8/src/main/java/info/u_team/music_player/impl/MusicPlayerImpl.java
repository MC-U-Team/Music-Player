package info.u_team.music_player.impl;

import info.u_team.music_player.config.Config;
import info.u_team.music_player.connector.IConnector;
import info.u_team.music_player.impl.event.MusicPlayerEventHandler;
import info.u_team.music_player.impl.key.MusicPlayerKeys;
import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class MusicPlayerImpl implements IConnector {
	
	private MusicPlayer musicplayer;
	
	private MusicPlayerEventHandler handler;
	
	@Override
	public void preinit(FMLPreInitializationEvent event) {
		musicplayer = new MusicPlayer();
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		musicplayer.setVolume(Config.getVolume());
		
		ClientRegistry.registerKeyBinding(MusicPlayerKeys.key_openmusicplayer);
		
		handler = new MusicPlayerEventHandler(musicplayer);
	}
	
	@Override
	public void postinit(FMLPostInitializationEvent event) {
		musicplayer.startAudioOutput();
	}
	
	@Override
	public void on(RenderGameOverlayEvent event) {
		handler.on(event);
	}
	
	@Override
	public void on(Post event) {
		handler.on(event);
	}
	
	@Override
	public void on(Pre event) {
		handler.on(event);
	}
	
	@Override
	public void on(KeyInputEvent event) {
		handler.on(event);
	}
	
}
