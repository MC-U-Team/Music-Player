package info.u_team.music_player.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import info.u_team.music_player.lavaplayer.api.*;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

public class GuiMusicPlayer extends GuiScreen implements IMusicPlayerEvents {
	
	private GuiTextField searchfield;
	
	@Override
	protected void initGui() {
		mc.keyboardListener.enableRepeatEvents(true);
		MusicPlayerManager.player.registerEventHandler(this);
		
		searchfield = new GuiTextField(0, mc.fontRenderer, 11, 11, width / 2 + 20, 20);
		searchfield.setMaxStringLength(1000);
		children.add(searchfield);
		
		super.initGui();
		searchfield.setFocused(true);
		searchfield.setCanLoseFocus(false);
	}
	
	@Override
	public void onGuiClosed() {
		mc.keyboardListener.enableRepeatEvents(false);
		MusicPlayerManager.player.unregisterEventHandler(this);
	}
	
	public void onResize(Minecraft minecraft, int width, int height) {
		String text = searchfield.getText();
		setWorldAndResolution(minecraft, width, height);
		searchfield.setText(text);
	}
	
	@Override
	public void tick() {
		searchfield.tick();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		searchfield.drawTextField(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean keyPressed(int keycode, int p_keyPressed_2_, int p_keyPressed_3_) {
		System.out.println(searchfield.isFocused() + " -> " + searchfield.getVisible() + " -> ");
		if (searchfield.isFocused() && keycode == GLFW.GLFW_KEY_ENTER) {
			// searchfield.setFocused(false);
			// MusicPlayerManager.player.getTrackSearch().play(searchfield.getText());
			// searchfield.setText("");
			// return false;
			
			String path = TinyFileDialogs.tinyfd_saveFileDialog("Save Project", null, null, null);
			System.out.println(path);
		}
		return super.keyPressed(keycode, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		return super.charTyped(typedChar, keyCode);
	}
	
	@Override
	public void onPlay(IAudioTrack track) {
		System.out.println("PLAY");
		System.out.println(track.getInfo().getIdentifier());
	}
	
	@Override
	public void onStop() {
		System.out.println("STOP");
	}
	
	@Override
	public void onSearchFailed(String error, Exception exeption) {
		System.out.println("SEARCH FAILED: " + error);
		exeption.printStackTrace();
	}
	
	@Override
	public void onSearchSuccess(State state) {
		System.out.println("SEARCH SUCCESS");
		System.out.println(state);
	}
}
