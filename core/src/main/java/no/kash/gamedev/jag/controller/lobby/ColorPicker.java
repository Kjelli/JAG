package no.kash.gamedev.jag.controller.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.kash.gamedev.jag.assets.Assets;

public class ColorPicker {

	ColorOption[][] colors;

	public ColorPicker(float x, float y, int width, int height) {
		colors = new ColorOption[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				colors[i][j] = new ColorOption(
						new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1),
						x + i * ColorOption.WIDTH, y + j * ColorOption.HEIGHT);
			}
		}
	}

	public void add(Stage stage) {
		for (ColorOption[] optionRow : colors) {
			for (ColorOption color : optionRow) {
				stage.addActor(color);
			}
		}
	}

	public static class ColorOption extends Actor {
		public static final int HEIGHT = 64;
		public static final int WIDTH = 64;
		Sprite sprite;

		public ColorOption(Color color, float x, float y) {
			sprite = new Sprite(Assets.card_bg);
			sprite.setColor(color);
			sprite.setX(x);
			sprite.setY(y);
			setX(x);
			setY(y);
		}

		@Override
		public Color getColor() {
			return sprite.getColor();
		}

		@Override
		public void setColor(Color color) {
			sprite.setColor(color);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			sprite.draw(batch);
			batch.draw(Assets.card_border, getX(), getY());
		}
	}
}
