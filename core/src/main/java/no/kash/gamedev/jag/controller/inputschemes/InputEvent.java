package no.kash.gamedev.jag.controller.inputschemes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

public class InputEvent {

	private final int id;
	private final Actor src;
	private final Event event;

	public InputEvent(int id, Actor src, Event event) {
		this.src = src;
		this.id = id;
		this.event = event;
	}

	public Actor getSource() {
		return src;
	}

	public int getId() {
		return id;
	}

	public Event getEvent() {
		return event;
	}

}
