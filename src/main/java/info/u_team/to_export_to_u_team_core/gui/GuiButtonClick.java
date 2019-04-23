package info.u_team.to_export_to_u_team_core.gui;

import java.util.function.Consumer;

import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiButtonClick extends GuiButtonExt {
	
	private final Consumer<GuiButtonClick> consumer;
	
	public GuiButtonClick(int xPos, int yPos, String displayString, Consumer<GuiButtonClick> consumer) {
		super(-1, xPos, yPos, displayString);
		this.consumer = consumer;
	}
	
	public GuiButtonClick(int xPos, int yPos, int width, int height, String displayString, Consumer<GuiButtonClick> consumer) {
		super(-1, xPos, yPos, width, height, displayString);
		this.consumer = consumer;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		if (consumer != null) {
			consumer.accept(this);
		}
	}
}
