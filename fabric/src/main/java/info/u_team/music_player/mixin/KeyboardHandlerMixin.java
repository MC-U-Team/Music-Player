package info.u_team.music_player.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import info.u_team.music_player.init.MusicPlayerEventHandler;
import net.minecraft.client.KeyboardHandler;

@Mixin(KeyboardHandler.class)
abstract class KeyboardHandlerMixin {
	
	@Inject(method = "keyPress(JIIII)V", at = @At(value = "TAIL"))
	private void keyInputAfterAllHandling(long window, int keyCode, int scanCode, int code, int modifiers, CallbackInfo info) {
		MusicPlayerEventHandler.onKeyInput();
	}
}
