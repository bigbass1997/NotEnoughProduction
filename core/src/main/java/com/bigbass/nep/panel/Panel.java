package com.bigbass.nep.panel;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Panel {

	public PanelGroup panelGroup;
	public Vector2 pos;
	public Vector2 dim;
	
	protected boolean isVisible;
	protected boolean isActive;
	
	public Panel(){
		panelGroup = new PanelGroup();
		pos = new Vector2(0,0);
		dim = new Vector2(0,0);
		isVisible = true;
		isActive = true;
	}
	
	/**
	 * This is where any rendering calls go.
	 */
	public void render() {
	}
	
	public void renderDebug(ShapeRenderer sr){
		if(sr.isDrawing()){
			for(Panel panel : panelGroup.panels){
				if(panel.isVisible()){
					sr.rect(panel.pos.x, panel.pos.y, panel.dim.x, panel.dim.y);
				}
			}
		}
	}
	
	/**
	 * This is where anything that is not related to rendering goes.
	 * @param delta length of time between the previous two frames
	 */
	public void update(float delta) {
	}
	
	/**
	 * Should this screen be rendered?
	 * @return true if yes, false if no
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	public void isVisible(boolean val) {
		this.isVisible = val;
	}
	
	/**
	 * Should this screen be updated?
	 * @return true if yes, false if no
	 */
	public boolean isActive() {
		return isActive;
	}
	
	public void isActive(boolean val) {
		this.isActive = val;
	}
	
	/**
	 * Called when the PanelGroup is resized.
	 * 
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height){
	}
	
	/**
	 * Method is called when program is trying to close/end. Shutdown or close any active threads.
	 */
	public void dispose() {
	}
	
}
