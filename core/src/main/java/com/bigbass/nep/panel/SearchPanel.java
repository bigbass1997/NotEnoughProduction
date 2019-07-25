package com.bigbass.nep.panel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bigbass.nep.gui.SearchTableBuilder;

public class SearchPanel extends Panel {
	
	private final Color PANEL_BACKGROUND_COLOR;
	
	private Table searchTable;
	
	private Stage stage;
	private ShapeRenderer sr;
	
	public SearchPanel(float x, float y, Stage stage, ShapeRenderer sr){
		super();
		
		PANEL_BACKGROUND_COLOR = new Color(0xAAAAAA9A);
		
		pos.x = x;
		pos.y = y;
		
		this.stage = stage;
		this.sr = sr;
		
		searchTable = new Table();
		SearchTableBuilder.build(searchTable, 300);
		searchTable.setPosition(pos.x + 10, pos.y + 10);
		
		this.stage.addActor(searchTable);
		

		isVisible(false);
		isActive(false);
	}
	
	@Override
	public void render(){
		sr.begin(ShapeType.Filled);
		sr.setColor(PANEL_BACKGROUND_COLOR);
		sr.rect(pos.x, pos.y, dim.x, dim.y);
		sr.end();
	}
	
	@Override
	public void update(float delta){
		
	}
	
	@Override
	public void isVisible(boolean val){
		super.isVisible(val);
		
		if(searchTable != null){
			searchTable.setVisible(val);
		}
	}
}
