package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.gui.elements.backport.GuiButtonExtNew;

public class GuiButtonClick extends GuiButtonExtNew {
	
	private Runnable runnable;
	
	public GuiButtonClick(int xPos, int yPos, String displayString) {
		super(-1, xPos, yPos, displayString);
	}
	
	public GuiButtonClick(int xPos, int yPos, int width, int height, String displayString) {
		super(-1, xPos, yPos, width, height, displayString);
	}
	
	public void setClickAction(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		if (runnable != null) {
			runnable.run();
		}
	}
}
