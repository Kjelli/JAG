package no.kash.gamedev.jag.commons.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public class Draw {

	public static void sprite(SpriteBatch batch, GameObject go) {
		if (go.getSprite() == null) {
			System.err.println("Object has no sprite: " + go);
			return;
		} else {
			sprite(batch, go.getSprite(), go.getX(), go.getY(), go.getWidth(), go.getHeight(), go.getScale(),
					go.getScale(), go.getRotation(), go.getSprite().getColor(), go.velocity().x < 0);
		}
	}

	public static void sprite(SpriteBatch batch, Sprite sprite, float x, float y, float w, float h, float rot) {

		sprite(batch, sprite, x, y, w, h, 1.0f, 1.0f, rot, sprite.getColor(), false);
	}

	public static void sprite(SpriteBatch batch, Sprite sprite, float x, float y, float w, float h, float scaleX,
			float scaleY, float rot, Color color, boolean flipX) {

		Color c_old = null;
		if (sprite.getColor() != null) {
			c_old = batch.getColor();
			batch.setColor(color);
		}
		batch.draw(sprite.getTexture(), x, y, sprite.getOriginX() * scaleX, sprite.getOriginY() * scaleY, w, h, scaleX,
				scaleY, (float) (rot * 180 / Math.PI), 0, 0, sprite.getRegionWidth(), sprite.getRegionHeight(), flipX,
				sprite.isFlipY());

		if (sprite.getColor() != null) {
			batch.setColor(c_old);
		}
	}

	public static void sprite(SpriteBatch batch, Sprite s) {
		sprite(batch, s, s.getX(), s.getY(), s.getWidth(), s.getHeight(), s.getScaleX(), s.getScaleY(), s.getRotation(),
				s.getColor(), false);
	}

}