package info.u_team.music_player.gui.ingame;

import java.io.IOException;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.u_team_core.gui.elements.RenderScrollingText;
import info.u_team.u_team_core.gui.elements.backport.GuiScreen1_13;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiIngameMenuCustom extends GuiScreen1_13 {
	
	@SuppressWarnings("unused")
	private int field_146445_a;
	@SuppressWarnings("unused")
	private int field_146444_f;
	
	private GuiControls controls;
	
	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the window
	 * resizes, the buttonList is cleared beforehand.
	 */
	@SuppressWarnings("unchecked")
	public void initGui() {
		this.field_146445_a = 0;
		this.buttonList.clear();
		byte b0 = -16;
		@SuppressWarnings("unused")
		boolean flag = true;
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b0, I18n.format("menu.returnToMenu", new Object[0])));
		
		if (!this.mc.isIntegratedServerRunning()) {
			((GuiButton) this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
		}
		
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b0, I18n.format("menu.returnToGame", new Object[0])));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b0, 98, 20, I18n.format("menu.options", new Object[0])));
		this.buttonList.add(new GuiButton(12, this.width / 2 + 2, this.height / 4 + 96 + b0, 98, 20, I18n.format("fml.menu.modoptions")));
		GuiButton guibutton;
		this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + b0, 200, 20, I18n.format("menu.shareToLan", new Object[0])));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20, I18n.format("gui.achievements", new Object[0])));
		this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20, I18n.format("gui.stats", new Object[0])));
		guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
		
		controls = new GuiControls(this, 3, width);
		children.add(controls);
	}
	
	@Override
	public void onResize(Minecraft minecraft, int width, int height) {
		final RenderScrollingText titleRender = controls.getTitleRender();
		final RenderScrollingText authorRender = controls.getAuthorRender();
		this.setWorldAndResolution(minecraft, width, height);
		controls.setTitleRender(titleRender);
		controls.setAuthorRender(authorRender);
	}
	
	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		case 1:
			button.enabled = false;
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld((WorldClient) null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		case 2:
		case 3:
		default:
			break;
		case 4:
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
			break;
		case 5:
			if (this.mc.thePlayer != null)
				this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
			break;
		case 6:
			if (this.mc.thePlayer != null)
				this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
			break;
		case 7:
			this.mc.displayGuiScreen(new GuiShareToLan(this));
			break;
		case 12:
			mc.displayGuiScreen(new GuiModList(this));
			break;
		}
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		++this.field_146444_f;
		controls.tick();
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
		controls.drawScreen(mouseX, mouseY, partialTicks);
	}
	
}
