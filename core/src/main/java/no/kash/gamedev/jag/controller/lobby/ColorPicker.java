package no.kash.gamedev.jag.controller.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.utils.Callback;

public class ColorPicker {

	ColorOption[][] colors;
	float x, y;
	int width, height;
	Stage stage;

	ColorOption selected;
	Callback onSelectCb;

	public ColorPicker(float x, float y, int width, int height, Callback cb) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		generate();
		onSelectCb = cb;

	}

	public Color getSelectedColor(Color defaultColor) {
		if (selected == null) {
			return defaultColor;
		} else {
			return selected.getColor();
		}
	}

	public void generate() {
		colors = new ColorOption[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				colors[i][j] = new ColorOption(
						new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1),
						x + i * ColorOption.WIDTH, y + j * ColorOption.HEIGHT);
				colors[i][j].addListener(new ClickListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						ColorOption source = (ColorOption) event.getTarget();
						if (selected != null) {
							selected.deselect();
						}
						source.select();
						onSelectCb.callback();
						return true;
					}

				});
			}
		}
	}

	public void shuffle() {
		for (ColorOption[] optionRow : colors) {
			for (ColorOption color : optionRow) {
				if(color == selected){
					continue;
				}
				color.randomize();
			}
		}
	}

	public void add(Stage stage) {
		this.stage = stage;

		for (ColorOption[] optionRow : colors) {
			for (ColorOption color : optionRow) {
				stage.addActor(color);
			}
		}
	}
	
	public void setInitialSelection(Color color) {
		colors[0][0].sprite.setColor(color);
		colors[0][0].select();
	}

	public class ColorOption extends TextButton {
		public static final int HEIGHT = 64;
		public static final int WIDTH = 64;
		Sprite sprite;

		boolean selected = false;

		public ColorOption(Color color, float x, float y) {
			super("", new Skin(Gdx.files.internal("uiskin.json")));
			sprite = new Sprite(Assets.card_square_bg);
			sprite.setColor(color);
			sprite.setX(x);
			sprite.setY(y);
			setX(x);
			setY(y);
			setWidth(WIDTH);
			setHeight(HEIGHT);
		}

		public void select() {
			ColorPicker.this.selected = this;
			selected = true;
		}

		public void deselect() {
			ColorPicker.this.selected = null;
			selected = false;
		}

		public void randomize() {
			sprite.setColor((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
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
			if (selected) {
				batch.draw(Assets.card_square_border_selected, getX(), getY());
			} else {
				batch.draw(Assets.card_square_border, getX(), getY());
			}
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return ColorOption.WIDTH * colors.length;
	}

	public float getHeight() {
		return ColorOption.HEIGHT * colors[0].length;
	}

}
