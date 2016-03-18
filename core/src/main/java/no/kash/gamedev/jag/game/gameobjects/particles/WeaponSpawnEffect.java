package no.kash.gamedev.jag.game.gameobjects.particles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.assets.Assets;

public class WeaponSpawnEffect extends AbstractParticle{
	
	private float scalingValue;
	
	public WeaponSpawnEffect(float x, float y, float width, float height, float timeToLive) {
		super(x, y, width, height, timeToLive);
		setSprite(new Sprite(Assets.wepSpawnEffect));
		scalingValue = 0.5f;
		setScale(scalingValue);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(getSprite(),getX(),getY());
	}


	@Override
	public void onTimeout() {
		destroy();
	}

	@Override
	public void updateParticle(float delta) {
		scalingValue += 0.1f;
		setScale(scalingValue);
	}

}
