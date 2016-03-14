package no.kash.gamedev.jag.commons.tweens;

public class TweenableFloat {
	private float value;

	public TweenableFloat(float value) {
		setValue(value);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
