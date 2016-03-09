package no.kash.gamedev.jag.game.gameobjects.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;

public class Bullet extends AbstractGameObject{
	
	protected float direction;
	protected float damage;
	
	protected float speed = 300f;
	
	public Bullet(float x, float y, float width, float height, float direction) {
		super(x, y, width, height);
		this.direction = (float) (direction);
		setSprite(new Sprite(Assets.bullet));
		setRotation(direction);
	}

	@Override
	public void update(float delta) {
		velocity.x = (float) (Math.cos((direction*Math.PI/180)+(Math.PI/2)) * speed);
		velocity.y = (float) (Math.sin((direction*Math.PI/180)+(Math.PI/2)) * speed);
		
		move(delta);
		
		outOfBounds();
	}
	
	private void outOfBounds() {
		if(position.x > Gdx.graphics.getWidth() || position.x<0
				||position.y > Gdx.graphics.getHeight() || position.y < 0){
			destroy();
		}
	}

	

}
