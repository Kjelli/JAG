package no.kash.gamedev.jag.controller.inputschemes;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class InputScheme {
	public Map<Integer, Actor> inputElements;

	public InputScheme() {
		inputElements = new HashMap<>();
	}

	public void addInputElement(int id, final Actor actor) {
		addInputElement(id, actor, false);
	}

	public void addInputElement(final int id, final Actor actor, boolean clickListener) {
		inputElements.put(id, actor);
		if (!clickListener) {
			actor.addListener(new EventListener() {

				@Override
				public boolean handle(Event event) {
					handleInputEvent(new InputEvent(id, actor, event));
					return true;
				}
			});
		} else {
			actor.addListener(new ClickListener() {
				@Override
				public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y,
						int pointer, int button) {
					handleInputEvent(new InputEvent(id, actor, event, false));
					return true;
				}

				@Override
				public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer,
						int button) {
					handleInputEvent(new InputEvent(id, actor, event, true));
				}
			});
		}
	}

	protected abstract void handleInputEvent(InputEvent event);

}
