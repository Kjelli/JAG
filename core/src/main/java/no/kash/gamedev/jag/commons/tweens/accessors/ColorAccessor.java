package no.kash.gamedev.jag.commons.tweens.accessors;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.TweenAccessor;

public class ColorAccessor implements TweenAccessor<Color> {

	public static final int TYPE_RGBA = 1, TYPE_A = 2;

	@Override
	public int getValues(Color c, int type, float[] returnVals) {
		switch (type) {
		case TYPE_RGBA:
			returnVals[0] = c.r;
			returnVals[1] = c.g;
			returnVals[2] = c.b;
			returnVals[3] = c.a;
			return 4;
		case TYPE_A:
			returnVals[0] = c.a;
			return 1;
		}
		return 0;
	}

	@Override
	public void setValues(Color c, int type, float[] newVals) {
		switch (type) {
		case TYPE_RGBA:
			c.r = newVals[0];
			c.g = newVals[1];
			c.b = newVals[2];
			c.a = newVals[3];
			break;
		case TYPE_A:
			c.a = newVals[0];
			break;
		}
	}

}
