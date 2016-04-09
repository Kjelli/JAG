package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class CircularHitbox {
	public Polygon poly;

	public CircularHitbox(float x, float y, float width, float height) {

		int vertexCount = 32;
		float[] vertices = new float[vertexCount * 2];
		for (int i = 0; i < vertexCount * 2; i += 2) {
			vertices[i] = (float) (width / 2 + width * Math.cos(i * 2.0f * Math.PI / vertexCount));
			vertices[i + 1] = (float) (height / 2 + height * Math.sin(i * 2.0f * Math.PI / vertexCount));
		}

		poly = new Polygon(vertices);

		poly.setPosition(x, y);
		poly.setOrigin(width / 2, height / 2);
	}

	public Polygon intersection(Polygon poly) {
		Polygon intersection = new Polygon();
		if (Intersector.intersectPolygons(this.poly, poly, intersection)) {
			return intersection;
		} else {
			return null;
		}
	}

	public void update(float x, float y, float rot) {
		poly.setPosition(x, y);
		poly.setRotation(rot);
	}
}
