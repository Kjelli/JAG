package no.kash.gamedev.jag.commons.selection;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Selector<T extends SelectOption> extends Actor {

	private static final float SPACING = 0f;

	float x, y;
	List<T> options;

	public Selector(float x, float y) {
		options = new ArrayList<>();
		this.x = x;
		this.y = y;
	}

	public void add(T t) {
		int newIndex = options.size();
		options.add(t);

		if (newIndex > 0) {
			t.setSize(options.get(newIndex - 1).x + options.get(newIndex - 1).getSprite().getWidth() + SPACING, y,
					t.getSprite().getWidth(), t.getSprite().getHeight());
		} else {
			t.setSize(x, y, t.getSprite().getWidth(), t.getSprite().getHeight());
		}

	}

	public void draw(Batch batch, float parentAlpha) {
		for (T t : options) {
			t.draw(batch, parentAlpha);
		}
	}

	public void add(Stage stage) {
		for (T t : options) {
			stage.addActor(t);
		}
	}

	public void setX(float x) {
		for (T t : options) {
			t.setX(t.getX() - (this.x - x));
		}
	}

}
