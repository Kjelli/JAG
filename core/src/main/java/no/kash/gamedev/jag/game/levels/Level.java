package no.kash.gamedev.jag.game.levels;

import java.awt.Point;
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
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.levels.pathfinding.LevelPathFinder;

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
	public ArrayList<Vector2> itemSpawns;
	public ArrayList<PlayerSpawnPoint> playerSpawns;
	public Map<Integer, Rectangle> teamSpawnZones;
	public WeaponSpawner weaponSpawner;
	public ItemSpawner itemSpawner;
	public boolean itemsExists;

	public Level(GameSession session, SpriteBatch batch, GameContext context) {
		this.context = context;
		this.session = session;
		this.batch = batch;
		init();
	}

	public Point getTileCoordinate(GameObject go) {
		return new Point((int) (go.getCenterX() / tileWidth), (int) (go.getCenterY() / tileHeight));
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
		weaponSpawner = determineWeaponSpawnPoints();
		
		itemsExists = true;
		itemSpawner = determineItemSpawnPoints();

		renderer.setView(context.getStage().getCamera().projection, 0, 0, width * tileWidth, height * tileHeight);
		LevelPathFinder.build(this);
	}

	public void spawnWeaponTiles() {
		
		weaponSpawner.spawnWeaponTiles();
	}
	
	public void spawnItemTiles(){
		if(itemsExists)
		itemSpawner.spawnItemTiles();
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

	private WeaponSpawner determineWeaponSpawnPoints() {
		MapObjects weaponSpawnPoints = map.getLayers().get("weaponspawn").getObjects();
		ArrayList<WeaponSpawnTileInfo> tempList = new ArrayList<>();
		for (int i = 0; i < weaponSpawnPoints.getCount(); i++) {
			MapObject temp = weaponSpawnPoints.get(i);
			GunType gunType = temp.getProperties().containsKey("type")
					? GunType.valueOf((String) temp.getProperties().get("type")) : null;
			float spawnRate = temp.getProperties().containsKey("rate")
					? Float.valueOf((String) temp.getProperties().get("rate")) : WeaponSpawner.SPAWN_RATE;
			tempList.add(new WeaponSpawnTileInfo(
					new Vector2(temp.getProperties().get("x", Float.class), temp.getProperties().get("y", Float.class)),
					gunType, spawnRate));
		}
		return new WeaponSpawner(tempList, context);
	}
	
	private ItemSpawner determineItemSpawnPoints() {
		ArrayList<ItemSpawnTileInfo> tempList = new ArrayList<>();
		try{
			
		MapObjects itemSpawnPoints = map.getLayers().get("itemspawn").getObjects();
		
		for (int i = 0; i < itemSpawnPoints.getCount(); i++) {
			MapObject temp = itemSpawnPoints.get(i);
			ItemType itemType = temp.getProperties().containsKey("type") ? ItemType.valueOf((String)temp.getProperties().get("type")) : null;
			float spawnRate = temp.getProperties().containsKey("rate") ? Float.valueOf((String)temp.getProperties().get("rate")) : ItemSpawner.SPAWN_RATE;
			tempList.add(new ItemSpawnTileInfo(new Vector2(temp.getProperties().get("x", Float.class),
					temp.getProperties().get("y", Float.class)), itemType, spawnRate ));
		}
		}catch(NullPointerException e){
			System.out.println("No items found");
			itemsExists = false;
			return null;
		}
		return new ItemSpawner(tempList, context);
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
		if(itemsExists)
		itemSpawner.update(delta);
	}
	


	public void resetPlayerSpawns() {
		for (PlayerSpawnPoint spawn : playerSpawns) {
			spawn.taken = false;
		}
	}
}
