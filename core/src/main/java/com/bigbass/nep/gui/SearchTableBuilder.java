package com.bigbass.nep.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bigbass.nep.gui.actors.ContainerLabel;
import com.bigbass.nep.gui.actors.CustomContainer;
import com.bigbass.nep.gui.actors.CustomScrollPane;
import com.bigbass.nep.recipes.RecipeManager;
import com.bigbass.nep.skins.SkinManager;

public class SearchTableBuilder {

	private final String FONTPATH = "fonts/droid-sans-mono.ttf";
	
	private final Color COLOR_TEXT_SEARCH = new Color(0xCC4444FF);
	private final Color COLOR_SOURCE_SEARCH = new Color(0xA4B2D7FF);
	private final Color COLOR_MACHINE_SEARCH = new Color(0xB4C2E7FF);
	
	private Stage stage;
	private Table root;
	private float tableWidth;
	
	public ContainerTextField searchName;
	public Table checkboxes;
	public ContainerTextField searchProcessType;
	public List<String> categories;
	public CustomScrollPane scrollPane;
	
	public boolean dirtyFilters = false;
	
	public SearchTableBuilder(Stage stage, Table root, float tableWidth){
		this.stage = stage;
		this.root = root;
		this.tableWidth = tableWidth;
	}
	
	public void build(){
		final boolean wasVisible = root.isVisible();
		
		root.reset();
		//root.debugAll();
		
		root.setVisible(wasVisible);
		
		root.setWidth(tableWidth);
		
		// left column
		Table leftColumn = new Table(root.getSkin());
		leftColumn.setWidth(tableWidth * 0.5f);
		
		textSearchRow(leftColumn);
		sourceSelectRow(leftColumn);
		machineSearchRow(leftColumn);
		
		root.add(leftColumn).align(Align.top);
		
		// right column
		Table rightColumn = new Table(root.getSkin());
		rightColumn.setWidth(tableWidth * 0.5f);
		
		machineListRow(rightColumn);
		
		root.add(rightColumn).align(Align.top);
		
		root.setPosition((Gdx.graphics.getWidth() * 0.5f) - (root.getWidth() * 0.5f), Gdx.graphics.getHeight() - root.getHeight() - 50);
		
		dirtyFilters = true;
	}
	
	public void reposition(){
		root.setPosition((Gdx.graphics.getWidth() * 0.5f) - (root.getWidth() * 0.5f), Gdx.graphics.getHeight() - root.getHeight() - 50);
	}
	
	// left column //
	
	private void textSearchRow(Table root){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);
		
		ContainerLabel searchText = new ContainerLabel(SkinManager.getSkin(FONTPATH, 12));
		searchText.label.setText("Element Search:");
		searchText.setBackgroundColor(COLOR_TEXT_SEARCH);
		searchText.setForegroundColor(COLOR_TEXT_SEARCH);
		searchText.minWidth(root.getWidth() * 0.4f);
		
		searchName = new ContainerTextField(SkinManager.getSkin(FONTPATH, 12));
		searchName.minWidth(root.getWidth() * 0.6f);
		searchName.height(searchText.getPrefHeight());
		searchName.addListener(new InputListener(){
			
			@Override
			public boolean keyTyped(InputEvent event, char character){
				dirtyFilters = true;
				
				return false;
			}
		});
		
		nested.add(searchText);
		nested.add(searchName);
		
