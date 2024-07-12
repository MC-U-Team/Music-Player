package info.u_team.music_player.data;

import info.u_team.music_player.MusicPlayerMod;
import info.u_team.music_player.data.provider.MusicPlayerLanguagesProvider;
import info.u_team.u_team_core.data.GenerationData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = MusicPlayerMod.MODID, bus = Bus.MOD)
public class MusicPlayerDataGenerator {
	
	@SubscribeEvent
	public static void data(GatherDataEvent event) {
		final GenerationData data = new GenerationData(MusicPlayerMod.MODID, event);
		data.addProvider(event.includeClient(), MusicPlayerLanguagesProvider::new);
	}
	
}
