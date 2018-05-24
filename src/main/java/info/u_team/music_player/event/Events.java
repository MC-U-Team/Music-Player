package info.u_team.music_player.event;

import net.minecraftforge.client.event.GuiScreenEvent.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class Events {
	
	private IEvents ievents;
	
	public Events(IEvents ievents) {
		this.ievents = ievents;
	}
	
	@SubscribeEvent
	public void on(RenderGameOverlayEvent event) {
		ievents.on(event);
	}
	
	@SubscribeEvent
	public void on(InitGuiEvent.Post event) {
		ievents.on(event);
	}
	
	@SubscribeEvent
	public void on(ActionPerformedEvent.Pre event) {
		ievents.on(event);
	}
	
	@SubscribeEvent
	public void on(KeyInputEvent event) {
		ievents.on(event);
	}
	
}