		root.add(nested);
	}
	
	private void sourceSelectRow(Table root){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);
		
		ContainerLabel sourceText = new ContainerLabel(SkinManager.getSkin(FONTPATH, 12));
		sourceText.label.setText("Source:");
		sourceText.setBackgroundColor(COLOR_SOURCE_SEARCH);
		sourceText.setForegroundColor(COLOR_SOURCE_SEARCH);
		sourceText.minWidth(root.getWidth() * 0.4f);
		
		RecipeManager rm = RecipeManager.getInst();
		checkboxes = new Table(rootSkin);
		for(String source : rm.recipeSources.keySet()){
			checkboxes.row();
			ContainerCheckBox box = new ContainerCheckBox(SkinManager.getSkin(FONTPATH, 12));
			box.box.setText(source);
			box.minWidth(root.getWidth() * 0.6f);
			box.box.setChecked(true);
			box.box.align(Align.left);
			
			box.setBackgroundColor(new Color(0x3F3F3FFF));
			CheckBoxStyle st = new CheckBoxStyle(box.box.getStyle());
			st.fontColor = ColorCache.getForegroundColor(new Color(0x3F3F3FFF));
			box.box.setStyle(st);
			
			box.box.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y){
					dirtyFilters = true;
				}
			});
			
			checkboxes.add(box);
		}
		
		
		nested.add(sourceText).fillY();
		nested.add(checkboxes);
		
		root.add(nested);
	}
	
	private void machineSearchRow(Table root){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);
		
		ContainerLabel searchText = new ContainerLabel(SkinManager.getSkin(FONTPATH, 12));
		searchText.label.setText("Process Type:");
		searchText.setBackgroundColor(COLOR_MACHINE_SEARCH);
		searchText.setForegroundColor(COLOR_MACHINE_SEARCH);
		searchText.minWidth(root.getWidth() * 0.4f);
		
		searchProcessType = new ContainerTextField(SkinManager.getSkin(FONTPATH, 12));
		searchProcessType.minWidth(root.getWidth() * 0.6f);
		searchProcessType.height(searchText.getPrefHeight() - 1);
		searchProcessType.addListener(new InputListener(){
			
			@Override
			public boolean keyTyped(InputEvent event, char character){
				dirtyFilters = true;
				
				return false;
			}
		});
		
		
		nested.add(searchText);
		nested.add(searchProcessType);
		
		root.add(nested);
	}
	
	// right column //
	
	private void machineListRow(Table root){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);
		
		ContainerLabel catText = new ContainerLabel(SkinManager.getSkin(FONTPATH, 14));
		catText.label.setText("Process Types");
		catText.label.setAlignment(Align.center);
		catText.setBackgroundColor(Color.GOLD);
		catText.setForegroundColor(Color.GOLD);
		catText.minWidth(root.getWidth());
		
		
		categories = new List<String>(SkinManager.getSkin(FONTPATH, 12));
		
		scrollPane = new CustomScrollPane(categories, SkinManager.getSkin(FONTPATH, 12)){
			
			@Override
			public float getMouseWheelY () {
				return categories.getItemHeight();
			}
			
		};
		scrollPane.setSmoothScrolling(false);
		scrollPane.addListener(new InputListener(){
			
			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount){
				System.out.println("fired! " + amount);
				if(categories.getItems() != null && categories.getItems().size > 0){
					categories.setSelectedIndex(MathUtils.clamp(categories.getSelectedIndex() + amount, 0, categories.getItems().size - 1));
				}
				
				return false;
			}
			
		});
		scrollPane.setFadeScrollBars(false);
		stage.setScrollFocus(scrollPane);
		
		
		nested.align(Align.top);
		nested.add(catText);
		nested.row();
		nested.add(scrollPane).width(root.getWidth()).height(categories.getItemHeight() * 5);
		
		root.add(nested);
	}
	
	public class ContainerTextField extends CustomContainer<TextField> {
		
		public TextField field;
		
		public ContainerTextField(Skin skin) {
			super(skin);
			
			field = new TextField("", skin);
			setActor(field);
			
			//pad(1, 3, 1, 3);
		}

		@Override
		public Container<TextField> minWidth(float minWidth){
			return super.minWidth(minWidth - (this.getPadLeft() + this.getPadRight()));
		}
	}
	
	public class ContainerCheckBox extends CustomContainer<CheckBox> {
		
		public CheckBox box;
		
		public ContainerCheckBox(Skin skin) {
			super(skin);
			
			box = new CheckBox("hello world", skin);
			setActor(box);
			
			//pad(1, 3, 1, 3);
			padLeft(3);
		}

		@Override
		public Container<CheckBox> minWidth(float minWidth){
			return super.minWidth(minWidth - (this.getPadLeft() + this.getPadRight()));
		}
	}
}
