package info.u_team.u_team_core.gui.elements;

import java.util.function.Consumer;

import net.minecraft.util.ResourceLocation;

public class GuiButtonClickImageToggle extends GuiButtonClickImage {
	
	protected ResourceLocation defaultResource;
	protected ResourceLocation toggleResource;
	
	protected boolean toggled;
	
	public GuiButtonClickImageToggle(int x, int y, int width, int height, ResourceLocation defaultResource, ResourceLocation toggleResource) {
		this(x, y, width, height, defaultResource, toggleResource, -1, -1);
	}
	
	public GuiButtonClickImageToggle(int x, int y, int width, int height, ResourceLocation defaultResource, ResourceLocation toggleResource, int color, int hovercolor) {
		super(x, y, width, height, defaultResource, color, hovercolor);
		this.defaultResource = defaultResource;
		this.toggleResource = toggleResource;
		toggled = false;
	}
	
	public ResourceLocation getDefaultResource() {
		return defaultResource;
	}
	
	public void setDefaultResource(ResourceLocation defaultResource) {
		this.defaultResource = defaultResource;
	}
	
	public ResourceLocation getToggleResource() {
		return toggleResource;
	}
	
	public void setToggleResource(ResourceLocation toggleResource) {
		this.toggleResource = toggleResource;
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public void toggle() {
		toggle(!toggled);
	}
	
	public void toggle(boolean value) {
		toggled = value;
		setResource(toggled ? toggleResource : defaultResource);
	}
	
	public void setToggleClickAction(Consumer<Boolean> consumer) {
		super.setClickAction(() -> {
			toggle();
			consumer.accept(toggled);
		});
	}
	
	@Override
	public void setClickAction(Runnable runnable) {
		throw new UnsupportedOperationException("Use setToggleClickAction instead");
	}
	
}
