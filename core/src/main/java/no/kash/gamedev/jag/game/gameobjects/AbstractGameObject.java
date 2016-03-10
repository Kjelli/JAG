package no.kash.gamedev.jag.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.game.gamecontext.GameContext;

public abstract class AbstractGameObject implements GameObject {
	private GameContext context;

	public static final float EPSILON = 50.0f;
	protected Vector2 velocity, max_velocity, acceleration, max_acceleration;
	private float x, y;
	private float width, height;
	protected Rectangle bounds;
	protected boolean alive = true;

	protected Sprite sprite;

	protected float speed = 30f;
	protected float rot = 0f;
	protected float scale = 1.0f;

	public AbstractGameObject(float x, float y, float width, float height) {
		velocity = new Vector2();
		acceleration = new Vector2();
		max_acceleration = new Vector2(50, 50);
		max_velocity = new Vector2(50, 50);
		bounds = new Rectangle(x, y, width, height);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public final GameContext getGameContext() {
		return context;
	}

	public final void setGameContext(GameContext context) {
		this.context = context;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		bounds.x = x;
		this.x = x;
	}

	public void setY(float y) {
		bounds.y = y;
		this.y = y;
	}

	public Vector2 velocity() {
		return velocity;
	}

	public Vector2 acceleration() {
		return acceleration;
	}

	public Vector2 maxAcceleration() {
		return acceleration;
	}

	public Vector2 maxVelocity() {
		return acceleration;
	}

	@Override
	public final float getCenterX() {
		return x + getWidth() / 2;
	}

	@Override
	public final float getCenterY() {
		return y + getHeight() / 2;
	}

	@Override
	public void setMaxSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public float getMaxSpeed() {
		return speed;
	}

	@Override
	public final float getWidth() {
		return width * getScale();
	}

	public final void setWidth(float width) {
		this.width = width / getScale();
	}

	@Override
	public final float getHeight() {
		return height * getScale();
	}

	public final void setHeight(float height) {
		this.height = height / getScale();
	}

	protected final void move(float delta) {
		x += velocity.x * delta;
		y += velocity.y * delta;
	}

	@Override
	public void destroy() {
		alive = false;
		if (context != null) {
			context.despawn(this);
		}
	}

	public boolean contains(float x, float y) {
		return (x >= this.x && x <= this.x + this.width) && (y >= this.y && y <= this.y + this.height);
	}

	public boolean intersects(GameObject other) {
		return Intersector.overlaps(other.getBounds(), this.getBounds());
	}

	@Override
	public float distanceTo(GameObject other) {
		return (float) Math.hypot(other.getCenterX() - getCenterX(), other.getCenterY() - getCenterY());
	}

	@Override
	public float distanceTo(float x, float y) {
		return (float) Math.hypot(x - getCenterX(), y - getCenterY());
	}

	public float angleTo(float x, float y) {
		float deltaY = y - getCenterY();
		float deltaX = x - getCenterX();
		float angle = (float) (Math.atan2(deltaY, deltaX));
		return angle;
	}

	public float angleTo(GameObject other) {
		float deltaY = other.getCenterY() - getCenterY();
		float deltaX = other.getCenterX() - getCenterX();
		float angle = (float) (Math.atan2(deltaY, deltaX));

		return angle;
	}

	@Override
	public void moveTowards(GameObject other) {
		float angle = angleTo(other);
		velocity().x = (float) Math.cos(angle);
		velocity().y = (float) Math.sin(angle);

	}

	public void moveTowards(float x, float y) {
		float angle = angleTo(x, y);
		velocity().x = (float) Math.cos(angle);
		velocity().y = (float) Math.sin(angle);

	}

	@Override
	public void moveFrom(GameObject other) {
		float angle = (float) (angleTo(other) - Math.PI);
		velocity().x = (float) Math.cos(angle);
		velocity().y = (float) Math.sin(angle);

	}

	public void moveFrom(float x, float y) {
		float angle = (float) (angleTo(x, y) - Math.PI);
		velocity().x = (float) Math.cos(angle);
		velocity().y = (float) Math.sin(angle);

	}

	@Override
	public void draw(SpriteBatch batch) {
		Draw.sprite(batch, this);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public float getScale() {
		return scale;
	}

	@Override
	public Sprite getSprite() {
		return sprite;
	}

	@Override
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void onSpawn() {
		// Left empty intentionally
	}

	public void onDespawn() {
		// Left empty intentionally
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	public void setRotation(float rot) {
		this.rot = rot % 360;
		if (sprite != null) {
			sprite.setRotation(this.rot);
		}
	}

	public float getRotation() {
		return rot;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Cleanup method, use with caution
	 */
	@Override
	public void dispose() {
		// Left empty intentionally
	}
}