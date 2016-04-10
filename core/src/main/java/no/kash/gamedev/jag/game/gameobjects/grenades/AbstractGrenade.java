package no.kash.gamedev.jag.game.gameobjects.grenades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.players.CircularHitbox;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public abstract class AbstractGrenade extends AbstractGameObject implements Grenade {

	public static final float TIME_TO_LIVE_MAX = 1.0f;
	public static final float SPEED = 300f;

	public float timeToLive = TIME_TO_LIVE_MAX;
	public final float direction;
	public final float power;

	public final Player thrower;
	public final CircularHitbox hitbox;
	public Cooldown bounceCooldown;

	public final TileCollisionListener tileListener;

	public AbstractGrenade(Player thrower, Texture texture, float x, float y, float direction, float power) {
		super(x, y, 16, 16);
		setSprite(new Sprite(texture));
		getSprite().setOrigin(getWidth() / 2, getHeight() / 2);
		this.direction = direction;
		this.power = power;
		this.thrower = thrower;
		this.bounceCooldown = new Cooldown(0.1f);
		this.hitbox = new CircularHitbox(x, y, getWidth()/3, getHeight()/3);
		acceleration().x = EPSILON;
		acceleration().y = EPSILON;
		velocity().x = (float) (thrower.velocity().x + Math.cos(direction) * power * SPEED);
		velocity().y = (float) (thrower.velocity().y + Math.sin(direction) * power * SPEED);
		this.tileListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
				collision(rectangleObject, col);
			}
		};
	}

	protected abstract void collision(MapObject rectangleObject, MinimumTranslationVector col);

	@Override
	public void update(float delta) {
		if ((timeToLive -= delta) <= 0) {
			timeOut();
		}
		bounceCooldown.update(delta);
		
		velocity().x *= 0.98f;
		velocity().y *= 0.98f;
		setRotation(
				(Math.max(timeToLive - TIME_TO_LIVE_MAX / 2, 0) / TIME_TO_LIVE_MAX) * power * 10 + power * direction);
		hitbox.update(getCenterX(), getCenterY(), getRotation());
		move(delta);
		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileListener);
	}
	
	@Override
	public Polygon getBounds() {
		return hitbox.poly;
	}

	public Player getThrower() {
		return thrower;
	}

}
