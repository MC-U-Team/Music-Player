package info.u_team.music_player.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import info.u_team.music_player.init.MusicPlayerEventHandler;
import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
abstract class GuiMixin {
	
	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V", shift = Shift.AFTER))
	private void renderTextInGui(PoseStack matrixStack, float partialTick, CallbackInfo info) {
		MusicPlayerEventHandler.onRenderGameOverlay(matrixStack, partialTick);
	}
	
}
