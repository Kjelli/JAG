package no.kash.gamedev.jag.game.gameobjects.players.ai;

import java.awt.Point;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.AbstractCollectable;
import no.kash.gamedev.jag.game.gameobjects.collectables.Collectable;
import no.kash.gamedev.jag.game.gameobjects.grenades.NormalGrenade;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.levels.pathfinding.LevelPathFinder;
import no.kash.gamedev.jag.game.levels.pathfinding.MapNode;
import no.kash.gamedev.jag.game.levels.pathfinding.PathResult;

public class PlayerAI {
	public static final float AI_UPDATE_PATH_RATE = 0.2f;
	private static final float NODE_PROXIMITY_TRESHOLD = 45f;
	private static final int MAX_GRENADE = 1;
	private static final float RANGE = 1200;

	public enum State {
		scout, pursue, shoot, wander
	}

	Player thisPlayer;
	GameObject target;
	Polygon ray;
	boolean inSight;

	GameContext gameContext;
	Level level;

	PathResult path;

	private MapNode targetNode;
	private int targetNodeIndex;

	State state = State.scout;
	private float lastUpdatedPath;
	private float throwGrenadeTimestamp;
	boolean holdingGrenade = false;

	private TileCollisionListener tileCollisionListener = new TileCollisionListener() {
		@Override
		public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
			if (Integer.parseInt((String) rectangleObject.getProperties().get("collision_level")) >= 2) {
				inSight = false;
			}
		}
	};

	public PlayerAI(Player thisPlayer, GameContext gameContext) {
		this.thisPlayer = thisPlayer;
		this.gameContext = gameContext;
		this.level = gameContext.getLevel();
		ray = new Polygon(new float[] { 0, 0, 50, 0, 50, 1, 0, 1 });
		ray.setOrigin(0, 0);
		ray.setRotation((float) (thisPlayer.getRotation() + Math.PI / 2));
	}

	public void update(float delta) {
		ray.setRotation((float) ((thisPlayer.getRotation() + Math.PI / 2) * 180 / Math.PI));
		ray.setPosition(thisPlayer.getBulletOriginX(), thisPlayer.getBulletOriginY());
		if (thisPlayer.getAliveTime() > throwGrenadeTimestamp) {
			thisPlayer.releaseGrenade();
			holdingGrenade = false;
		}

		switch (state) {
		case wander:
			break;
		case scout:
			target = null;
			Player closestTarget = null;
			float minDistance = RANGE;
			for (GameObject o : thisPlayer.getGameContext().getByClass(new Class[] { Player.class })) {
				Player that = (Player) o;
				if (that.equals(thisPlayer)) {
					continue;
				}
				float distanceToThat = that.distanceTo(thisPlayer);
				if (distanceToThat < minDistance && viableTarget(that)) {
					minDistance = distanceToThat;
					closestTarget = that;
				}
			}
			if (closestTarget == null) {
				state = State.wander;
			} else {
				target = closestTarget;
				state = State.pursue;

			}

			break;
		case pursue:
			if (target == null || !target.isAlive()) {
				state = State.scout;
				break;
			}

			if (target instanceof Collectable) {
				if (((Collectable) target).isCollected()) {
					state = State.scout;
					break;
				}
			}

			if (target instanceof Player && playerInSight()) {
				state = State.shoot;
				break;
			}

			if (target instanceof Player && thisPlayer.distanceTo(target) > 300 && !holdingGrenade
					&& !thisPlayer.getThrowableCooldown().isOnCooldown()) {
				thisPlayer.holdGrenade(Math.min(thisPlayer.distanceTo(target) / MAX_GRENADE, MAX_GRENADE),
						(float) (this.thisPlayer.getRotation() + Math.PI / 2));
				throwGrenadeTimestamp = thisPlayer.getAliveTime() + 1.0f;
				holdingGrenade = true;
			}

			thisPlayer.setRotation((float) (thisPlayer.angleTo(target) - Math.PI / 2));

			if (path == null || thisPlayer.getAliveTime() - lastUpdatedPath > AI_UPDATE_PATH_RATE) {
				lastUpdatedPath = thisPlayer.getAliveTime();
				Point start = level.getTileCoordinate(thisPlayer);
				Point end = level.getTileCoordinate(target);
				path = LevelPathFinder.findPath(start.x, start.y, end.x, end.y);
				targetNode = path.path.get(0);
				targetNodeIndex = 0;
			}

			if (thisPlayer.distanceTo(targetNode.mX, targetNode.mY) < NODE_PROXIMITY_TRESHOLD) {
				targetNodeIndex++;
				if (targetNodeIndex < path.path.nodes.size) {
					targetNode = path.path.get(targetNodeIndex);
				}
			} else {
				float angleToDestination = thisPlayer.angleTo(targetNode.mX, targetNode.mY);
				thisPlayer.accelerate((float) Math.cos(angleToDestination), (float) Math.sin(angleToDestination));
			}
			break;
		case shoot:
			if (target == null || !target.isAlive()) {
				state = State.scout;
				break;
			}
			if (playerInSight()) {
				if (holdingGrenade) {
					thisPlayer.holdGrenade(Math.min(thisPlayer.distanceTo(target) / 300, MAX_GRENADE),
							(float) (this.thisPlayer.getRotation() + Math.PI / 2));
					thisPlayer.releaseGrenade();
					holdingGrenade = false;
				}
				path = null;
				thisPlayer.setRotation((float) (thisPlayer.angleTo(target) - Math.PI / 2));
				thisPlayer.accelerate(0, 0);
				if (thisPlayer.getGun().isHoldToShoot()) {
					thisPlayer.setFiring(true);
				} else {
					thisPlayer.setFiring(!thisPlayer.isFiring());
				}
			} else {
				thisPlayer.setFiring(false);
				state = State.scout;
				break;
			}
			break;
		default:
			break;

		}
	}

	private boolean viableTarget(Player that) {
		if (that.getInfo().teamId == thisPlayer.getInfo().teamId && thisPlayer.getInfo().teamId >= 0) {
			return false;
		}
		return true;
	}

	private boolean playerInSight() {
		float width = thisPlayer.distanceTo(target);
		float height = 1;
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
