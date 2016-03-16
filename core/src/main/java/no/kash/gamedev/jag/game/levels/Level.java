package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamesettings.GameSettings;

public class Level {
	public TiledMap map;
	public int width, height;
	public int tileWidth, tileHeight;
	public float x, y;

	public OrthogonalTiledMapRenderer renderer;

	public ArrayList<Vector2> wepSpawns;
	Spawner spawn;

	public Level(GameSettings settings, SpriteBatch batch, GameContext context) {
		map = new TmxMapLoader().load(settings.mapFilename);

		width = (Integer) map.getProperties().get("width", -1, Integer.class);
		height = (Integer) map.getProperties().get("height", -1, Integer.class);
		tileWidth = (Integer) map.getProperties().get("tilewidth", -1, Integer.class);
		tileHeight = (Integer) map.getProperties().get("tileheight", -1, Integer.class);
		renderer = new OrthogonalTiledMapRenderer(map, batch);

		wepSpawns = deterimnePoints("weaponspawn");
		spawn = new Spawner(wepSpawns, context);

		renderer.setView(context.getStage().getCamera().projection, 0, 0, width * tileWidth, height * tileHeight);
	}

	private ArrayList<Vector2> deterimnePoints(String layer) {
		MapObjects weaponSpawnPoints = map.getLayers().get(layer).getObjects();
		ArrayList<Vector2> tempList = new ArrayList<Vector2>();
		for (int i = 0; i < weaponSpawnPoints.getCount(); i++) {
			MapObject temp = weaponSpawnPoints.get(i);
			tempList.add(new Vector2(temp.getProperties().get("x", Float.class),
					temp.getProperties().get("y", Float.class)));
			;
		}
		return tempList;
	}

	public void dispose() {
		map.dispose();
	}

	public void render() {

		renderer.getBatch().end();
		renderer.render();
		renderer.getBatch().begin();
	}

	public void update(float delta) {
		spawn.update(delta);
	}
}
