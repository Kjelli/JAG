package no.kash.gamedev.jag.game.screens;

import static no.kash.gamedev.jag.controller.screens.ControllerScreen.BUTTON_RELOAD;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_LEFT;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_MID;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_RIGHT;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
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
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gamesettings.GameMode;
import no.kash.gamedev.jag.game.gamesettings.GameSettings;
import no.kash.gamedev.jag.game.levels.Level;
import no.kash.gamedev.jag.game.levels.SpawnTile;

public class GameScreen extends AbstractGameScreen {

	private static final Class[] classes = new Class[] { Player.class, Weapon.class, SpawnTile.class };
	// TODO setup gamesettings beforehand, using STD for testing
	public static GameSettings STD;
	static {
		STD = new GameSettings();
		STD.gameMode = GameMode.STANDARD_FFA;
		STD.mapFilename = "maps/sumoarena1.tmx";
		STD.startingHealth = 100f;
		STD.rounds = 3;
		STD.roundTime = 60.0f;
	}

	GameSettings gameSettings;
	Map<Integer, Player> players;
	Level level;

	// TODO quickfix for spawning players
	float[][] spawnPoints;

	// game)
	public GameScreen(JustAnotherGame game) {
		super(game);

		// TODO add gamesettings to constructor (required by setup menu to start
		this.gameSettings = STD;
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

		// Quick fix for spawning
		MapObjects spawnPoints = level.map.getLayers().get("spawnpoints").getObjects();
		this.spawnPoints = new float[spawnPoints.getCount()][];
		for (int i = 0; i < spawnPoints.getCount(); i++) {
			MapObject spawnPoint = spawnPoints.get(i);
			float x = spawnPoint.getProperties().get("x", Float.class);
			float y = spawnPoint.getProperties().get("y", Float.class);
			this.spawnPoints[i] = new float[] { x, y };
		}

		Player man = new Player(gameSettings, -1, "Dummy", 400, 400);
		players.put(-666, man);
		gameContext.spawn(man);
		game.setReceiver(new JagReceiver() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				System.out.println("GamePacket received: " + m);

				if (m instanceof PlayerStateChangeResponse) {

					PlayerStateChangeResponse resp = (PlayerStateChangeResponse) m;

					if (resp.stateId == JustAnotherGameController.PLAY_STATE && !players.containsKey(c.getID())) {
						log("Connection " + c.getID() + " made! [" + c.getRemoteAddressTCP() + "]");
						float[] spawnPoint = GameScreen.this.spawnPoints[(int) (Math.random()
								* GameScreen.this.spawnPoints.length)];
						float spawnX = spawnPoint[0];
						float spawnY = spawnPoint[1];
						Player man = new Player(gameSettings, c.getID(), "Minge", spawnX, spawnY);
						gameContext.spawn(man);
						players.put(c.getID(), man);
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
						p.setRotation((float) (dir + Math.PI / 2));
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

	public void log(String s) {
		System.out.println(s);
	}

	@Override
	public void dispose() {
		super.dispose();
		level.dispose();
	}

}
