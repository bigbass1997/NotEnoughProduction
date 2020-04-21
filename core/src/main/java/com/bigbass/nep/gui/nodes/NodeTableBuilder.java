package com.bigbass.nep.gui.nodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.bigbass.nep.gui.PathManager;
import com.bigbass.nep.gui.actors.ContainerLabel;
import com.bigbass.nep.gui.borders.BorderedTable;
import com.bigbass.nep.gui.listeners.HoverListener;
import com.bigbass.nep.recipes.elements.usual.Fluid;
import com.bigbass.nep.recipes.elements.Pile;
import com.bigbass.nep.recipes.processing.Recipe;
import com.bigbass.nep.skins.SkinManager;
import com.bigbass.nep.util.Singleton;

public class NodeTableBuilder {
	
	// The following textures are not the best way to store assets in code, but good enough for now.
	
	/** Texture for the node's movement image. */
	private static TextureRegion MOVE_TEXTURE;
	/** Texture for the node's removal image. */
	private static TextureRegion REMOVE_TEXTURE;
	
	private static final String FONTPATH = "fonts/droid-sans-mono.ttf";

	private static final Color COLOR_OVERCLOCK = new Color(0xDD1111FF);
	private static final Color COLOR_OUTPUT_HEADER = new Color(0x595959FF);
	private static final Color COLOR_INPUT_HEADER = new Color(0x595959FF);
	private static final Color COLOR_OUTPUTS_BAR = new Color(0xA6A6A6FF);
	private static final Color COLOR_INPUTS_BAR = new Color(0xC0C0C0FF);
	private static final Color COLOR_COST = new Color(0xFFC000FF);
	
	private NodeTableBuilder(){}
	
	public static void build(Node node, BorderedTable root){
		build(node, root, true);
	}
	
	public static void build(Node node, BorderedTable root, boolean includeControlsRow){
		if(MOVE_TEXTURE == null){
			MOVE_TEXTURE = new TextureRegion(new Texture(Gdx.files.internal("textures/moveNode.png")));
		}
		
		if(REMOVE_TEXTURE == null){
			REMOVE_TEXTURE = new TextureRegion(new Texture(Gdx.files.internal("textures/removeNode.png")));
		}
		
		//final ShapeDrawer sd = new BorderDrawableFactory(stageBatch);
		
		root.reset();
		//root.debug();
		
		root.setWidth(node.width);
		
		// for organization and maintainability, each type of row is in its own private function
		if(includeControlsRow){
			nodeMenuRow(root, node);
		}
		titleRow(root, node);
		inputHeaderRow(root, node);
		inputRows(root, node);
		outputHeaderRow(root, node);
		outputRows(root, node);
	}
	
	private static void nodeMenuRow(Table root, Node node){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);
		
		ContainerLabel spacer = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
		spacer.setBackgroundColor(Color.CLEAR);
		
		MoveNodeImage moveNode = new MoveNodeImage(MOVE_TEXTURE, SkinManager.getSkin(FONTPATH, 10), root, node);
		moveNode.setScaling(Scaling.fill);
		
		RemoveNodeImage removeNode = new RemoveNodeImage(REMOVE_TEXTURE, SkinManager.getSkin(FONTPATH, 10), node);
		removeNode.setScaling(Scaling.fill);
		
		
		spacer.minWidth( root.getWidth() - (MOVE_TEXTURE.getRegionWidth() + REMOVE_TEXTURE.getRegionWidth()) );
		
		
		nested.add(spacer);
		nested.add(moveNode).width(MOVE_TEXTURE.getRegionWidth()).height(MOVE_TEXTURE.getRegionHeight());
		nested.add(removeNode).width(REMOVE_TEXTURE.getRegionWidth()).height(REMOVE_TEXTURE.getRegionHeight());
		
