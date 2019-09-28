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
					final float halfThick = thick * 0.5f;
					
					if((sideData.side & BorderSide.TOP) == BorderSide.TOP){
						shapeDrawer.line(x - halfThick, y + height, x + width + halfThick, y + height, Color.BLACK, thick);
					}
					if((sideData.side & BorderSide.BOTTOM) == BorderSide.BOTTOM){
						shapeDrawer.line(x - halfThick, y, x + width + halfThick, y, Color.BLACK, thick);
					}
					if((sideData.side & BorderSide.LEFT) == BorderSide.LEFT){
						shapeDrawer.line(x, y - halfThick, x, y + height + halfThick, Color.BLACK, thick);
					}
					if((sideData.side & BorderSide.RIGHT) == BorderSide.RIGHT){
						shapeDrawer.line(x + width, y - halfThick, x + width, y + height + halfThick, Color.BLACK, thick);
					}
				}
			}
		};
	}
}
