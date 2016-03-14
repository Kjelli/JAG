package no.kash.gamedev.jag.game.tilecollisions;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.levels.Level;

public class TileCollisionDetector {
	private static Rectangle NO_COLLIDE = new Rectangle(-1, -1, -1, -1);

	public static void checkTileCollisions(Level level, GameObject go) {
		checkTileCollisions(level, go, null);
	}

	public static void checkTileCollisions(Level level, GameObject go, TileCollisionListener listener) {
		MapLayer layer = level.map.getLayers().get("collision");
		MapObjects objects = layer.getObjects();

		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
			Rectangle intersection = new Rectangle(NO_COLLIDE);

			// TODO point of optimization in future, if needed

			if (Intersector.intersectRectangles(rectangleObject.getRectangle(), go.getBounds(), intersection)) {
				if (intersection.equals(NO_COLLIDE)) {
					return;
				}

				if (listener != null) {
					listener.onCollide(rectangleObject, intersection);
				}
			}
		}
	}

	public static void nudge(GameObject go, Rectangle intersection) {
		// Determine colliding edge:
		if (intersection.width < intersection.height) {
			// Collision horizontal
			if (go.getCenterX() > intersection.x) {
				go.setX(go.getX() + intersection.width);
			} else {
				go.setX(go.getX() - intersection.width);
			}
		} else {

			// Collision vertical
			if (go.getCenterY() > intersection.y) {
				go.setY(go.getY() + intersection.height);
			} else {
				go.setY(go.getY() - intersection.height);
			}
		}
	}
}
