package no.kash.gamedev.jag.game.levels;

import com.badlogic.gdx.maps.MapObjects;

import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;

public class Spawner {
	
	public float spawnPoints[][];
	public Weapon wep;
	
	
	public Spawner(float[][] spawnPoints, GameContext gameContext){
		
		Weapon wep = new Weapon(spawnPoints[0][0],spawnPoints[0][1], GunType.m4);
		gameContext.spawn(wep);
	}
}
