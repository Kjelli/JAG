package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Hitbox {
	Rectangle rect;

	public Hitbox(float x, float y, float width, float height) {
		rect = new Rectangle(x, y, width, height);
	}

	public Rectangle intersection(Rectangle rect) {
		Rectangle intersection = new Rectangle();
		if (Intersector.intersectRectangles(this.rect, rect, intersection)) {
			return intersection;
		} else {
			return null;
		}
	}
}
