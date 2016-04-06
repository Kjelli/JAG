package no.kash.gamedev.jag.game.screens;

import static no.kash.gamedev.jag.controller.screens.ControllerScreen.BUTTON_RELOAD;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_LEFT;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_MID;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_RIGHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.esotericsoftware.kryonet.Connection;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.TweenableFloat;
import no.kash.gamedev.jag.commons.tweens.accessors.FloatAccessor;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.grenades.Grenade;
import no.kash.gamedev.jag.game.gameobjects.particles.Confetti;
import no.kash.gamedev.jag.game.gameobjects.particles.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.FFARoundHandler;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.RoundHandler;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.RoundResult;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.FFARoundResult;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.levels.PlayerSpawnPoint;
import no.kash.gamedev.jag.game.levels.WeaponSpawnTile;

public class PlayScreen extends AbstractGameScreen {

	@SuppressWarnings({ "rawtypes" })
	private static final Class[] focusCameraOnPOIs = new Class[] { Player.class, Weapon.class, WeaponSpawnTile.class,
			Grenade.class, Explosion.class }, focusCameraOnWinner = new Class[] { Player.class };

	GameSession gameSession;
	Map<Integer, Player> players;
	Level level;

	boolean gameOver = false;

	public RoundResult<?> result;

	// game)
	public PlayScreen(JustAnotherGame game, GameSession session) {
		super(game);
		players = new HashMap<>();
		this.gameSession = session;
		session.reset();
	}

	@Override
	protected void onShow() {
		stage.setViewport(new StretchViewport(Defs.WIDTH, Defs.HEIGHT, camera));
		initInputReceiver();
		initSession();
		loadLevel();
		start();
	}

	private void initSession() {
		gameSession.init(this, gameContext, players);
	}

	@Override
	protected void update(float delta) {
		gameContext.update(delta);
		handleCamera(delta);
		level.update(delta);

		checkWinCondition();
	}

	public void spawnConfetti() {
		for (int i = 0; i < 2; i++) {
			float x = (float) (Math.random()
					* (camera.position.x + camera.viewportWidth * camera.zoom + 2 * Confetti.WIDTH) - Confetti.WIDTH);
			float y = camera.position.y + camera.viewportHeight * camera.zoom;
			gameContext.spawn(new Confetti(x, y));
		}
	}

	@Override
	protected void draw(SpriteBatch batch, float delta) {
		level.render();
		gameContext.draw(batch);
	}

	@Override
	protected void drawHud(SpriteBatch hudBatch, float delta) {
		getGameContext().getAnnouncer().draw(hudBatch);
	}

	public void start() {
		gameOver = false;
		level.spawnWeaponTiles();
		gameSession.roundHandler.start();
	}

	public void restart() {
		getGameContext().clear();
		players.clear();
		level.resetPlayerSpawns();
		start();

	}

	public void spawnPlayer(PlayerInfo playerInfo) {

		if (gameSession.gameMode == GameMode.STANDARD_TEAM) {
			Rectangle spawnZone = level.teamSpawnZones.get(playerInfo.teamId);

			float spawnX = (float) (spawnZone.x + Player.WIDTH / 2 + Math.random() * (spawnZone.width - Player.WIDTH));
			float spawnY = (float) (spawnZone.y + Player.HEIGHT / 2
					+ Math.random() * (spawnZone.height - Player.HEIGHT));

			Player player = new Player(gameSession, playerInfo, spawnX, spawnY);
			players.put(playerInfo.id, player);
			gameContext.spawn(player);
			return;
		}

		int index = (int) (Math.random() * level.playerSpawns.size());
		while (true) {
			if (index >= level.playerSpawns.size()) {
				break;
			}

			PlayerSpawnPoint spawnPoint = level.playerSpawns.get(index);
			if (spawnPoint.taken) {
				index++;
				continue;
			}

			float spawnX = spawnPoint.position.x - Player.WIDTH / 2;
			float spawnY = spawnPoint.position.y - Player.HEIGHT / 2;
			Player player = new Player(gameSession, playerInfo, spawnX, spawnY);
			players.put(playerInfo.id, player);
			gameContext.spawn(player);

			spawnPoint.taken = true;
			index++;

			return;
		}
		gameContext.getAnnouncer().announce("Too many players! (max " + level.playerSpawns.size() + ")");

	}

