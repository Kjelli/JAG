package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class MiniMap {
	private float x, y;
	private float mapWidth, mapHeight, tileWidth = 8, tileHeight = 8;
	private float scale = 0.5f;
	Sprite[][] cells;

	private MiniMap(Sprite[][] cells) {
		this.cells = cells;
		this.mapWidth = cells.length;
		this.mapHeight = cells[0].length;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
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
		return mapHeight * tileHeight* scale;
	}

	public void draw(SpriteBatch batch) {
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				batch.draw(cells[x][y], this.x + x * tileWidth * scale, this.y + y * tileHeight * scale,
						tileWidth * scale, tileHeight * scale);
			}
		}
	}

	public void scale(float scale) {
		this.scale = scale;
	}

	public static MiniMap build(TiledMap map) {
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
					cells[x][y] = sprite;
				}
			}
		}
		return new MiniMap(cells);
	}
}
