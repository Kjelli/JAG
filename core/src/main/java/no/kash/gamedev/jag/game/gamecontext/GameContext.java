package no.kash.gamedev.jag.game.gamecontext;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Queue;

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

		// TODO Optimize in the future
		physics = new BruteForcePhysicsHandler();
	}

	public void update(float delta) {
		if (isPaused()) {
			return;
		}
		ticks++;
		elapsedTime += delta;

		for (GameObject object : objects) {
			object.update(delta * timeModifier);
		}
		while ((add.size > 0)) {
			GameObject n = add.removeFirst();
			n.setGameContext(this);
			newlySpawned.addFirst(n);
			objects.add(0, n);
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

}