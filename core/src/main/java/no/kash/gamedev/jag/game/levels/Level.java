package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Level {
	public TiledMap map;
	public int width, height;
	public int tileWidth, tileHeight;
	public float x, y;

	public OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera camera;

	public Level(OrthographicCamera camera, SpriteBatch batch) {
		this.camera = camera;
		map = new TmxMapLoader().load("sumoarena1.tmx");
		width = (Integer) map.getProperties().get("width", -1, Integer.class);
		height = (Integer) map.getProperties().get("height", -1, Integer.class);
		tileWidth = (Integer) map.getProperties().get("tilewidth", -1, Integer.class);
		tileHeight = (Integer) map.getProperties().get("tileheight", -1, Integer.class);
		renderer = new OrthogonalTiledMapRenderer(map, batch);
		float mWidth = width * tileWidth;
		float mHeight = height * tileHeight;
		float sWidth = Gdx.app.getGraphics().getWidth();
		float sHeight = Gdx.app.getGraphics().getHeight();
		float mapRatio = mWidth / mHeight;
		float screenRatio = sWidth / sHeight;

		float effectiveWidth = 0, effectiveHeight = 0;
		float offsetX = 0, offsetY = 0;
		if (mapRatio > screenRatio) {
			effectiveWidth = mWidth;
			effectiveHeight = mWidth / mapRatio;
			offsetX = mWidth / 2;
			offsetY = mHeight / 2;
		} else {
			effectiveWidth = mHeight / mapRatio;
			effectiveHeight = mHeight;
			offsetX = mWidth / 2;
			offsetY = mHeight / 2;
		}

		camera.setToOrtho(false, effectiveWidth, effectiveHeight);
		camera.position.x = offsetX;
		camera.position.y = offsetY;
		camera.update();

		renderer.setView(camera.projection, 0, 0, width * tileWidth, height * tileHeight);
	}

	public void dispose() {
		map.dispose();
	}

	public void render() {

		renderer.getBatch().end();
		renderer.render();
		renderer.getBatch().begin();
	}
}
