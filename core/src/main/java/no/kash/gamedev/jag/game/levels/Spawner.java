package no.kash.gamedev.jag.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamecontext.functions.Cooldown;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class Spawner {

	private GameContext gameContext;
	private ArrayList<Vector2> wepSpawns;

	private Cooldown timerInterval;
	private float interval = 5;

	public Spawner(ArrayList<Vector2> wepSpawns, GameContext gameContext) {
		this.gameContext = gameContext;
		this.wepSpawns = wepSpawns;
		timerInterval = new Cooldown(interval);
	}

	public void spawnWeapon(float x, float y, GunType type) {
		Weapon temp = new Weapon(x, y, type);
		gameContext.spawn(temp);

	}

	public void update(float delta) {
		timerInterval.update(delta);
		if (!timerInterval.isOnCooldown()) {
			for (int i = 0; i < wepSpawns.size(); i++) {
				int randomNum = 1 + (int) (Math.random() * 4);
				if (randomNum == 1) {
					spawnWeapon(wepSpawns.get(i).x, wepSpawns.get(i).y, GunType.m4);
				}
			}
			timerInterval.startCooldown();
		}
	}

}
