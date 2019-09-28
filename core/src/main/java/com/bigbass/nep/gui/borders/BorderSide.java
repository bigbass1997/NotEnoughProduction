package com.bigbass.nep.gui.borders;

import java.util.ArrayList;
import java.util.List;

public class BorderSide {
	
	public static final int TOP = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;
	public static final int ALL = 15;
	
	public int side;
	public int thickness;
	
	public BorderSide(int side, int thickness){
		this.side = side;
		this.thickness = thickness;
	}
	
	public static BorderSide top(int thickness){
		return new BorderSide(TOP, thickness);
	}

	public static BorderSide bottom(int thickness){
		return new BorderSide(BOTTOM, thickness);
	}

	public static BorderSide left(int thickness){
		return new BorderSide(LEFT, thickness);
	}

	public static BorderSide right(int thickness){
		return new BorderSide(RIGHT, thickness);
	}
	
	public static BorderSide[] mask(int sidesMask, int thickness){

		if((sidesMask & ALL) == ALL){
			BorderSide[] sides = new BorderSide[4];
			
			sides[0] = top(thickness);
			sides[1] = bottom(thickness);
			sides[2] = left(thickness);
			sides[3] = right(thickness);
			
			return sides;
		}

		List<BorderSide> sides = new ArrayList<BorderSide>();
		
		if((sidesMask & TOP) == TOP){
			sides.add(top(thickness));
		}

		if((sidesMask & BOTTOM) == BOTTOM){
			sides.add(bottom(thickness));
		}

		if((sidesMask & LEFT) == LEFT){
			sides.add(left(thickness));
		}

		if((sidesMask & RIGHT) == RIGHT){
			sides.add(right(thickness));
		}
		
		return sides.toArray(new BorderSide[0]);
	}
}
