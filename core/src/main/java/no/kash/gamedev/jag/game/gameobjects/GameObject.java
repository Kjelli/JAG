package no.kash.gamedev.jag.game.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gamecontext.GameContext;

public interface GameObject {

	public Vector2 velocity();

	public Vector2 acceleration();

	public Vector2 maxAcceleration();

	public float getX();

	public float getY();

	public void setX(float x);

	public void setY(float y);

	public float getCenterX();

	public float getCenterY();

	public void setMaxSpeed(float speed);

	public float getMaxSpeed();

	public float getWidth();

	public void setWidth(float width);

	public float getHeight();

	public void setHeight(float height);

	public Rectangle getBounds();

	public void setScale(float scale);

	public float getScale();

	public void draw(SpriteBatch batch);

	public void update(float delta);

	public void setSprite(Sprite sprite);

	public Sprite getSprite();

	public void onSpawn();

	public void onDespawn();

	public void destroy();

	public void setGameContext(GameContext gameContext);

	public boolean contains(float x, float y);

	public boolean intersects(GameObject other);

	public float distanceTo(GameObject other);

	public float distanceTo(float x, float y);

	public float angleTo(GameObject other);

	public float angleTo(float x, float y);

	public void moveTowards(GameObject other);

	public void moveTowards(float x, float y);

	public void moveFrom(GameObject other);

	public void moveFrom(float x, float y);

	public void setRotation(float rot);

	public float getRotation();

	public boolean isAlive();

	public void updateAliveTime(float delta);

	public float getAliveTime();

	public void dispose();

}