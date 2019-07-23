package com.bigbass.nep.panel;

import com.badlogic.gdx.InputAdapter;

public abstract class ScrollwheelInputAdapter extends InputAdapter {
	
	@Override
	public abstract boolean scrolled(int amount);
}
