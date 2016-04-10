package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.grenades.Explosion;
import no.kash.gamedev.jag.game.gameobjects.grenades.NormalGrenade;
import no.kash.gamedev.jag.game.gameobjects.particles.BloodSplatter;
import no.kash.gamedev.jag.game.gameobjects.particles.Star;
import no.kash.gamedev.jag.game.gameobjects.players.damagehandlers.DamageHandler;
import no.kash.gamedev.jag.game.gameobjects.players.damagehandlers.VanillaDamageHandler;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.hud.HealthHud;
import no.kash.gamedev.jag.game.gameobjects.players.item.Item;
import no.kash.gamedev.jag.game.gameobjects.players.item.ItemType;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;
import no.kash.gamedev.jag.game.gameobjects.players.status.StatusHandler;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.screens.LobbyScreen;

public class Player extends AbstractGameObject implements Collidable {

	public static final float MAX_SPEED = 150;
	public static final float ACCELERATION = 3200;
	public static final float WIDTH = 64, HEIGHT = 64;
	public static final float HITBOX_WIDTH = 14, HITBOX_HEIGHT = 14;

	private final TileCollisionListener tileCollisionListener;

	private final PlayerInfo info;

	public PlayerInfo getInfo() {
		return info;
	}

	private Cooldown exitTimer;
	private boolean isExiting;

	private boolean firing;
	private boolean holdingGrenade;
	private boolean aiming;

	private boolean blockInput = false;
	private boolean invincible = false;
	private boolean drawNames = true;

	private Item throwable;
	private Gun gun;
	private CircularHitbox hitbox;
	private HealthHud healthHud;

	private float grenadePower;
	private float grenadeDirection;
	private float healthMax;
	private float health;
	private Cooldown throwableCooldown;
	private float grenadeCooldownDuration = 3;

	private GlyphLayout nameLabel;

	private DamageHandler damageHandler;
	private StatusHandler statusHandler;

	private Sprite holding_grenade;

	public LaserSight laserSight;

	private GameSession gameSession;

	private boolean reloading;

