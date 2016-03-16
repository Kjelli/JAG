package no.kash.gamedev.jag.controller.inputschemes;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class InputScheme {
	public Map<Integer, Actor> inputElements;

	public InputScheme() {
		inputElements = new HashMap<>();
	}

	public void addInputElement(final int id, final Actor actor) {
		inputElements.put(id, actor);
		actor.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				handleInputEvent(new InputEvent(id, actor, event));
				return true;
			}
		});
	}

	protected abstract void handleInputEvent(InputEvent event);

}
