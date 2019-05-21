package info.u_team.u_team_core.gui.elements.backport;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.*;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiScreen1_13 extends GuiScreenInternal {
	
	protected final List<IGuiEventListener> children = Lists.newArrayList();
	protected final List<GuiButton> newButtons = Lists.newArrayList();
	
	protected <T extends GuiButtonNew> T addNewButton(T button) {
		this.newButtons.add(button);
		this.children.add(button);
		return button;
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRenderer = mc.fontRenderer;
		this.width = width;
		this.height = height;
		if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			this.buttonList.clear();
			this.newButtons.clear();
			this.children.clear();
			this.initGui();
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}
	
	@Override
	protected List<? extends IGuiEventListener> getChildren() {
		return children;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (int i = 0; i < this.newButtons.size(); ++i) {
			this.newButtons.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked((double) mouseX, (double) mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased((double) mouseX, (double) mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	private int mouseX = -1, mouseY = -1;
	
	@Override
	protected void mouseClickMove(int xpos, int ypos, int clickedMouseButton, long timeSinceLastClick) {
		if (mouseX == -1 || mouseY == -1) {
			mouseX = xpos;
			mouseY = ypos;
		}
		double d2 = (xpos - this.mouseX);
		double d3 = (ypos - this.mouseY);
		super.mouseDragged(xpos, ypos, clickedMouseButton, d2, d3);
		this.mouseX = xpos;
		this.mouseY = ypos;
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int scroll = Mouse.getEventDWheel();
		if (scroll != 0) {
			super.mouseScrolled(scroll / 120F);
		}
	}
	
	@Override
	public void handleKeyboardInput() throws IOException {
		
		final char c0 = Keyboard.getEventCharacter();
		
		final int key = Keyboard.getEventKey();
		
		final boolean flag = Keyboard.getEventKeyState();
		
		if (key == 0 && c0 >= ' ' || flag) {
			keyTyped(c0, key);
		}
		
		if (!flag) {
			keyReleased(key, -1, -1);
		} else {
			keyPressed(key, -1, -1);
		}
		
		this.mc.dispatchKeypresses();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.charTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
}
