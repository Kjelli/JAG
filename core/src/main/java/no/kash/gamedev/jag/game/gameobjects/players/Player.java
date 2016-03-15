package no.kash.gamedev.jag.game.gameobjects.players;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;

import no.kash.gamedev.jag.JustAnotherGame;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.packets.PlayerFeedback;
import no.kash.gamedev.jag.game.gamecontext.physics.Collidable;
import no.kash.gamedev.jag.game.gamecontext.physics.Collision;
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
import no.kash.gamedev.jag.game.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.tilecollisions.TileCollisionListener;

public class Player extends AbstractGameObject implements Collidable {

	private final TileCollisionListener tileCollisionListener;

	private final int id;
	private final String name;

	private boolean firing;

	private Gun gun;
	private Hitbox hitbox;
	private HealthHud healthHud;

	private float grenadePower;
	private float grenadeDirection;
	private float healthMax = 100;
	private float health = healthMax;

	private final GlyphLayout nameLabel;

	private DamageHandler damageHandler;
	
	public Player(int id, String name, float x, float y) {
		super(x, y, 64, 64);
		Sprite sprite = new Sprite(Assets.man);
		sprite.setOrigin(getWidth() / 2, getHeight() / 2);
		setSprite(sprite);

		setDamageHandler(new VanillaDamageHandler(this));

		hitbox = new Hitbox(x + getWidth() / 2 - 8, y + getHeight() / 2 - 8, 16, 16);

		this.id = id;
		this.name = name;
		this.tileCollisionListener = new TileCollisionListener() {
			@Override
			public void onCollide(MapObject rectangleObject, Rectangle intersection) {
				TileCollisionDetector.nudge(Player.this, intersection);
			}
		};

		nameLabel = new GlyphLayout(Assets.font, name);

		healthHud = new HealthHud(this, getCenterX() - HealthHud.WIDTH / 2, getCenterY() - HealthHud.HEIGHT / 2 - 20f);
		gun = new Gun(GunType.pistol);
		gun.equip(this);
	}

	@Override
	public void update(float delta) {
		gun.update(delta);
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
		if (!gun.isReloading()) {
			gun.draw(batch);
		}
		Assets.font.draw(batch, nameLabel, getX() + getWidth() / 2 - nameLabel.width / 2,
				getY() + getHeight() + nameLabel.height);

		healthHud.draw(batch);
	}
	
	public void equipWeapon(Weapon weapon){
		equipGun(weapon.gun);
	}
	
	public void equipGun(GunType type){
		gun = new Gun(type);
		gun.equip(this);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void fireBullet() {
		gun.shoot();
	}

	public void reload() {
		gun.reload();
	}

	public float getBulletOriginX() {
		return (float) (getCenterX() + Math.cos((rot) + (Math.PI / 2)) * getWidth() / 2);
	}

	public float getBulletOriginY() {
		return (float) (getCenterY() + Math.sin((rot) + (Math.PI / 2)) * getHeight() / 2);
	}

	@Override
	public Rectangle getBounds() {
		return hitbox.rect;
	}

	public void vibrate(int ms) {
		((JustAnotherGame) getGameContext().getGame()).getServer().send(id,
				new PlayerFeedback(PlayerFeedback.FEEDBACK_VIBRATION, new float[] { ms }));
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public boolean isFiring() {
		return firing;
	}

	public void holdGrenade(float power, float dir) {
		this.grenadeDirection = dir;
		this.grenadePower = power;
	}

	public void setHealth(float health) {
		this.health = health;
		if (health < 0) {
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
		setHealth(healthMax);
		setX(400);
		setY(400);
	}

	public float getHealth() {
		return health;
	}

	public float getHealthPercentage() {
		return health / healthMax;
	}

	public void releaseGrenade() {
		getGameContext().spawn(new Grenade(this, getCenterX(), getCenterY(), grenadeDirection, grenadePower));
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
	}

	public void damage(Explosion explosion) {
		damageHandler.onDamage(explosion);
	}


	
}