	private void checkWinCondition() {
		if (!gameOver) {
			result = gameSession.roundHandler.roundEnded();
			if (result != null) {
				gameOver = true;
				gameSession.roundHandler.proceed();
			}
		} else if (result.isGameEnding()) {
			spawnConfetti();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void handleCamera(float delta) {
		if (players.size() > 0) {
			float leftMostX = Defs.WIDTH;
			float rightMostX = 0;
			float topMostY = 0;
			float bottomMostY = Defs.HEIGHT;
			Class[] focusClasses;
			if (gameOver) {
				focusClasses = focusCameraOnWinner;
			} else {
				focusClasses = focusCameraOnPOIs;
			}
			for (GameObject object : gameContext.getByClass(focusClasses)) {
				if (object instanceof WeaponSpawnTile) {
					if (!((WeaponSpawnTile) object).isSpawning()) {
						continue;
					}
				} else if (object instanceof Weapon) {
					if (((Weapon) object).getAliveTime() > 5.0f) {
						continue;
					}
				}
				if (object.getCenterX() < leftMostX) {
					leftMostX = object.getCenterX();
				}
				if (object.getCenterX() > rightMostX) {
					rightMostX = object.getCenterX();
				}
				if (object.getCenterY() > topMostY) {
					topMostY = object.getCenterY();
				}
				if (object.getCenterY() < bottomMostY) {
					bottomMostY = object.getCenterY();
				}
			}

			float margin = 200;

			float centerX = leftMostX + (rightMostX - leftMostX) / 2;
			float centerY = bottomMostY + (topMostY - bottomMostY) / 2;
			float horizontalRatio = (rightMostX - leftMostX + margin) / (stage.getWidth());
			float verticalRatio = (topMostY - bottomMostY + margin) / (stage.getHeight());
			float zoom = Math.max(Math.max(horizontalRatio, verticalRatio), 0.2f);
			float delay = 0.90f;

			camera.position.set((delay * camera.position.x + centerX * (1 - delay)),
					(delay * camera.position.y + centerY * (1 - delay)), 0);
			camera.zoom = camera.zoom * delay + zoom * (1 - delay);
			camera.update();
		}

	}

	private void initInputReceiver() {

		game.setReceiver(new JagServerPacketHandler() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerStateChangeResponse) {
					PlayerStateChangeResponse resp = (PlayerStateChangeResponse) m;
					// TODO handle proper dropin
				}

				if (m instanceof PlayerConnect) {
					if (gameSession.dropIn) {
						game.getServer().send(c.getID(), new PlayerStateChange(JustAnotherGameController.PLAY_STATE));
					} else {
						// TODO send message to wait for next game
					}
				}
			}

			@Override
			public void handleInput(PlayerInput input) {

				// Print input received from player
				// printInput(input);

				Player p = players.getOrDefault(input.senderId, null);
				if (p == null || p.isInputBlocked()) {
					return;
				}

				switch (input.inputId) {
				case JOYSTICK_LEFT:
					p.accelerate(input.state[0], input.state[1]);
					break;
				case JOYSTICK_RIGHT:
					float x = input.state[0];
					float y = input.state[1];
					if (x != 0 || y != 0) {
						p.setRotation((float) (Math.atan2(y, x) - Math.PI / 2));
						p.setFiring(true);
					} else {
						p.setFiring(false);
					}
					break;
				case JOYSTICK_MID:
					float velx = input.state[0];
					float vely = input.state[1];
					float power = (float) Math.hypot(velx, vely);
					float dir = (float) (Math.atan2(vely, velx));
					if (velx != 0 || vely != 0) {
						p.holdGrenade(power, (float) (dir + Math.PI));
					} else {
						p.releaseGrenade();
					}
					break;

				case BUTTON_RELOAD:
					p.reload();
					break;
				default:
					System.out.println("Unknown input: " + input.inputId);
				}

			}

			@Override
			public void handleDisconnection(Connection c) {
				log("Connection " + c.getID() + " lost");
				gameSession.players.remove(c.getID());
				gameContext.despawn(players.remove(c.getID()));
			}

			@SuppressWarnings("unused")
			protected void printInput(PlayerInput input) {
				StringBuilder stateString = new StringBuilder("{");
				for (int i = 1; i < input.state.length; i++) {
					stateString.append(input.state[i] + ", ");
				}
				stateString.append(input.state[input.state.length - 1] + "}");
				log("Input: " + input.inputId + "" + stateString.toString() + "");
			}
		});

	}

	private void loadLevel() {
		level = new Level(gameSession, getSpriteBatch(), getGameContext());
		gameContext.setLevel(level);
	}

	public void log(String s) {
		System.out.println(s);
	}

	@Override
	public void dispose() {
		super.dispose();
		level.dispose();
	}

}
