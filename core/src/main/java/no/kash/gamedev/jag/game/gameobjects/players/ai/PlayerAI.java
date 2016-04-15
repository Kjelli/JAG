package no.kash.gamedev.jag.game.gameobjects.players.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale.FilteringMode;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.collectables.AbstractCollectable;
import no.kash.gamedev.jag.game.gameobjects.collectables.Collectable;
import no.kash.gamedev.jag.game.gameobjects.collectables.items.Item;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.grenades.Grenade;
import no.kash.gamedev.jag.game.gameobjects.grenades.NormalGrenade;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.levels.pathfinding.LevelPathFinder;
import no.kash.gamedev.jag.game.levels.pathfinding.MapNode;
import no.kash.gamedev.jag.game.levels.pathfinding.PathResult;

public class PlayerAI {
	public static final float AI_UPDATE_PATH_RATE = 0.2f;
	private static final float NODE_PROXIMITY_TRESHOLD = 45f;

	public enum State {
		scout, pursue, flee, shoot, wander
	}

	Player thisPlayer;
	Goal goal;
	Polygon ray;
	boolean inSight;

	GameContext gameContext;
	Level level;

	PathResult path;

	private MapNode targetNode;
	private int targetNodeIndex;

	Behaviour behaviour;
	State state = State.scout;

	private float lastUpdatedPath;
	private float throwGrenadeTimestamp;

	private Cooldown thinkCooldown;
	public Cooldown shootCooldown;
	private Cooldown aimingCooldown;

	boolean holdingGrenade = false;

	public int missedBullets = 0;

	public float spreadOffset = 0;

