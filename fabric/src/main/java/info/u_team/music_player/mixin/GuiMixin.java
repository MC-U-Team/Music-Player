package info.u_team.music_player.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import info.u_team.music_player.init.MusicPlayerEventHandler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
abstract class GuiMixin {
	
	@Inject(method = "renderDemoOverlay(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At(value = "HEAD"))
	private void musicplayer$renderTextInGui(GuiGraphics guiGraphics, DeltaTracker partialTick, CallbackInfo info) {
		MusicPlayerEventHandler.onRenderGameOverlay(guiGraphics, partialTick);
	}
	
}
