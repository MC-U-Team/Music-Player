package info.u_team.music_player.data;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.data.provider.MusicPlayerLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = MusicPlayerMod.MODID, bus = Bus.MOD)
public class MusicPlayerDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(MusicPlayerMod.MODID, event);
		data.addProvider(event.includeClient(), MusicPlayerLanguagesProvider::new);
	}
	
}
