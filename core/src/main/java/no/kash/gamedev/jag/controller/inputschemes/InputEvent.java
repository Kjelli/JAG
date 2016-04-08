package no.kash.gamedev.jag.controller.inputschemes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

public class InputEvent {

	private final int id;
	private final Actor src;
	private final Event event;
	private final boolean released;

	public InputEvent(int id, Actor src, Event event) {
		this.src = src;
		this.id = id;
		this.event = event;
		this.released = false;
	}
	
	public InputEvent(int id, Actor src, Event event, boolean released) {
		this.src = src;
		this.id = id;
		this.event = event;
		this.released = released;
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
	
	public boolean isReleased() {
		return released;
	}

}
