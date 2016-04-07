package no.kash.gamedev.jag.game.gamecontext;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Queue;

import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.game.announcer.Announcer;
import no.kash.gamedev.jag.game.gamecontext.physics.BruteForcePhysicsHandler;
import no.kash.gamedev.jag.game.gamecontext.physics.PhysicsHandler;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.levels.Level;

public class GameContext {

	private final Game game;

	private final List<GameObject> objects;
	private final Queue<GameObject> add;
	private final Queue<GameObject> remove;
	private final Queue<GameObject> newlySpawned;
	private final Queue<GameObject> newlyDespawned;

	private Stage stage;
	private PhysicsHandler physics;
	private Announcer announcer;
	private long ticks = 0;
	private double elapsedTime = 0;

	private Level level;

	private float timeModifier = 1.0f;

	private boolean paused = false;

	public GameContext(Game game) {
		this.game = game;

		objects = new ArrayList<>();
		add = new Queue<>();
		remove = new Queue<>();
		newlySpawned = new Queue<>();
		newlyDespawned = new Queue<>();

		stage = new Stage();
		announcer = new Announcer(stage.getWidth() / 2, stage.getHeight());

		// TODO Optimize in the future
		physics = new BruteForcePhysicsHandler();
	}

	public void update(float delta) {
		if (isPaused()) {
			return;
		}
		ticks++;
		elapsedTime += delta * timeModifier;
		announcer.update(delta * timeModifier);

		TweenGlobal.update(delta * timeModifier);

		for (GameObject object : objects) {
			object.update(delta * timeModifier);
			object.updateAliveTime(delta * timeModifier);
		}
		while ((add.size > 0)) {
			GameObject n = add.removeFirst();
			n.setGameContext(this);
			newlySpawned.addFirst(n);
			objects.add(n);
		}
		while ((remove.size > 0)) {
			GameObject o = remove.removeFirst();
			newlyDespawned.addFirst(o);
			objects.remove(o);
		}

		while ((newlySpawned.size > 0)) {
			GameObject o = newlySpawned.removeFirst();
			if (o != null) {
				o.onSpawn();
			}
		}

		while ((newlyDespawned.size > 0)) {
			GameObject o = newlyDespawned.removeFirst();
			if (o != null) {
				o.onDespawn();
			}
		}

		physics.collisonCheck(objects);
	}

	public final List<GameObject> getObjects() {
		return objects;
	}

	public final List<GameObject> getByClass(Class<? extends GameObject>[] classes) {
		List<GameObject> returnVals = new ArrayList<>();
		obj: for (GameObject object : objects) {
			for (int i = 0; i < classes.length; i++) {
				if (object.getClass().equals(classes[i])) {
					returnVals.add(object);
					continue obj;
				}
			}
		}
		return returnVals;

	}

	public void spawn(GameObject object) {
		add.addFirst(object);
	}

	public void despawn(GameObject object) {
		remove.addFirst(object);
	}

	public void draw(SpriteBatch batch) {
		for (GameObject object : objects) {
			object.draw(batch);
		}
	}

	public void dispose() {
		for (GameObject object : objects) {
			object.dispose();
		}
	}

	public void bringToFront(GameObject go) {
		int index = objects.indexOf(go);
		objects.remove(index);
		objects.add(go);
	}

	public void bringToBack(GameObject go) {
		int index = objects.indexOf(go);
		objects.remove(index);
		objects.add(0, go);
	}

	public long getTicks() {
		return ticks;
	}

	public Stage getStage() {
		return stage;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public Game getGame() {
		return game;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	public void setTimeModifier(float timeModifier) {
		this.timeModifier = timeModifier;
	}

	public void clear() {
		add.clear();
		remove.clear();
		objects.clear();
	}

	public Announcer getAnnouncer() {
		return announcer;
	}

	public float getTimeModifier() {
		return timeModifier;
	}

}