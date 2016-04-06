package no.kash.gamedev.jag.controller.lobby;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.kash.gamedev.jag.commons.utils.Callback;

public class TeamColorPicker extends ColorPicker {

	public TeamColorPicker(float x, float y, Callback cb) {
		super(x, y, 2, 1, cb);
	}

	@Override
	public void generate() {
		colors = new ColorOption[horizontal][vertical];
		for (int i = 0; i < horizontal; i++) {
			for (int j = 0; j < vertical; j++) {
				colors[i][j] = new ColorOption(i == 0 ? new Color(Color.RED) : new Color(Color.BLUE),
						x + i * ColorOption.WIDTH, y + j * ColorOption.HEIGHT);
				colors[i][j].addListener(new ClickListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						ColorOption source = (ColorOption) event.getTarget();
						source.select();
						onSelectCb.callback();
						return true;
					}

				});
			}
		}
	}

}
