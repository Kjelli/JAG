package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class Level {
	public TiledMap map;
	public int width, height;
	public int tileWidth, tileHeight;
	public float x, y;

	private GameContext context;
	private GameSession session;
	private SpriteBatch batch;

	public OrthogonalTiledMapRenderer renderer;

	public ArrayList<Vector2> weaponSpawns;
	public ArrayList<PlayerSpawnPoint> playerSpawns;
	public Map<Integer, Rectangle> teamSpawnZones;
	public WeaponSpawner weaponSpawner;

	public Level(GameSession session, SpriteBatch batch, GameContext context) {
		this.context = context;
		this.session = session;
		this.batch = batch;
		init();
	}

	public void init() {
		map = new TmxMapLoader().load(session.mapFilename);

		width = (Integer) map.getProperties().get("width", -1, Integer.class);
		height = (Integer) map.getProperties().get("height", -1, Integer.class);
		tileWidth = (Integer) map.getProperties().get("tilewidth", -1, Integer.class);
		tileHeight = (Integer) map.getProperties().get("tileheight", -1, Integer.class);
		renderer = new OrthogonalTiledMapRenderer(map, batch);

		playerSpawns = determinePlayerSpawnPoints();
		try {
			teamSpawnZones = determineTeamSpawnZones();
		} catch (NullPointerException npe) {
			System.err.println("NOT A TEAM MAP!");
		}
		weaponSpawns = determineWeaponSpawnPoints();
		weaponSpawner = new WeaponSpawner(weaponSpawns, context);

		renderer.setView(context.getStage().getCamera().projection, 0, 0, width * tileWidth, height * tileHeight);
	}

	public void spawnWeaponTiles() {
		weaponSpawner.spawnWeaponTiles();
	}

	private Map<Integer, Rectangle> determineTeamSpawnZones() {
		MapObjects teamSpawnZones = map.getLayers().get("spawnzones").getObjects();
		Map<Integer, Rectangle> tempMap = new HashMap<>();
		for (int i = 0; i < teamSpawnZones.getCount(); i++) {
			MapObject temp = teamSpawnZones.get(i);
			int teamId = Integer.parseInt((String) temp.getProperties().get("team_id"));
			tempMap.put(teamId,
					new Rectangle(temp.getProperties().get("x", Float.class),
							temp.getProperties().get("y", Float.class), temp.getProperties().get("width", Float.class),
							temp.getProperties().get("height", Float.class)));
		}
		return tempMap;
	}

	private ArrayList<PlayerSpawnPoint> determinePlayerSpawnPoints() {
		MapObjects weaponSpawnPoints = map.getLayers().get("spawnpoints").getObjects();
		ArrayList<PlayerSpawnPoint> tempList = new ArrayList<>();
		for (int i = 0; i < weaponSpawnPoints.getCount(); i++) {
			MapObject temp = weaponSpawnPoints.get(i);
			tempList.add(new PlayerSpawnPoint(temp.getProperties().get("x", Float.class),
					temp.getProperties().get("y", Float.class)));
		}
		return tempList;
	}

	private ArrayList<Vector2> determineWeaponSpawnPoints() {
		MapObjects weaponSpawnPoints = map.getLayers().get("weaponspawn").getObjects();
		ArrayList<Vector2> tempList = new ArrayList<>();
		for (int i = 0; i < weaponSpawnPoints.getCount(); i++) {
			MapObject temp = weaponSpawnPoints.get(i);
			tempList.add(new Vector2(temp.getProperties().get("x", Float.class),
					temp.getProperties().get("y", Float.class)));
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
		weaponSpawner.update(delta);
	}

	public void resetPlayerSpawns() {
		for (PlayerSpawnPoint spawn : playerSpawns) {
			spawn.taken = false;
		}
	}
}
