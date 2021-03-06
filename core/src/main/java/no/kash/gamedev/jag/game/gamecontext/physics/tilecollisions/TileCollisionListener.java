package no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public interface TileCollisionListener {
	void onCollide(MapObject rectangleObject, MinimumTranslationVector col);
}
