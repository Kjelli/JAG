package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.graphics.Draw;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
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
import no.kash.gamedev.jag.game.gameobjects.particles.Star;
import no.kash.gamedev.jag.game.gameobjects.players.damagehandlers.DamageHandler;
import no.kash.gamedev.jag.game.gameobjects.players.damagehandlers.VanillaDamageHandler;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.hud.HealthHud;
import no.kash.gamedev.jag.game.gamesession.GameSession;

public class Player extends AbstractGameObject implements Collidable {

	public static final float MAX_SPEED = 150;
	public static final float ACCELERATION = 3200;
	public static final float WIDTH = 64, HEIGHT = 64;

	private final TileCollisionListener tileCollisionListener;

	private final PlayerInfo info;

	public PlayerInfo getInfo() {
		return info;
	}

	private boolean firing;
	private boolean holdingGrenade;
	private boolean blockInput = false;
	private boolean invincible = false;

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

	private Sprite holding_grenade;

	private GameSession gameSession;

	public Player(GameSession gameSession, PlayerInfo info, float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		init(gameSession);

		this.info = info;
		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, Rectangle intersection) {
				TileCollisionDetector.nudge(Player.this, intersection);
			}
		};
	}

	private void init(GameSession gameSettings) {
		this.gameSession = gameSettings;
		this.healthMax = gameSettings.startingHealth;
		this.health = healthMax;

		switch (gameSettings.gameMode) {
		case STANDARD_FFA:
		case STANDARD_TEAM:
			setDamageHandler(new VanillaDamageHandler(this));
		case SMASH_FFA:
		case SMASH_TEAM:
			break;
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

		hitbox = new Hitbox(getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8, 16, 16);

		nameLabel = new GlyphLayout(Assets.font, info.name);
		healthHud = new HealthHud(this, getCenterX() - HealthHud.WIDTH / 2, getCenterY() - HealthHud.HEIGHT / 2 - 20f);

		equipGun(GunType.flamethrower);
		grenadeCooldown = new Cooldown(grenadeCooldownDuration);

		getGameContext().bringToFront(this);

		((JustAnotherGame) getGameContext().getGame()).getServer().send(info.id,
				new PlayerUpdate(3, new int[] { PlayerUpdate.GUN, PlayerUpdate.AMMO, PlayerUpdate.HEALTH },
						new float[][] { { GunType.pistol.ordinal() },
								{ gun.getMagasineAmmo(), gun.getMagasineSize(), gun.getAmmo() }, { health } }));
	}

	@Override
	public void update(float delta) {
		gun.update(delta);
		grenadeCooldown.update(delta);
		if (blockInput) {
			accelerate(0, 0);
		}
		move(delta);
		hitbox.update(getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8);
		healthHud.setX(getCenterX() - HealthHud.WIDTH / 2);
		healthHud.setY(getCenterY() - HealthHud.HEIGHT / 2 - 20f);
		healthHud.update(delta);

		if (isInvincible()) {
			spawnStars();
		}

		if (isFiring()) {
			fireBullet();
		}

		TileCollisionDetector.checkTileCollisions(getGameContext().getLevel(), this, tileCollisionListener);
	}

	private void spawnStars() {
		if (Math.random() < 0.2f) {
			getGameContext().spawn(new Star(getCenterX() + (float) ((Math.random() - 0.5f) * hitbox.rect.width * 2),
					getCenterY() + (float) ((Math.random() - 0.5f) * hitbox.rect.height * 2)));
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

		if (gameSession.drawNames) {
			Assets.font.draw(batch, nameLabel, getX() + getWidth() / 2 - nameLabel.width / 2,
					getY() + getHeight() + nameLabel.height);
		}
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
			if (gun.getMagasineAmmo() == 0) {
				reload();
			}
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
		healthHud.display();

	}

	public void death(Player killer) {
		if (isAlive()) {
			for (int i = 0; i < 200; i++) {
				getGameContext().spawn(
						new BloodSplatter(getCenterX(), getCenterY(), (float) (Math.random() * 2 * Math.PI), 40.0f));
			}

			gameSession.roundHandler.playerKilled(killer, this);
		}
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
		if (isInvincible()) {
			return;
		}
		for (int i = 0; i < bullet.getDamage(); i++) {
			getGameContext().spawn(new BloodSplatter(bullet.getCenterX(), bullet.getCenterY(),
					(float) (bullet.getRotation() + (Math.random() * 0.5f - 0.25f)), bullet.getDamage()));
			// getGameContext().bringToBack(temp);
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

}