	private TileCollisionListener tileCollisionListener = new TileCollisionListener() {
		@Override
		public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
			if (Integer.parseInt((String) rectangleObject.getProperties().get("collision_level")) >= 2) {
				inSight = false;
			}
		}
	};

	// TODO decide behaviour
	public PlayerAI(Player thisPlayer, GameContext gameContext) {
		behaviour = Behaviour.HARD;
		this.thisPlayer = thisPlayer;
		this.gameContext = gameContext;
		this.level = gameContext.getLevel();
		this.thinkCooldown = new Cooldown(behaviour.thinkInterval);
		this.aimingCooldown = new Cooldown(behaviour.aimtime);
		this.shootCooldown = new Cooldown(behaviour.readjustmentTime);
		ray = new Polygon(new float[] { 0, 0, 50, 0, 50, 1, 0, 1 });
		ray.setOrigin(0, 0);
		ray.setRotation((float) (thisPlayer.getRotation() + Math.PI / 2));
		spreadOffset = (float) Math.min(behaviour.spread,
				Math.max(-behaviour.spread, (Math.random() * 2 * behaviour.spread) - behaviour.spread));
	}

	public void update(float delta) {
		ray.setRotation((float) ((thisPlayer.getRotation() + Math.PI / 2) * 180 / Math.PI));
		ray.setPosition(thisPlayer.getBulletOriginX(), thisPlayer.getBulletOriginY());
		if (thisPlayer.getAliveTime() > throwGrenadeTimestamp) {
			thisPlayer.releaseGrenade();
			holdingGrenade = false;
		}

		if (Math.random() < 0.01f * behaviour.aggressiveness && !holdingGrenade
				&& thisPlayer.getThrowableCooldown().isReady()) {
			if (goal != null && goal.objectTarget) {
				if (thisPlayer.getGun().getType() != GunType.flamethrower && goal.target instanceof Player
						&& thisPlayer.distanceTo(goal.target) < 600) {
					thisPlayer.holdGrenade(Math.min(thisPlayer.distanceTo(goal.target) / 300, 1),
							(float) (this.thisPlayer.getRotation()));
					holdingGrenade = true;
				}
			}
		}

		shootCooldown.update(delta);
		aimingCooldown.update(delta);
		thinkCooldown.update(delta);
		if (thinkCooldown.isReady()) {
			thinkCooldown.start();
			state = State.scout;
		}
		switch (state) {
		case wander:
			log("I am wandering");

			boolean newPath = false;
			Point there = null;
			while (!newPath) {
				Point here = level.getTileCoordinate(thisPlayer);
				there = new Point((int) (Math.random() * level.width), (int) (Math.random() * level.height));
				PathResult r = LevelPathFinder.findPath(here.x, here.y, there.x, there.y);
				newPath = r.success;
			}
			goal = new Goal(there, 100, false);
			state = State.pursue;
			break;
		case scout:
			scout();
			break;
		case pursue:
			pursue();
		case shoot:
			shoot();
			break;
		case flee:
			flee();
			break;
		default:
			break;

		}
	}

	private void shoot() {
		boolean hasTarget = hasTarget();
		if (!hasTarget
				|| (hasTarget && goal.objectTarget && thisPlayer.distanceTo(goal.target) < thisPlayer.getWidth() / 2)
				|| missingShots()) {
			thisPlayer.setFiring(false);
			shootCooldown.start();
			missedBullets = 0;
			state = State.wander;
			return;
		}

		if (targetInSight() && goal != null && goal.target instanceof Player) {
			if (holdingGrenade) {
				thisPlayer.holdGrenade(Math.min(thisPlayer.distanceTo(goal.target) / 300, 1),
						(float) (this.thisPlayer.getRotation()));
				thisPlayer.releaseGrenade();
				holdingGrenade = false;
				log("I can see my target, releasing grenade early");
			}
			path = null;
			aimAtTarget();
			if (thisPlayer.getGun().getType() == GunType.awp) {
				thisPlayer.accelerate(0, 0);
			} else if (thisPlayer.getGun().getType() == GunType.flamethrower) {
				thisPlayer.accelerate((float) Math.cos(thisPlayer.getRotation() + Math.PI / 2),
						(float) Math.sin(thisPlayer.getRotation() + Math.PI / 2));
			} else {
				thisPlayer.accelerate(
						behaviour.evasiveness * behaviour.evasiveness * (float) Math.cos(thisPlayer.getRotation()),
						behaviour.evasiveness * behaviour.evasiveness * (float) Math.sin(thisPlayer.getRotation()));
			}
			if (thisPlayer.getGun().isHoldToShoot()) {
				thisPlayer.setFiring(true);
			} else {
				if (!aimingCooldown.isOnCooldown()) {
					aimingCooldown.start();
					thisPlayer.setFiring(false);
				} else if (!thisPlayer.getGun().isOnCooldown()) {
					thisPlayer.setFiring(true);
				}
			}
		} else if (shootCooldown.isReady()) {
			thisPlayer.setFiring(false);
			state = State.scout;
			return;
		}
	}

	private void flee() {
		if (!hasTarget()) {
			state = State.scout;
			return;
		}

		float angleToDanger;
		if (goal.objectTarget) {
			angleToDanger = thisPlayer.angleTo(goal.target) + spreadOffset;
		} else {
			angleToDanger = thisPlayer.angleTo(goal.targetPoint.x, goal.targetPoint.y) + spreadOffset;
		}

		thisPlayer.accelerate((float) -Math.cos(angleToDanger), (float) -Math.sin(angleToDanger));
		thisPlayer.setRotation((float) (-angleToDanger + Math.PI / 2));

	}

	private void pursue() {

		if (!hasTarget()) {
			state = State.scout;
			return;
		}

		// Determine / update path
		if (path == null
				|| gameContext.getElapsedTime() > lastUpdatedPath + AI_UPDATE_PATH_RATE + behaviour.readjustmentTime) {
			lastUpdatedPath = (float) gameContext.getElapsedTime();
			Point here = level.getTileCoordinate(thisPlayer);
			Point there;
			if (goal.objectTarget) {
				there = level.getTileCoordinate(goal.target);
			} else {
				there = goal.targetPoint;
			}
			path = LevelPathFinder.findPath(here.x, here.y, there.x, there.y);
			targetNodeIndex = 0;
			targetNode = path.path.get(targetNodeIndex);
		} else {
			if (thisPlayer.distanceTo(targetNode.mX, targetNode.mY) < NODE_PROXIMITY_TRESHOLD) {
				targetNodeIndex++;
				if (targetNodeIndex < path.path.getCount()) {
					targetNode = path.path.get(targetNodeIndex);
				}
			}
		}
		if (goal.objectTarget) {
			if (goal.target instanceof Player) {
				if (targetInSight()) {
					state = State.shoot;
					log("Shooting at " + ((Player) goal.target).getInfo().name);
					return;
				}
			}
		}
		aimAtTarget();
		move();
	}

	private void move() {
		float angleToTarget = thisPlayer.angleTo(targetNode.mX, targetNode.mY);
		thisPlayer.accelerate((float) Math.cos(angleToTarget), (float) Math.sin(angleToTarget));
	}

	private void aimAtTarget() {
		if (Math.random() < 0.025f) {
			spreadOffset = (float) Math.min(behaviour.spread,
					Math.max(-behaviour.spread, spreadOffset + (Math.random() - 0.5f) * 0.25f * behaviour.spread));
		}
		float xDist, yDist;
		if (!goal.objectTarget) {
			xDist = thisPlayer.getCenterX() - path.path.get(path.path.getCount() - 1).mX;
			yDist = thisPlayer.getCenterY() - path.path.get(path.path.getCount() - 1).mY;

		} else {
			float hypot = thisPlayer.distanceTo(goal.target);
			// Aim directly towards the player
			xDist = thisPlayer.getCenterX() - goal.target.getCenterX();
			yDist = thisPlayer.getCenterY() - goal.target.getCenterY();

			// Take into account predicted movement (Difficulty factor)
			if (thisPlayer.getGun().getType() != GunType.awp) {
				xDist -= behaviour.accuracy * goal.target.velocity().x * (hypot / 300);
				yDist -= behaviour.accuracy * goal.target.velocity().y * (hypot / 300);
			}
		}
		thisPlayer.setRotation((float) (Math.atan2(yDist, xDist) + Math.PI / 2) + spreadOffset);
	}

	private boolean missingShots() {
		return thisPlayer.getGun().getType() != GunType.flamethrower && missedBullets > behaviour.bulletsMissedTreshold;
	}

	private void log(String string) {
		System.out.println(thisPlayer.getInfo().name + " (AI): " + string);
	}

	private void scout() {
		// Determine most interesting target
		goal = determineGoal();

		// If target is found, enter pursue state
		if (goal != null) {
			if (goal.evasive) {
				state = State.flee;
			} else {
				state = State.pursue;
			}
		} else {
			state = State.wander;
		}
	}

	private Goal determineGoal() {
		List<GameObject> targets = thisPlayer.getGameContext()
				.getByClass(new Class[] { Player.class, Collectable.class, Grenade.class, Bullet.class });

		List<Goal> goals = new ArrayList<>();

		Goal target = null;
		for (GameObject go : targets) {
			Goal goal = behaviour.evaluate(thisPlayer, go);
			if (goal.priority > 0) {
				goals.add(goal);
			}
		}

		Collections.sort(goals);
		if (goals.size() > 0) {
			target = goals.get(0);
		}
		return target;
	}

	private boolean hasTarget() {
		return goal != null && ((goal.objectTarget && goal.target.isAlive()) || (!goal.objectTarget));
	}

	private boolean targetInSight() {
		float width;
		float height = 1;
		if (goal.objectTarget) {
			width = thisPlayer.distanceTo(goal.target);
		} else {
			width = thisPlayer.distanceTo(goal.targetPoint.x, goal.targetPoint.y);
		}
		ray.setVertices(new float[] { 0, 0, width, 0, width, height, 0, height });
		ray.setRotation((float) ((thisPlayer.getRotation() + Math.PI / 2) * 180 / Math.PI));
		inSight = true;
		// This will update inSight to false if not in sight
		TileCollisionDetector.checkTileCollisions(level, ray, tileCollisionListener);
		return inSight;
	}

	public void debugDraw(ShapeRenderer renderer) {
		if (path != null) {
			path.render(renderer);
		}

		renderer.polygon(ray.getTransformedVertices());
	}
}
