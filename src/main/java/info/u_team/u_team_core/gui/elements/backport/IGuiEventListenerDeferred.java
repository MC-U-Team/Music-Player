package info.u_team.u_team_core.gui.elements.backport;

import javax.annotation.Nullable;

import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public interface IGuiEventListenerDeferred extends IGuiEventListener {
	
	@Nullable
	IGuiEventListener getFocused();
	
	default boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		return this.getFocused() != null && this.getFocused().mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	default boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		return this.getFocused() != null && this.getFocused().mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
	
	default boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		return this.getFocused() != null && this.getFocused().mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
	}
	
	default boolean mouseScrolled(double p_mouseScrolled_1_) {
		return this.getFocused() != null && this.getFocused().mouseScrolled(p_mouseScrolled_1_);
	}
	
	default boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return this.getFocused() != null && this.getFocused().keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	default boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
		return this.getFocused() != null && this.getFocused().keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
	}
	
	default boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		return this.getFocused() != null && this.getFocused().charTyped(p_charTyped_1_, p_charTyped_2_);
	}
}