		root.add(nested);
	}
	
	private static void titleRow(BorderedTable root, Node node){
		root.row();
		final Skin rootSkin = root.getSkin();
		BorderedTable nested = new BorderedTable(rootSkin);
		
		final Color col = new Color(0xB4C2E7FF);
		
		Recipe rec = node.getRecipe();
		
		// title
		ContainerLabel title = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
		title.label.setText("Crafting Table");
		title.setBackgroundColor(col); // temporary colors
		title.setForegroundColor(col);
		title.minWidth(root.getWidth() * 0.7f);
		
		// rate
		ContainerLabel rate = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
		rate.setBackgroundColor(col); // temporary colors
		rate.setForegroundColor(col);
		rate.minWidth(root.getWidth() * 0.3f);
		rate.label.setAlignment(Align.right);
		
		
		nested.add(title).fillY();
		nested.add(rate);
		
		root.add(nested/*, BorderSide.mask(BorderSide.ALL, 1)*/);
	}
	
	private static void inputHeaderRow(Table root, Node node){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);

		Recipe rec = node.getRecipe();
		
		// output
		ContainerLabel input = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
		input.label.setText("Input:");
		input.setBackgroundColor(COLOR_INPUT_HEADER);
		input.setForegroundColor(COLOR_INPUT_HEADER);
		input.minWidth(root.getWidth() * 0.6f);
		
		// cost
		ContainerLabel cost = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));

		cost.setBackgroundColor(COLOR_OUTPUT_HEADER);
		cost.setForegroundColor(COLOR_OUTPUT_HEADER);

		cost.minWidth(root.getWidth() * 0.4f);
		cost.label.setAlignment(Align.center);
		
		
		nested.add(input);
		nested.add(cost);
		
		root.add(nested);
	}
	
	private static void inputRows(Table root, Node node){
		final Skin rootSkin = root.getSkin();

		Recipe rec = node.getRecipe();
		
		if(rec != null){
			for(Pile pile : rec.inputs){
				root.row();
				HoverableTable nested = new HoverableTable(rootSkin);
				
				// name
				ContainerLabel name = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
				name.label.setText(pile.element.HRName());
				name.setBackgroundColor(COLOR_INPUTS_BAR);
				name.setForegroundColor(COLOR_INPUTS_BAR);
				name.minWidth(root.getWidth() * 0.8f);
				
				// qty
				ContainerLabel qty = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
				String qtyText = String.valueOf(pile.amount);
				if(pile.element instanceof Fluid){
					qtyText += "L";
				}
				qty.label.setText(qtyText);
				qty.setBackgroundColor(COLOR_INPUTS_BAR);
				qty.setForegroundColor(COLOR_INPUTS_BAR);
				qty.minWidth(root.getWidth() * 0.2f);
				qty.label.setAlignment(Align.center);
				
				nested.add(name);
				nested.add(qty).fillY();
				
				nested.addListener(new ClickListener(Buttons.LEFT) {
					@Override
					public void clicked(InputEvent event, float x, float y){
						Singleton.getInstance(PathManager.class).createPath(
								node.uuid,
								pile.element,
								true,
								event,
								x,
								y);
					}
				});
//				nested.addListener(new ClickListener(Buttons.LEFT){\
//					@Override
//					public void clicked(InputEvent event, float x, float y){
//
//					}
//				});
//

				node.addInputTable(pile.element.name(), nested);

				root.add(nested);
			}
		}
	}

	private static void outputHeaderRow(Table root, Node node){
		root.row();
		final Skin rootSkin = root.getSkin();
		Table nested = new Table(rootSkin);

		Recipe rec = node.getRecipe();
		
		// output
		ContainerLabel output = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
		output.label.setText("Output:");
		output.setBackgroundColor(COLOR_OUTPUT_HEADER);
		output.setForegroundColor(COLOR_OUTPUT_HEADER);
		output.minWidth(root.getWidth() * 0.6f);
		

		// ticks
		ContainerLabel ticks = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
		ticks.setBackgroundColor(COLOR_INPUT_HEADER);
		ticks.setForegroundColor(COLOR_INPUT_HEADER);
		ticks.minWidth(root.getWidth() * 0.4f);
		ticks.label.setAlignment(Align.right);
		
		
		nested.add(output).fillY();
		nested.add(ticks);
		
		root.add(nested);
	}
	
	private static void outputRows(Table root, Node node){
		final Skin rootSkin = root.getSkin();

		Recipe rec = node.getRecipe();
		
		if(rec != null){
			for(Pile pile : rec.outputs){
				root.row();
				HoverableTable nested = new HoverableTable(rootSkin);
				
				// name
				ContainerLabel name = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
				name.label.setText(pile.element.HRName());
				name.setBackgroundColor(COLOR_OUTPUTS_BAR);
				name.setForegroundColor(COLOR_OUTPUTS_BAR);
				name.minWidth(root.getWidth() * 0.8f);
				
				// qty
				ContainerLabel qty = new ContainerLabel(SkinManager.getSkin(FONTPATH, 10));
				String qtyText = String.valueOf(pile.amount);
				if(pile.element instanceof Fluid){
					qtyText += "L";
				}
				qty.label.setText(qtyText);
				qty.setBackgroundColor(COLOR_OUTPUTS_BAR);
				qty.setForegroundColor(COLOR_OUTPUTS_BAR);
				qty.minWidth(root.getWidth() * 0.2f);
				qty.label.setAlignment(Align.center);
				
				nested.add(name);
				nested.add(qty).fillY();

				nested.addListener(new ClickListener(Buttons.LEFT) {
					@Override
					public void clicked(InputEvent event, float x, float y){
						Singleton.getInstance(PathManager.class).createPath(
								node.uuid,
								pile.element,
								false,
								event,
								x,
								y);
					}
				});

				node.addOutputTable(pile.element.name(), nested);
				root.add(nested);
			}
		}
	}
	
	/**
	 * Function is called in Main.dispose()
	 */
	public static void dispose(){
		if (MOVE_TEXTURE != null) {
			MOVE_TEXTURE.getTexture().dispose();
		}
		if (REMOVE_TEXTURE != null) {
			REMOVE_TEXTURE.getTexture().dispose();
		}
	}
	
	public static class RemoveNodeImage extends Image {

		private HoverListener hoverListener;
		private ClickListener clickListener;
		private Drawable hover;
		
		public RemoveNodeImage(TextureRegion tex, Skin skin, Node node){
			super(tex);
			
			hover = skin.newDrawable("whiteBackground", 1, 0.2f, 0.2f, 0.65f);
			hoverListener = new HoverListener();
			this.addListener(hoverListener);
			
			clickListener = new ClickListener(){
				
				@Override
				public void clicked(InputEvent event, float x, float y){
					node.setForRemoval();
				}
				
			};
			clickListener.setTapSquareSize(1);
			this.addListener(clickListener);
		}

		@Override
		public void draw(Batch batch, float parentAlpha){
			super.draw(batch, parentAlpha);
			if(hoverListener.isOver()){
				hover.draw(batch, getX(), getY(), getWidth(), getHeight());
			}
		}
	}
	
	public static class MoveNodeImage extends Image {
		
		private HoverListener hoverListener;
		private DragListener dragListener;
		private Drawable hover;
		
		public MoveNodeImage(TextureRegion tex, Skin skin, Table root, Node node){
			super(tex);
			
			hover = skin.newDrawable("whiteBackground", 1, 1, 1, 0.5f);
			hoverListener = new HoverListener();
			this.addListener(hoverListener);
			
			dragListener = new DragListener(){
				
				@Override
				public void drag(InputEvent event, float x, float y, int pointer){
					final float deltaX = this.getDragX() - (tex.getRegionWidth() * 0.5f);
					final float deltaY = this.getDragY() - (tex.getRegionHeight() * 0.5f);
					
					if(pointer == Input.Buttons.LEFT){
						root.moveBy(deltaX, deltaY);
						node.pos.add(deltaX, deltaY);
					}
				}
				
			};
			dragListener.setTapSquareSize(1);
			this.addListener(dragListener);
		}

		@Override
		public void draw(Batch batch, float parentAlpha){
			super.draw(batch, parentAlpha);
			if(hoverListener.isOver()){
				hover.draw(batch, getX(), getY(), getWidth(), getHeight());
			}
		}
	}
	
	public static class HoverableTable extends Table {
		
		private HoverListener hoverListener;
		private Drawable hover;
		
		public HoverableTable(Skin skin){
			super(skin);
			
			hover = skin.newDrawable("whiteBackground", 1, 1, 1, 0.5f);
			hoverListener = new HoverListener();
			this.addListener(hoverListener);
		}

		@Override
		public void draw(Batch batch, float parentAlpha){
			super.draw(batch, parentAlpha);
			if(hoverListener.isOver()){
				hover.draw(batch, getX(), getY(), getWidth(), getHeight());
			}
		}
	}
}
