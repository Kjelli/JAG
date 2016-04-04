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
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class Level {
	public TiledMap map;
	public int width, height;
	public int tileWidth, tileHeight;
	public float x, y;

	private GameContext context;
	public OrthogonalTiledMapRenderer renderer;

	public ArrayList<Vector2> weaponSpawns;
	public ArrayList<PlayerSpawnPoint> playerSpawns;
	public Spawner spawner;

	public Level(GameSession settings, SpriteBatch batch, GameContext context) {
		this.context = context;
		map = new TmxMapLoader().load(settings.mapFilename);

		width = (Integer) map.getProperties().get("width", -1, Integer.class);
		height = (Integer) map.getProperties().get("height", -1, Integer.class);
		tileWidth = (Integer) map.getProperties().get("tilewidth", -1, Integer.class);
		tileHeight = (Integer) map.getProperties().get("tileheight", -1, Integer.class);
		renderer = new OrthogonalTiledMapRenderer(map, batch);

		spawnWeaponSpawns();
		playerSpawns = determinePlayerSpawnPoints("spawnpoints");

		renderer.setView(context.getStage().getCamera().projection, 0, 0, width * tileWidth, height * tileHeight);
	}

	private ArrayList<PlayerSpawnPoint> determinePlayerSpawnPoints(String layer) {
		MapObjects weaponSpawnPoints = map.getLayers().get(layer).getObjects();
		ArrayList<PlayerSpawnPoint> tempList = new ArrayList<>();
		for (int i = 0; i < weaponSpawnPoints.getCount(); i++) {
			MapObject temp = weaponSpawnPoints.get(i);
			tempList.add(new PlayerSpawnPoint(temp.getProperties().get("x", Float.class),
					temp.getProperties().get("y", Float.class)));
			;
		}
		return tempList;
	}

	private ArrayList<Vector2> determineWeaponSpawnPoints(String layer) {
		MapObjects weaponSpawnPoints = map.getLayers().get(layer).getObjects();
		ArrayList<Vector2> tempList = new ArrayList<>();
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

	public void spawnWeaponSpawns() {
		GameContext context = this.context;
		weaponSpawns = determineWeaponSpawnPoints("weaponspawn");
		spawner = new Spawner(weaponSpawns, context);
	}

	public void render() {

		renderer.getBatch().end();
		renderer.render();
		renderer.getBatch().begin();
	}

	public void update(float delta) {
		spawner.update(delta);
	}

	public void resetPlayerSpawns() {
		for(PlayerSpawnPoint spawn : playerSpawns){
			spawn.taken = false;
		}
	}
}
