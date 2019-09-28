package com.bigbass.nep.gui.borders;

import com.badlogic.gdx.graphics.Color;

import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class BorderUtil {
	
	public static ShapeDrawerDrawable newDrawable(ShapeDrawer shapeDrawer, BorderSide[] borders){
		return new ShapeDrawerDrawable(shapeDrawer){

			@Override
			public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
				for(BorderSide sideData : borders){
					final int thick = sideData.thickness;
					final int halfThickUp = Math.round(thick * 0.5f);
					final int halfThickDown = (int) (thick * 0.5f);
					
					if((sideData.side & BorderSide.TOP) == BorderSide.TOP){
						shapeDrawer.line(x - halfThickDown, y + height, x + width + halfThickUp, y + height, Color.BLACK, thick);
					}
					if((sideData.side & BorderSide.BOTTOM) == BorderSide.BOTTOM){
						shapeDrawer.line(x, y, x + width, y, Color.BLACK, thick);
					}
					if((sideData.side & BorderSide.LEFT) == BorderSide.LEFT){
						shapeDrawer.line(x, y, x, y + height, Color.BLACK, thick);
					}
					if((sideData.side & BorderSide.RIGHT) == BorderSide.RIGHT){
						shapeDrawer.line(x + width, y, x + width, y + height, Color.BLACK, thick);
					}
				}
			}
		};
	}
}
