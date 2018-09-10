package info.u_team.music_player.impl.render.gui;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import info.u_team.music_player.MusicPlayerConstants;
import info.u_team.music_player.impl.render.gui.button.GuiButtonImage;
import info.u_team.music_player.impl.render.gui.button.special.*;
import info.u_team.music_player.impl.render.gui.list.GuiTrackList;
import net.hycrafthd.basiclavamusicplayer.MusicPlayer;
import net.hycrafthd.basiclavamusicplayer.event.*;
import net.hycrafthd.basiclavamusicplayer.event.events.EventSearch.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiMusicPlayer extends GuiScreen {
	
	private MusicPlayer musicplayer;
	
	private GuiTextField searchfield;
	private GuiButtonExt searchbutton;
	
	private long searchstatustime;
	private String searchstatus;
	
	private GuiTrackList guilist;
	
	public GuiMusicPlayer(MusicPlayer musicplayer) {
		this.musicplayer = musicplayer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		MusicPlayerEventBus.register(this);
		
		searchfield = new GuiTextField(0, mc.fontRendererObj, 11, 11, width / 2 + 20, 20);
		searchfield.setFocused(true);
		searchfield.setMaxStringLength(1000);
		
		searchbutton = new GuiButtonExt(1, width / 2 + 40, 10, width / 2 - 50, 22, I18n.format(MusicPlayerConstants.MODID + ":button.search"));
		buttonList.add(searchbutton);
		
		GuiButtonImage playpause = new GuiButtonImagePlayPause(musicplayer, 2, width - 32, 36, 22, 22);
		GuiButtonImage clear = new GuiButtonImageClear(musicplayer, 3, width - 56, 36, 22, 22);
		GuiButtonImage skip = new GuiButtonImageSkip(musicplayer, 4, width - 80, 36, 22, 22);
		GuiButtonImage repeat = new GuiButtonImageRepeat(musicplayer, 5, width - 104, 36, 22, 22);
		GuiButtonImage shuffle = new GuiButtonImageShuffle(musicplayer, 6, width - 128, 36, 22, 22);
		GuiSlider volume = new GuiButtonSliderVolume(musicplayer, 7, width - 200, 36, 70, 22);
		
		buttonList.add(playpause);
		buttonList.add(clear);
		buttonList.add(skip);
		buttonList.add(repeat);
		buttonList.add(shuffle);
		buttonList.add(volume);
		
		guilist = new GuiTrackList(this, musicplayer.getTrackScheduler(), mc, width - 20, height - 20, 62, height - 10, 10, 10, mc.displayWidth, mc.displayHeight);
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		MusicPlayerEventBus.unregister(this);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		drawBackground(0);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(10 * scaled.getScaleFactor(), 10 * scaled.getScaleFactor(), (scaled.getScaledWidth() - 20) * scaled.getScaleFactor(), (scaled.getScaledHeight() - 73) * scaled.getScaleFactor());
		guilist.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		searchfield.drawTextBox();
		
		if (searchstatustime + 4000 >= System.currentTimeMillis()) {
			drawString(fontRendererObj, searchstatus, 11, 42, 0xFFF);
		}
	}
	
	@Override
	public void updateScreen() {
		searchfield.updateCursorCounter();
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		searchfield.textboxKeyTyped(typedChar, keyCode);
		if (searchfield.isFocused() && keyCode == Keyboard.KEY_RETURN) {
			searchfield.setFocused(false);
			searchTrack();
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		searchfield.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		guilist.actionPerformed(button);
		if (button.id == 1) {
			searchTrack();
		} else if (button.id == 2) {
			musicplayer.getTrackScheduler().setPaused(!musicplayer.getTrackScheduler().isPaused());
		} else if (button.id == 3) {
			musicplayer.getTrackScheduler().stop();
		} else if (button.id == 4) {
			musicplayer.getTrackScheduler().skip();
		} else if (button.id == 5) {
			musicplayer.getTrackScheduler().setRepeat(!musicplayer.getTrackScheduler().isRepeat());
		} else if (button.id == 6) {
			musicplayer.getTrackScheduler().setShuffle(!musicplayer.getTrackScheduler().isShuffle());
		}
	}
	
	@MusicEventSubscriber
	public void on(EventSearchSuccess event) {
		searchTrackStatus("\u00A7aAdded successfully!");
	}
	
	@MusicEventSubscriber
	public void on(EventSearchFailed event) {
		searchTrackStatus("\u00A7cError occured: " + event.getError());
	}
	
	private void searchTrack() {
		if (searchfield.getText().isEmpty()) {
			searchTrackStatus("\u00A7cEmpty search not allowed!");
			return;
		}
		musicplayer.getTrackSearch().queue(searchfield.getText());
		searchfield.setText("");
	}
	
	private void searchTrackStatus(String status) {
		searchstatustime = System.currentTimeMillis();
		searchstatus = status;
	}
	
	public void drawHoverText(List<String> textLines, int x, int y) {
		RenderHelper.enableStandardItemLighting();
		drawHoveringText(textLines, x - 8, y + 8);
		RenderHelper.disableStandardItemLighting();
	}
	
	private URI uri;
	
	public void openLink(String string) {
		try {
			uri = new URI(string);
			if (this.mc.gameSettings.chatLinksPrompt) {
				this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, string, 1, true));
			} else {
				openLink(uri);
			}
		} catch (Exception ex) {
		}
	}
	
	@Override
	public void confirmClicked(boolean result, int id) {
		super.confirmClicked(result, id);
		if (id == 1) {
			if (result) {
				this.openLink(uri);
			}
			uri = null;
			this.mc.displayGuiScreen(this);
		}
	}
	
	private void openLink(URI uri) {
		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { uri });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
