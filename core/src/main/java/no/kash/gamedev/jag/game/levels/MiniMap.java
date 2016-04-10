package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class MiniMap {
	private static final BitmapFont font = Assets.fontSmall;

	private String filepath;

	private float x, y;
	private float mapWidth, mapHeight, tileWidth = 8, tileHeight = 8;
	private float scale = 0.5f;
	private String name;
	private boolean available;
	private boolean teamMap;
	private boolean ffaMap;
	private int maxPlayersFFA;

	GlyphLayout nameLabel;
	Sprite[][] cells;

	private MiniMap(TiledMap map, String filepath, Sprite[][] cells) {
		this.filepath = filepath;
		this.cells = cells;
		this.mapWidth = cells.length;
		this.mapHeight = cells[0].length;
		this.setName((String) map.getProperties().get("name"));
		setFfaMap(Boolean.valueOf((String) map.getProperties().get("ffa")));
		setTeamMap(Boolean.valueOf((String) map.getProperties().get("team")));
		this.maxPlayersFFA = map.getLayers().get("spawnpoints").getObjects().getCount();
		this.nameLabel = new GlyphLayout(font, getName(), Color.WHITE, -1, -1, false);
	}

	public void setX(float x) {
		this.x = x;
		updateSprites();
	}

	private void updateSprites() {
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				cells[x][y].setSize(tileWidth * scale, tileHeight * scale);
				cells[x][y].setX(this.x + x * tileWidth * scale);
				cells[x][y].setY(this.y + y * tileHeight * scale);
			}
		}
	}

	public void setY(float y) {
		this.y = y;
		updateSprites();
	}

	public void setTargetWidth(float width) {
		scale(width / (this.mapWidth * tileWidth));
	}

	public float getMapWidth() {
		return mapWidth;
	}

	public float getMapHeight() {
		return mapHeight;
	}

	public float getEffectiveMapWidth() {
		return mapWidth * tileWidth * scale;
	}

	public float getEffectiveMapHeight() {
		return mapHeight * tileHeight * scale;
	}

	public void draw(SpriteBatch batch) {
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				cells[x][y].draw(batch);
			}
		}
		font.draw(batch, nameLabel, x + getEffectiveMapWidth() / 2 - nameLabel.width / 2, y - nameLabel.height);
	}

	public void scale(float scale) {
		this.scale = scale;
		updateSprites();
	}

	public void setAvailable(boolean available) {
		this.available = available;
		Color avail = new Color(Color.WHITE);
		if (!available) {
			nameLabel.setText(font, "N/A", Color.WHITE, -1, -1, false);
			avail.r = 0.5f;
			avail.g = 0.5f;
			avail.b = 0.5f;
			avail.a = 0.5f;
		}
		for (Sprite[] sa : cells) {
			for (Sprite s : sa) {
				s.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
			}
		}
	}

	public static MiniMap build(FileHandle mapFile) {
		TmxMapLoader loader = new TmxMapLoader();
		TiledMap map = loader.load(mapFile.path());

		Sprite[][] cells = new Sprite[(Integer) map.getProperties().get("width", -1, Integer.class)][(Integer) map
				.getProperties().get("height", -1, Integer.class)];

		for (int i = 0; i < map.getLayers().getCount(); i++) {
			MapLayer layer = map.getLayers().get(i);
			TiledMapTileLayer tileLayer;
			try {
				tileLayer = (TiledMapTileLayer) layer;
			} catch (ClassCastException e) {
				continue;
			}
			for (int x = 0; x < tileLayer.getWidth(); x++) {
				for (int y = 0; y < tileLayer.getHeight(); y++) {
					Cell cell = tileLayer.getCell(x, y);
					if (cell == null) {
						continue;
					}
					TextureRegion t = cell.getTile().getTextureRegion();
					Sprite sprite = new Sprite(t);
					sprite.setX(x * tileLayer.getTileWidth());
					sprite.setY(y * tileLayer.getTileHeight());
					sprite.setSize(tileLayer.getTileWidth(), tileLayer.getTileHeight());
					cells[x][y] = sprite;
				}
			}
		}
		return new MiniMap(map, mapFile.path(), cells);
	}

	public boolean isAvailable() {
		return available;
	}

	public boolean isFfaMap() {
		return ffaMap;
	}

	public boolean isTeamMap() {
		return teamMap;
	}

	public String getFilename() {
		return filepath;
	}

	public void setTeamMap(boolean teamMap) {
		this.teamMap = teamMap;
	}

	public void setFfaMap(boolean ffaMap) {
		this.ffaMap = ffaMap;
	}

	public boolean isCompatible(GameSession session) {
		switch (session.settings.getSelectedValue(Defs.SESSION_GM, GameMode.class)) {
		case STANDARD_FFA:
			if (!isFfaMap() || session.players.size() > maxPlayersFFA) {
				setAvailable(false);
				return false;
			}
			break;
		case STANDARD_TEAM:
			if (!isTeamMap()) {
				setAvailable(false);
				return false;
			}
			break;
		default:
			break;

		}
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
