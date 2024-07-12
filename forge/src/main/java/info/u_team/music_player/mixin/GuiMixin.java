package info.u_team.music_player.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import info.u_team.music_player.init.MusicPlayerEventHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
abstract class GuiMixin {
	
	@Inject(method = "renderDemoOverlay(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At(value = "HEAD"))
	private void musicplayer$renderTextInGui(GuiGraphics guiGraphics, float partialTick, CallbackInfo info) {
		MusicPlayerEventHandler.onRenderGameOverlay(guiGraphics, partialTick);
	}
	
}
