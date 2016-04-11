package no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.levels.Level;

public class TileCollisionDetector {
	private static Polygon NO_COLLIDE = new Polygon();

	public static void checkTileCollisions(Level level, GameObject go) {
		checkTileCollisions(level, go, null);
	}

	public static void checkTileCollisions(Level level, GameObject go, TileCollisionListener listener) {
		checkTileCollisions(level, go.getBounds(), listener);
	}

	public static void checkTileCollisions(Level level, Polygon go, TileCollisionListener listener) {
		MapLayer layer = level.map.getLayers().get("collision");
		MapObjects objects = layer.getObjects();

		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
			MinimumTranslationVector col = new MinimumTranslationVector();

			// TODO point of optimization in future, if needed
			Rectangle r = rectangleObject.getRectangle();
			Polygon p = new Polygon(new float[] { 0, 0, r.width, 0, r.width, r.height, 0, r.height });
			p.setPosition(r.x, r.y);
			p.setOrigin(0, 0);

			if (Intersector.overlapConvexPolygons(p, go, col)) {

				if (listener != null) {
					listener.onCollide(rectangleObject, col);
				}
			}
		}
	}

	public static void nudge(GameObject go, MinimumTranslationVector col) {
		// TODO
		go.setX(go.getX() - col.depth * col.normal.x);
		go.setY(go.getY() - col.depth * col.normal.y);
	}
}
