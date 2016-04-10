package no.kash.gamedev.jag.game.levels;

import java.io.File;
import java.io.FileFilter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapHandler {

	public static FileHandle[] availableMaps() {
		File file = new File("../assets/maps");
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".tmx"));
			}
		});
		FileHandle[] maps = new FileHandle[files.length];

		for (int i = 0; i < files.length; i++) {
			maps[i] = new FileHandle(files[i].toString());
		}

		return maps;
	}


}
