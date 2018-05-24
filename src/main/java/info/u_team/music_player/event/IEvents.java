package info.u_team.music_player.event;

import net.minecraftforge.client.event.GuiScreenEvent.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public interface IEvents {
	
	public void on(RenderGameOverlayEvent event);
	
	public void on(InitGuiEvent.Post event);
	
	public void on(ActionPerformedEvent.Pre event);
	
	public void on(KeyInputEvent event);
	
}
