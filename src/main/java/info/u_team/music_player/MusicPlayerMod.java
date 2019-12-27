package info.u_team.music_player;

import info.u_team.music_player.proxy.*;
import info.u_team.u_team_core.api.IModProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MusicPlayerMod.MODID)
public class MusicPlayerMod {
	
	public static final String MODID = "musicplayer";
	
	private static final IModProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public MusicPlayerMod() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		PROXY.construct();
	}
	
	@SubscribeEvent
	public void setup(FMLCommonSetupEvent event) {
		PROXY.setup();
	}
	
	public void ready(FMLLoadCompleteEvent event) {
		PROXY.complete();
	}
	
}
