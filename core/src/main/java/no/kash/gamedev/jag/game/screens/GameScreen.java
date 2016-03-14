package no.kash.gamedev.jag.game.screens;

import static no.kash.gamedev.jag.controller.screens.ControllerScreen.*;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.JustAnotherGame;
import no.kash.gamedev.jag.commons.network.JagReceiver;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.levels.Level;

public class GameScreen extends AbstractGameScreen {

	Map<Integer, Player> players;
	Level level;

	float[][] spawnPoints;

	public GameScreen(JustAnotherGame game) {
		super(game);
		players = new HashMap<>();
	}

	@Override
	protected void update(float delta) {
		gameContext.update(delta);

		handleCamera(delta);

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
			for (Player player : players.values()) {
				if (player.getCenterX() < leftMostX) {
					leftMostX = player.getCenterX();
				}
				if (player.getCenterX() > rightMostX) {
					rightMostX = player.getCenterX();
				}
				if (player.getCenterY() > topMostY) {
					topMostY = player.getCenterY();
				}
				if (player.getCenterY() < bottomMostY) {
					bottomMostY = player.getCenterY();
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
		level = new Level(getCamera(), batch);

		MapObjects spawnPoints = level.map.getLayers().get("spawnpoints").getObjects();
		this.spawnPoints = new float[spawnPoints.getCount()][];
		for (int i = 0; i < spawnPoints.getCount(); i++) {
			MapObject spawnPoint = spawnPoints.get(i);
			float x = spawnPoint.getProperties().get("x", Float.class);
			float y = spawnPoint.getProperties().get("y", Float.class);
			this.spawnPoints[i] = new float[] { x, y };
		}

		Player man = new Player(level, -1, "Small Electric Car", 400, 400);
		players.put(-666, man);
		gameContext.spawn(man);

		game.getServer().listen(13337);
		game.setReceiver(new JagReceiver() {

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
					p.velocity().x = input.state[0] * 140;
					p.velocity().y = input.state[1] * 140;
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
			public void handleConnection(Connection c) {
				log("Connection " + c.getID() + " made! [" + c.getRemoteAddressTCP() + "]");
				float[] spawnPoint = GameScreen.this.spawnPoints[(int) (Math.random()
						* GameScreen.this.spawnPoints.length)];
				float spawnX = spawnPoint[0];
				float spawnY = spawnPoint[1];
				Player man = new Player(level, c.getID(), "Minge", spawnX, spawnY);
				gameContext.spawn(man);
				players.put(c.getID(), man);
			}

			@Override
			public void handleDisconnection(Connection c) {
				log("Connection " + c.getID() + " lost");
				gameContext.despawn(players.remove(c.getID()));
			}

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
