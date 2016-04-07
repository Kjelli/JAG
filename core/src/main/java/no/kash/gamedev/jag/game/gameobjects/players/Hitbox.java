package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Hitbox {
	Polygon poly;

	public Hitbox(float x, float y, float width, float height) {
		poly = new Polygon(new float[] { 0, 0, width, 0, width, height, 0, height });
		poly.setPosition(x, y);
		poly.setOrigin(0, 0);
	}

	public Polygon intersection(Polygon poly) {
		Polygon intersection = new Polygon();
		if (Intersector.intersectPolygons(this.poly, poly, intersection)) {
			return intersection;
		} else {
			return null;
		}
	}

	public void update(float x, float y) {
		poly.setPosition(x, y);
	}
}
