package info.u_team.music_player.proxy;

import info.u_team.music_player.dependency.DependencyManager;
import info.u_team.music_player.event.EventHandlerMusicPlayer;
import info.u_team.music_player.init.*;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.util.WrappedObject;
import info.u_team.u_team_core.api.IModProxy;
import info.u_team.u_team_core.registry.util.CommonRegistry;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy implements IModProxy {
	
	@Override
	public void construct() {
		super.construct();
		System.setProperty("http.agent", "Chrome");
		MusicPlayerFiles.construct();
		DependencyManager.construct();
		MusicPlayerManager.construct();
		MusicPlayerKeys.setup();
	}
	
	@Override
	public void setup() {
		super.setup();
		CommonRegistry.registerEventHandler(EventHandlerMusicPlayer.class);
		
		Playlist playlist = new Playlist("GENERATED PLAYLIST");
		playlist.uris.add(new WrappedObject<>("https://www.youtube.com/watch?v=Rj0piP6FJec"));
		playlist.uris.add(new WrappedObject<>("https://www.youtube.com/playlist?list=PLyseegEZ84-drkYTkLldkiIBPHRJd7Xgd"));
		MusicPlayerManager.getPlaylistManager().getPlaylists().add(playlist);
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	}
	
	@Override
	public void complete() {
		super.complete();
		System.out.println("____________________WHY IS THIS NOT CALLED EVERYTIME???___________________________");
		MusicPlayerFonts.setup();
	}
}
