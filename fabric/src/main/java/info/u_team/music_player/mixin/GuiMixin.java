package info.u_team.music_player.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import info.u_team.music_player.init.MusicPlayerEventHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
abstract class GuiMixin {
	
	@Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lnet/minecraft/client/gui/GuiGraphics;)V", shift = Shift.AFTER))
	private void renderTextInGui(GuiGraphics guiGraphics, float partialTick, CallbackInfo info) {
		MusicPlayerEventHandler.onRenderGameOverlay(guiGraphics, partialTick);
	}
	
}
