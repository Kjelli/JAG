package no.kash.gamedev.jag.commons.tweens.accessors;

import com.badlogic.gdx.math.Vector3;

import aurelienribon.tweenengine.TweenAccessor;

public class Vector3Accessor implements TweenAccessor<Vector3> {

	public static final int TYPE_XYZ = 0;

	@Override
	public int getValues(Vector3 v, int type, float[] returnVals) {
		switch (type) {
		case TYPE_XYZ:
			returnVals[0] = v.x;
			returnVals[1] = v.y;
			returnVals[2] = v.z;
			return 3;
		}
		return 0;
	}

	@Override
	public void setValues(Vector3 v, int type, float[] newVals) {
		switch (type) {
		case TYPE_XYZ:
			v.x = newVals[0];
			v.y = newVals[1];
			v.z = newVals[2];
			break;
		}
	}

}
