package info.u_team.u_team_core.gui.elements.backport;

import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public interface IGuiEventListener {
	
	default boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		return false;
	}
	
	default boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		return false;
	}
	
	default boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
		return false;
	}
	
	default boolean mouseScrolled(double p_mouseScrolled_1_) {
		return false;
	}
	
	default boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return false;
	}
	
	default boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
		return false;
	}
	
	default boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		return false;
	}
	
	default void focusChanged(boolean focused) {
	}
	
	default boolean canFocus() {
		return false;
	}
}