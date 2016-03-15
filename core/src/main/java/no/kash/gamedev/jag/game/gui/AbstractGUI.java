package no.kash.gamedev.jag.game.gui;

import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;

public class AbstractGUI extends AbstractGameObject implements GUI {

	public AbstractGUI(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	public void update(float delta) {

	}

}
