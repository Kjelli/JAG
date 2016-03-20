package no.kash.gamedev.jag.game.screens;

import static no.kash.gamedev.jag.controller.screens.ControllerScreen.BUTTON_RELOAD;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_LEFT;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_MID;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_RIGHT;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.JagReceiver;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerConnect;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gameobjects.GameObject;
import no.kash.gamedev.jag.game.gameobjects.collectables.weapons.Weapon;
import no.kash.gamedev.jag.game.gameobjects.grenades.Grenade;
import no.kash.gamedev.jag.game.gameobjects.particles.Explosion;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gamesettings.GameSettings;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.levels.SpawnTile;

public class GameScreen extends AbstractGameScreen {

	@SuppressWarnings("rawtypes")
	private static final Class[] classes = new Class[] { Player.class, Weapon.class, SpawnTile.class, Grenade.class,
			Explosion.class };
	// TODO setup gamesettings beforehand, using STD for testing

	GameSettings gameSettings;
	Map<Integer, Player> players;
	Level level;

	// game)
	public GameScreen(JustAnotherGame game, GameSettings settings) {
		super(game);

		// TODO add gamesettings to constructor (required by setup menu to start
		this.gameSettings = settings;
		players = new HashMap<>();
	}

	@Override
	protected void update(float delta) {
		gameContext.update(delta);
		handleCamera(delta);
		level.update(delta);

	}

	@Override
	protected void draw(float delta) {
		level.render();
		gameContext.draw(batch);
	}

	@SuppressWarnings("unchecked")
	private void handleCamera(float delta) {
		if (players.size() > 0) {
			float leftMostX = Gdx.graphics.getWidth();
			float rightMostX = 0;
			float topMostY = 0;
			float bottomMostY = Gdx.graphics.getHeight();
			for (GameObject object : gameContext.getByClass(classes)) {
				if (object instanceof SpawnTile) {
					if (!((SpawnTile) object).isSpawning()) {
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

	@Override
	protected void onShow() {

		level = new Level(gameSettings, batch, getGameContext());
		gameContext.setLevel(level);

		for (PlayerInfo player : gameSettings.players.values()) {
			spawnPlayer(player);
		}

		game.setReceiver(new JagReceiver() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				System.out.println("GamePacket received: " + m);

				if (m instanceof PlayerStateChangeResponse) {

					PlayerStateChangeResponse resp = (PlayerStateChangeResponse) m;

					if (gameSettings.dropIn && resp.stateId == JustAnotherGameController.PLAY_STATE
							&& !players.containsKey(c.getID())) {
						PlayerInfo standard = new PlayerInfo();
						standard.id = c.getID();
						standard.name = "Minge_" + c.getID();
						standard.timesPlayed = -1;
						standard.color = new Color(Color.WHITE);
						spawnPlayer(standard);
					}
				}

				if (m instanceof PlayerConnect) {
					if (gameSettings.dropIn) {
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
				if (p == null) {
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

	private void spawnPlayer(PlayerInfo player) {
		Vector2 spawnPoint = level.playerSpawns.get((int) (Math.random() * level.playerSpawns.size()));
		float spawnX = spawnPoint.x;
		float spawnY = spawnPoint.y;
		if (!gameSettings.players.containsKey(player.id)) {
			gameSettings.players.put(player.id, player);
		}
		if (!players.containsKey(player.id)) {
			Player newlyJoined = new Player(gameSettings, player.id, spawnX, spawnY);
			players.put(player.id, newlyJoined);
			gameContext.spawn(newlyJoined);
		}
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
