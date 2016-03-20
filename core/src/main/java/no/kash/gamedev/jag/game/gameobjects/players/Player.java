package no.kash.gamedev.jag.game.gameobjects.players;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gamecontext.functions.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.gamecontext.physics.tilecollisions.TileCollisionListener;
import no.kash.gamedev.jag.game.gameobjects.AbstractGameObject;
import no.kash.gamedev.jag.game.gameobjects.bullets.Bullet;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.grenades.Grenade;
import no.kash.gamedev.jag.game.gameobjects.particles.BloodSplatter;
import no.kash.gamedev.jag.game.gameobjects.particles.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.damagehandlers.DamageHandler;
import no.kash.gamedev.jag.game.gameobjects.players.damagehandlers.VanillaDamageHandler;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.hud.HealthHud;
import no.kash.gamedev.jag.game.gamesettings.GameSettings;
import no.kash.gamedev.jag.game.screens.GameScreen;

public class Player extends AbstractGameObject implements Collidable {

	public static final float MAX_SPEED = 150;
	public static final float ACCELERATION = 3200;
	public static final float WIDTH = 64, HEIGHT = 64;

	private final TileCollisionListener tileCollisionListener;

	private final PlayerInfo info;

	private boolean firing;

	private Gun gun;
	private Hitbox hitbox;
	private HealthHud healthHud;

	private float grenadePower;
	private float grenadeDirection;
	private float healthMax;
	private float health;
	private Cooldown grenadeCooldown;
	private float grenadeCooldownDuration = 3;

	private GlyphLayout nameLabel;

	private DamageHandler damageHandler;
	private boolean holdingGrenade;

	private Sprite holding_grenade;
	
	private GameScreen gamescreen;
	
	public Player(GameSettings gameSettings, int id, float x, float y, GameScreen gameScreen) {
		super(x, y, WIDTH, HEIGHT);
		this.gamescreen = gameScreen;
		init(gameSettings);

		this.info = gameSettings.players.get(id);
		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, Rectangle intersection) {
				TileCollisionDetector.nudge(Player.this, intersection);
			}
		};
	}

	private void init(GameSettings gameSettings) {
		this.healthMax = gameSettings.startingHealth;
		this.health = healthMax;
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

		hitbox = new Hitbox(getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8, 16, 16);

		// TODO choose damagehandler according to game mode
		setDamageHandler(new VanillaDamageHandler(this));

		nameLabel = new GlyphLayout(Assets.font, info.name);
		healthHud = new HealthHud(this, getCenterX() - HealthHud.WIDTH / 2, getCenterY() - HealthHud.HEIGHT / 2 - 20f);

		equipGun(GunType.pistol);
		grenadeCooldown = new Cooldown(grenadeCooldownDuration);

		getGameContext().bringToFront(this);
	}

	@Override
	public void update(float delta) {
		gun.update(delta);
		grenadeCooldown.update(delta);
		move(delta);
		hitbox.update(getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8);
		healthHud.setX(getCenterX() - HealthHud.WIDTH / 2);
		healthHud.setY(getCenterY() - HealthHud.HEIGHT / 2 - 20f);

		if (isFiring()) {
			gun.shoot();
		}

		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileCollisionListener);
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		if (!gun.isReloading() && !holdingGrenade) {
			gun.draw(batch);
		} else if (holdingGrenade) {
			Draw.sprite(batch, holding_grenade, getX(), getY(), getWidth(), getHeight(), getRotation());
		}

		Assets.font.draw(batch, nameLabel, getX() + getWidth() / 2 - nameLabel.width / 2,
				getY() + getHeight() + nameLabel.height);

		healthHud.draw(batch);
	}

	public void accelerate(float xacc, float yacc) {
		acceleration.x = xacc * max_acceleration.x;
		acceleration.y = yacc * max_acceleration.y;
	}

	public void equipWeapon(Weapon weapon) {
		equipGun(weapon.gun);
	}

	public void equipGun(GunType type) {
		gun = new Gun(type);
		gun.equip(this);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id, new PlayerUpdate(2,
				new int[] { PlayerUpdate.GUN, PlayerUpdate.AMMO },
				new float[][] { { type.ordinal() }, { gun.getMagasineAmmo(), gun.getMagasineSize(), gun.getAmmo() } }));
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
				- Bullet.WIDTH / 2);
	}

	public float getBulletOriginY() {
		return (float) (getCenterY() + Math.sin((rot) + (Math.PI / 2) + gun.getAngleOffset()) * getHeight() / 2
				- Bullet.HEIGHT / 2);
	}

	@Override
	public Rectangle getBounds() {
		return hitbox.rect;
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public boolean isFiring() {
		return firing;
	}

	public void holdGrenade(float power, float dir) {
		if (!grenadeCooldown.isOnCooldown() && !isFiring()) {
			this.grenadeDirection = dir;
			this.grenadePower = power;
			setRotation((float) (dir - Math.PI / 2));
			holdingGrenade = true;
		}
	}

	public void setHealth(float health) {
		this.health = health;
		if (health <= 0) {
			onDeath();
		}
		healthHud.display();

	}

	private void onDeath() {
		// TODO make sense
		for (int i = 0; i < 200; i++) {
			getGameContext()
					.spawn(new BloodSplatter(getCenterX(), getCenterY(), (float) (Math.random() * 2 * Math.PI)));
		}
		/*
		setHealth(healthMax);
		List<Vector2> respawnPoints = getGameContext().getLevel().playerSpawns;
		Vector2 random = respawnPoints.get((int) (respawnPoints.size() * Math.random()));
		setX(random.x);
		setY(random.y);
		 * 
		 */
		destroy();
		gamescreen.roundHandler.checkWinCondition();
	}

	public float getHealth() {
		return health;
	}

	public float getHealthPercentage() {
		return health / healthMax;
	}

	public void releaseGrenade() {
		if (!grenadeCooldown.isOnCooldown() && holdingGrenade) {
			getGameContext().spawn(new Grenade(this, getCenterX(), getCenterY(), grenadeDirection, grenadePower));
			grenadeCooldown.startCooldown();

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
		damageHandler.onDamage(bullet);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
				new PlayerUpdate(2, new int[] { PlayerUpdate.HEALTH, PlayerUpdate.FEEDBACK_VIBRATION },
						new float[][] { { health }, { 100.0f } }));
	}

	public void damage(Explosion explosion) {
		damageHandler.onDamage(explosion);
		System.out.println("Sending health update to " + info.id + " , " + health);
		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
				new PlayerUpdate(2, new int[] { PlayerUpdate.HEALTH, PlayerUpdate.FEEDBACK_VIBRATION },
						new float[][] { { health }, { 400.0f } }));
	}

}
