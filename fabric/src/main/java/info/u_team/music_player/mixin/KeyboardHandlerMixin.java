package info.u_team.music_player.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import info.u_team.music_player.init.MusicPlayerEventHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.gui.screens.Screen;

@Mixin(KeyboardHandler.class)
abstract class KeyboardHandlerMixin {
	
	@Inject(method = "method_1454(ILnet/minecraft/client/gui/screens/Screen;[ZIII)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(III)Z", shift = Shift.AFTER))
	private static void keyPressAfterGuiHandling(int code, Screen screen, boolean[] resultHack, int keyCode, int scanCode, int modifiers, CallbackInfo info) {
		if (!resultHack[0]) {
			resultHack[0] = MusicPlayerEventHandler.onKeyboardPressed(keyCode, scanCode);
		}
	}
	
	@Inject(method = "keyPress(JIIII)V", at = @At(value = "TAIL"))
	private void keyInputAfterAllHandling(long window, int keyCode, int scanCode, int code, int modifiers, CallbackInfo info) {
		MusicPlayerEventHandler.onKeyInput();
	}
}
