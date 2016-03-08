package no.kash.gamedev.jag.game.screens;

import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_LEFT;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK_RIGHT;

import java.util.HashMap;
import java.util.Map;

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
	}

	@Override
	protected void draw(float delta) {
		level.render();
		gameContext.draw(batch);

	}

	@Override
	protected void onShow() {
		level = new Level(getCamera(), batch);

		MapObjects spawnPoints = level.map.getLayers().get("spawns").getObjects();
		this.spawnPoints = new float[spawnPoints.getCount()][];
		for (int i = 0; i < spawnPoints.getCount(); i++) {
			MapObject spawnPoint = spawnPoints.get(i);
			float x = spawnPoint.getProperties().get("x", Float.class);
			float y = spawnPoint.getProperties().get("y", Float.class);
			this.spawnPoints[i] = new float[] { x, y };
		}

		game.getServer().listen(13337);
		game.setReceiver(new JagReceiver() {

			@Override
			public void handleInput(PlayerInput input) {
				printInput(input);

				Player p = players.getOrDefault(input.senderId, null);
				if (p == null) {
					return;
				}

				switch (input.inputId) {
				case JOYSTICK_LEFT:
					p.velocity().x = input.state[0] * 70;
					p.velocity().y = input.state[1] * 70;
					break;
				case JOYSTICK_RIGHT:
					float rot = (float) (1.5 * Math.PI - Math.atan2(input.state[0], input.state[1]) * 180 / Math.PI);
					p.setRotation(rot);
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
				Player man = new Player(c.getID(), "Minge", spawnX, spawnY);
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