	public Player(GameSession gameSession, PlayerInfo info, float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		init(gameSession);

		this.info = info;
		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, MinimumTranslationVector col) {
				TileCollisionDetector.nudge(Player.this, col);
			}
		};
	}

	private void init(GameSession gameSession) {
		this.drawNames = gameSession.settings.getSelectedValue(Defs.SESSION_DRAW_NAMES, Boolean.class);

		this.gameSession = gameSession;
		this.healthMax = gameSession.settings.getSelectedValue(Defs.SESSION_START_HP, Integer.class);
		this.health = healthMax;
		this.exitTimer = new Cooldown(3.0f);
		this.statusHandler = new StatusHandler(this);

		switch (gameSession.settings.getSelectedValue(Defs.SESSION_GM, GameMode.class)) {
		case STANDARD_FFA:
		case STANDARD_TEAM:
			setDamageHandler(new VanillaDamageHandler(this));
		default:
			break;
		}

	}

	@Override
	public void onSpawn() {
		// Set sprite
		Sprite sprite = new Sprite(Assets.man);
		sprite.setOrigin(getWidth() / 2, getHeight() / 2);
		sprite.setColor(info.color);
		setSprite(sprite);

		holding_grenade = new Sprite(Assets.man_holding_grenade);
		holding_grenade.setOrigin(getWidth() / 2, getHeight() / 2);
		maxAcceleration().x = ACCELERATION;
		maxAcceleration().y = ACCELERATION;
		setMaxSpeed(MAX_SPEED);

		hitbox = new CircularHitbox(getX() + getWidth() / 2 - HITBOX_WIDTH / 2,
				getY() + getHeight() / 2 - HITBOX_HEIGHT / 2, HITBOX_WIDTH, HITBOX_HEIGHT);

		nameLabel = new GlyphLayout(Assets.font, info.name);
		healthHud = new HealthHud(this, getCenterX() - HealthHud.WIDTH / 2, getCenterY() - HealthHud.HEIGHT / 2 - 20f);
		getGameContext().spawn(healthHud);
		GunType startingGun = gameSession.settings.getSelectedValue(Defs.SESSION_STARTING_GUN, GunType.class);
		equipGun(startingGun);
		ItemType startingItem = gameSession.settings.getSelectedValue(Defs.SESSION_STARTING_ITEM, ItemType.class);
		equipItem(startingItem);
		System.out.println("Equipped: " + startingItem);
		throwableCooldown = new Cooldown(grenadeCooldownDuration);

		getGameContext().bringToFront(this);

	}

	boolean firstFrame = true;

	@Override
	public void update(float delta) {
		if (firstFrame) {
			((JustAnotherGame) getGameContext().getGame()).getServer()
					.send(info.id,
							new PlayerUpdate(3, new int[] { PlayerUpdate.GUN, PlayerUpdate.AMMO, PlayerUpdate.HEALTH },
									new float[][] { { gun.getType().ordinal() },
											{ gun.getMagasineAmmo(), gun.getMagasineSize(), gun.getAmmo() },
											{ health } }));
			firstFrame = false;
		}
		gun.update(delta);
		statusHandler.update(delta);
		throwableCooldown.update(delta);

		if (gun.getType() == GunType.awp && aiming && !gun.isReloading()) {
			if (laserSight == null || laserSight.disposed) {
				laserSight = new LaserSight(this, getCenterX(), getCenterX(), getRotation());
			}
			laserSight.update(delta);
		}

		if (blockInput) {
			accelerate(0, 0);
		}
		move(delta);
		hitbox.update(getX() + getWidth() / 2 - HITBOX_WIDTH / 2, getY() + getHeight() / 2 - HITBOX_HEIGHT / 2,
				(float) (rot * 180 / Math.PI));
		healthHud.setX(getCenterX() - HealthHud.WIDTH / 2);
		healthHud.setY(getCenterY() - HealthHud.HEIGHT / 2 - 20f);

		if (isInvincible()) {
			spawnStars();
		}

		gunLogic(delta);

		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileCollisionListener);
	}

	private void gunLogic(float delta) {

		if (isReloading()) {
			reload();
			if (info.gameMaster) {
				System.out.println("EXITING: " + exitTimer.getCooldownTimer());
				exitTimer.update(delta);
				if (exitTimer.getCooldownTimer() < 1.0f && isExiting == false) {
					getGameContext().getAnnouncer().announce("Exiting...");
					isExiting = true;
				}
				if (!exitTimer.isOnCooldown()) {
					getGameContext().getGame()
							.setScreen(new LobbyScreen((JustAnotherGame) getGameContext().getGame(), gameSession));
					((JustAnotherGame) getGameContext().getGame()).getServer()
							.broadcast(new PlayerStateChange(JustAnotherGameController.LOBBY_STATE));
					return;
				}
			}
		} else {
			// Reset cooldown
			exitTimer.startCooldown();
			isExiting = false;
		}

		if (!isFiring() && aiming) {
			fireBullet();
			if (laserSight != null && laserSight.disposed) {
				laserSight = null;
			}
			aiming = false;
			gun.checkOutOfAmmo();

		}
		if (isFiring()) {
			if (gun.isHoldToShoot()) {
				fireBullet();
			} else if (!gun.isReloading() && !gun.isOnCooldown()) {
				aiming = true;
			}
		}
	}

	private void spawnStars() {
		if (Math.random() < 0.2f) {
			getGameContext().spawn(new Star(
					getCenterX() + (float) ((Math.random() - 0.5f) * hitbox.poly.getBoundingRectangle().width * 2),
					getCenterY() + (float) ((Math.random() - 0.5f) * hitbox.poly.getBoundingRectangle().height * 2), 0,
					-50.0f));
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if (!gun.isReloading() && !holdingGrenade) {
			gun.draw(batch);
		} else if (holdingGrenade) {
			Draw.sprite(batch, holding_grenade, getX(), getY(), getWidth(), getHeight(), getRotation());
		}

		if (laserSight != null && gun.getType() == GunType.awp && aiming)
			laserSight.draw(batch);

		if (drawNames) {
			Assets.font.draw(batch, nameLabel, getX() + getWidth() / 2 - nameLabel.width / 2,
					getY() + getHeight() + nameLabel.height);
		}
	}

	@Override
	public void debugDraw(ShapeRenderer renderer) {
		super.debugDraw(renderer);
		if (laserSight != null) {
			renderer.polygon(laserSight.getBounds().getTransformedVertices());
		}
	}

	public void accelerate(float xacc, float yacc) {
		acceleration.x = xacc * max_acceleration.x;
		acceleration.y = yacc * max_acceleration.y;
	}

	public void equipWeapon(Weapon weapon) {
		int newAmmo = weapon.ammo;
		int newMag = weapon.mag;
		if (gun.getType() == weapon.gun) {
			newAmmo += gun.getAmmo() + weapon.mag;
			gun.setAmmo(newAmmo);
			((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
					new PlayerUpdate(1, new int[] { PlayerUpdate.AMMO },
							new float[][] { { gun.getMagasineAmmo(), gun.getMagasineSize(), gun.getAmmo() } }));
		} else {
			equipGun(weapon.gun, newAmmo, weapon.mag);
		}
	}

	public void equipGun(GunType type) {
		equipGun(type, type.getMaxAmmo(), type.getMagazineSize());
	}

	
	public void equipGun(GunType type, int ammo, int mag) {
		gun = new Gun(type);
		gun.equip(this);
		gun.setAmmo(ammo);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id, new PlayerUpdate(2,
				new int[] { PlayerUpdate.GUN, PlayerUpdate.AMMO },
				new float[][] { { type.ordinal() }, { gun.getMagasineAmmo(), gun.getMagasineSize(), gun.getAmmo() } }));

	}
	
	private void equipItem(ItemType itemType) {
		throwable = new Item(itemType);
	}


	public int getId() {
		return info.id;
	}

	public String getName() {
		return info.name;
	}

	public void fireBullet() {
		if (!isHoldingGrenade()) {
			gun.shoot();
			if (gun.getMagasineAmmo() < 1) {
				reload();
			}
		}
	}

	private boolean isHoldingGrenade() {
		return holdingGrenade;
	}

	public void reload() {
		gun.reload();
	}

	public float getBulletOriginX() {
		return (float) (getCenterX() + Math.cos((rot) + (Math.PI / 2) + gun.getAngleOffset()) * getWidth() / 2
				- gun.getBulletWidth() / 2);
	}

	public float getBulletOriginY() {
		return (float) (getCenterY() + Math.sin((rot) + (Math.PI / 2) + gun.getAngleOffset()) * getHeight() / 2
				- gun.getBulletHeight() / 2);
	}

	@Override
	public Polygon getBounds() {
		return hitbox.poly;
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public boolean isFiring() {
		return firing;
	}

	public void holdGrenade(float power, float dir) {
		if (!throwableCooldown.isOnCooldown() && !isFiring()) {
			this.grenadeDirection = dir;
			this.grenadePower = power;
			setRotation((float) (dir - Math.PI / 2));
			holdingGrenade = true;
		}
	}

	public void setHealth(float health) {
		this.health = health;
		healthHud.display();
	}

	public void death(Player killer) {
		if (isAlive()) {
			for (int i = 0; i < 200; i++) {
				getGameContext().spawn(
						new BloodSplatter(getCenterX(), getCenterY(), (float) (Math.random() * 2 * Math.PI), 10.0f));
			}

			gameSession.roundHandler.playerKilled(killer, this);
		}
		onDeath();
	}

	public float getHealth() {
		return health;
	}

	public float getHealthPercentage() {
		return health / healthMax;
	}

	public void releaseGrenade() {
		if (!throwableCooldown.isOnCooldown() && holdingGrenade) {
			switch (throwable.getType()) {
			case grenade:
				getGameContext()
						.spawn(new NormalGrenade(this, getCenterX(), getCenterY(), grenadeDirection, grenadePower));
				break;
			default:
				break;
			}
			throwableCooldown.startCooldown();

			holdingGrenade = false;
		}
	}

	public void setDamageHandler(DamageHandler handler) {
		this.damageHandler = handler;
	}

	public DamageHandler getDamageHandler() {
		return damageHandler;
	}

	// Leave empty for now
	@Override
	public void onCollide(Collision collision) {
	}

	public void damage(Bullet bullet) {
		if (isInvincible()) {
			return;
		}
		for (int i = 0; i < Math.min(bullet.getDamage(), 250); i++) {
			getGameContext().spawn(new BloodSplatter(getCenterX(), getCenterY(),
					(float) (bullet.getDirection() + (Math.random() * 0.5f - 0.25f)),
					Math.min(bullet.getDamage(), 100f)));
		}
		damageHandler.onDamage(bullet);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
				new PlayerUpdate(2, new int[] { PlayerUpdate.HEALTH, PlayerUpdate.FEEDBACK_VIBRATION },
						new float[][] { { health }, { 100.0f } }));
	}

	public void damage(Explosion explosion) {
		if (isInvincible()) {
			return;
		}
		damageHandler.onDamage(explosion);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
				new PlayerUpdate(2, new int[] { PlayerUpdate.HEALTH, PlayerUpdate.FEEDBACK_VIBRATION },
						new float[][] { { health }, { 400.0f } }));
	}

	public void damage(Status status) {
		damageHandler.onDamage(status);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
				new PlayerUpdate(2, new int[] { PlayerUpdate.HEALTH, PlayerUpdate.FEEDBACK_VIBRATION },
						new float[][] { { health }, { 30.0f } }));
	}

	@Override
	public String toString() {
		return getName();
	}

	public GameSession getGameSession() {
		return gameSession;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void blockInput(boolean block) {
		this.blockInput = block;
	}

	public boolean isInputBlocked() {
		return blockInput;
	}

	public void death(Status status) {
		if (isAlive()) {
			for (int i = 0; i < 200; i++) {
				getGameContext().spawn(
						new BloodSplatter(getCenterX(), getCenterY(), (float) (Math.random() * 2 * Math.PI), 10.0f));
			}

			gameSession.roundHandler.playerKilled(this, status);
		}

		onDeath();
	}

	private void onDeath() {
		if (gun.getType() == GunType.pistol) {
			return;
		}
		getGameContext()
				.spawn(new Weapon(getCenterX(), getCenterY(), gun.getType(), gun.getAmmo(), gun.getMagasineAmmo()));
	}

	public void applyStatus(Status status) {
		statusHandler.apply(status);
	}

	public boolean isReloading() {
		return reloading;
	}

	public void setReloading(boolean reloading) {
		this.reloading = reloading;
	}

	@Override
	public void destroy() {
		super.destroy();
		healthHud.destroy();
	}

}
