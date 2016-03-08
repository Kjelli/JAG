package no.kash.gamedev.jag.commons.tweens.accessors;

import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.TweenAccessor;

public class Vector2Accessor implements TweenAccessor<Vector2> {

	static final int TYPE_XY = 0;

	@Override
	public int getValues(Vector2 v, int type, float[] returnVals) {
		switch (type) {
		case TYPE_XY:
			returnVals[0] = v.x;
			returnVals[1] = v.y;
			return 2;
		}
		return 0;
	}

	@Override
	public void setValues(Vector2 v, int type, float[] newVals) {
		switch (type) {
		case TYPE_XY:
			v.x = newVals[0];
			v.y = newVals[1];
			break;
		}
	}

}
