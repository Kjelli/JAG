package no.kash.gamedev.jag.game.gameobjects.players.ai;

import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.CollectableItem;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.ItemType;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.grenades.Explosion;
import no.kash.gamedev.jag.game.gameobjects.grenades.Grenade;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public enum Behaviour {
	EASY(0, 0.4f, (float) Math.PI / 4, 2.0f, 0.2f, 0f, 0.1f, 15, 2.0f, 2.0f), HARD(1.5f, 1.0f, 0, 0.5f, 0.8f, 1.0f,
			1.0f, 5, 0.5f, 1.0f);

	public float accuracy, aggressiveness, spread, aimtime, safeFactor, evasiveness, gunHungry, readjustmentTime,
			thinkInterval;
	int bulletsMissedTreshold;

	Behaviour(float accuracy, float aggressiveness, float spread, float aimtime, float safeFactor, float evasiveness,
			float gunHungry, int bulletsMissedTreshold, float readjustmentTime, float thinkInterval) {
		this.accuracy = accuracy;
		this.aggressiveness = aggressiveness;
		this.spread = spread;
		this.aimtime = aimtime;
		this.safeFactor = safeFactor;
		this.evasiveness = evasiveness;
		this.gunHungry = gunHungry;
		this.bulletsMissedTreshold = bulletsMissedTreshold;
		this.readjustmentTime = readjustmentTime;
		this.thinkInterval = thinkInterval;
	}

	public Goal evaluate(Player player, GameObject go) {
		float priority = 0;
		boolean evasive = false;

		// Prioritize weapon
		if (go instanceof Weapon) {
			Weapon weapon = (Weapon) go;

			// DECIDE PRIORITY

			// Better gun
			if (weapon.gun.getTier() > player.getGun().getType().getTier()) {
				priority = 2f * weapon.gun.getTier() + (300 - Math.min(player.distanceTo(weapon), 300))/ 25;
				priority *= gunHungry;
				// Low on ammo
			} else if (player.getGun().getTotalAmmoPercentage() < 0.5f) {
				priority = 3f + (1 - player.getGun().getTotalAmmoPercentage()) * 5f;
				priority *= gunHungry;
			}
		} else if (go instanceof CollectableItem) {
			CollectableItem item = (CollectableItem) go;

			// Prioritize higher when low on health
			if (item.item.getType() == ItemType.healthpack) {
				priority = 3f + (1 - player.getHealthPercentage()) * 30
						+ (player.getStatusHandler().hasNegativeStatuses() ? 50f : 0f);
				priority *= safeFactor;
			}
		} else if (go instanceof Player) {
			Player other = (Player) go;
			if (other.equals(player)
					|| (other.getInfo().teamId == player.getInfo().teamId && player.getInfo().teamId >= 0)) {
				priority = -100f;
			} else if (other.getHealthPercentage() < player.getHealthPercentage()) {
				priority = 10f + (player.getHealthPercentage() - other.getHealthPercentage()) * 30f;
			} else {
				priority = 5f + (600 - Math.min(player.distanceTo(other), 600)) / 55;
			}
			priority *= aggressiveness;

		} else if (go instanceof Grenade || go instanceof Explosion) {
			priority = 10 / (player.getHealthPercentage() + 0.1f) * (100 - player.distanceTo(go)) / 30f;
			priority *= safeFactor + evasiveness;
			evasive = true;
		} else if (go instanceof Bullet) {
			Bullet bullet = (Bullet) go;
			if (bullet.getShooter() == player || (bullet.getShooter().getInfo().teamId == player.getInfo().teamId
					&& player.getInfo().teamId > -1)) {
				priority = -100f;
			} else if (Math.atan2(go.velocity().y, go.velocity().x) - (go.angleTo(player) - Math.PI) < 0.2f) {
				priority = 10 / (player.getHealthPercentage() + 0.1f) * (100 - player.distanceTo(go)) / 50f;
				priority *= safeFactor + evasiveness;
				evasive = true;
			}
		}
		return new Goal(go, priority, evasive);
	}
}
