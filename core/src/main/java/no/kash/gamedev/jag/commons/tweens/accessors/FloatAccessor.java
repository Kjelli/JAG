package no.kash.gamedev.jag.commons.tweens.accessors;

import aurelienribon.tweenengine.TweenAccessor;
import no.kash.gamedev.jag.commons.tweens.TweenableFloat;

public class FloatAccessor implements TweenAccessor<TweenableFloat> {

	public static final int TYPE_VALUE = 0;

	@Override
	public int getValues(TweenableFloat v, int type, float[] returnVals) {
		switch (type) {
		case TYPE_VALUE:
			returnVals[0] = v.getValue();
			return 1;
		}
		return 0;
	}

	@Override
	public void setValues(TweenableFloat v, int type, float[] newVals) {
		switch (type) {
		case TYPE_VALUE:
			v.setValue(newVals[0]);
			break;
		}
	}

}
