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
import no.kash.gamedev.jag.game.gameobjects.grenades.Grenade;
import no.kash.gamedev.jag.game.gameobjects.particles.BloodSplatter;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Gun;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.guns.Pistol;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.tilecollisions.TileCollisionDetector;
import no.kash.gamedev.jag.game.tilecollisions.TileCollisionListener;

public class Player extends AbstractGameObject implements Collidable {
	private final int id;
	private final String name;

	private float grenadePower;
	private float grenadeDirection;

	private final TileCollisionListener tileCollisionListener;

	private final GlyphLayout nameLabel;
	private Gun gun;
	private Hitbox hitbox;
	private boolean firing;

	public Player(int id, String name, float x, float y) {
		super(x, y, 64, 64);
		Sprite sprite = new Sprite(Assets.man);
		sprite.setOrigin(getWidth() / 2, getHeight() / 2);
		setSprite(sprite);

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
		gun = new Pistol(GunType.pistol);
		gun.setAmmo(100);
		gun.reload();
		gun.equip(this);
	}

	@Override
	public void update(float delta) {
		gun.update(delta);
		move(delta);
		hitbox.update(getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8);

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
	public void onCollide(Collision collision) {
		if (collision.getTarget() instanceof Bullet) {
			Bullet target = (Bullet) collision.getTarget();
			if (target.getShooter()
					.equals(this) /* || target.distanceTo(this) > 16 */) {
				return;
			}

			for (int i = 0; i < 20; i++) {
				getGameContext().spawn(new BloodSplatter(target.getCenterX(), target.getCenterY(),
						(float) (target.getRotation() + (Math.random() * 0.5f - 0.25f))));
			}
			// Vibration
			vibrate(100);
			target.destroy();
		}
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

	public void releaseGrenade() {
		getGameContext().spawn(new Grenade(this, getCenterX(), getCenterY(), grenadeDirection, grenadePower));
	}
}
