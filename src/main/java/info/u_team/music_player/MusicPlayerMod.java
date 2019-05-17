package info.u_team.music_player;

import info.u_team.music_player.proxy.*;
import info.u_team.u_team_core.api.IModProxy;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MusicPlayerMod.modid)
public class MusicPlayerMod {

	public static final String modid = "musicplayer";

	public static final IModProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

	public MusicPlayerMod() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		proxy.construct();
	}

	@SubscribeEvent
	public void setup(FMLCommonSetupEvent event) {
		proxy.setup();
	}

	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
	public void ready(FMLLoadCompleteEvent event) {
		proxy.complete();
	}

}
