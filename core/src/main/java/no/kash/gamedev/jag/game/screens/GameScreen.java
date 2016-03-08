package no.kash.gamedev.jag.game.screens;

import static no.kash.gamedev.jag.controller.screens.ControllerScreen.BUTTON;
import static no.kash.gamedev.jag.controller.screens.ControllerScreen.JOYSTICK;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.JustAnotherGame;
import no.kash.gamedev.jag.commons.network.JagReceiver;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.game.gameobjects.TemplateMan;
import no.kash.gamedev.jag.game.levels.Level;

public class GameScreen extends AbstractGameScreen {

	Map<Integer, TemplateMan> players;
	Level level;

	float[][] spawnPoints;

	public GameScreen(JustAnotherGame game) {
		super(game);
		players = new HashMap<>();
	}

	@Override
	protected void update(float delta) {
		gameContext.update(delta);
		if (players.size() > 0) {
			System.out.print(getCamera().position + " && ");
			for (TemplateMan man : players.values()) {
				System.out.println(man.position());
			}
		}
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

				TemplateMan p = players.getOrDefault(input.senderId, null);
				if (p == null) {
					return;
				}

				switch (input.inputId) {
				case JOYSTICK:
					p.velocity().x = input.state[0] * 150;
					p.velocity().y = input.state[1] * 150;
					break;
				case BUTTON:
					break;
				default:
					System.out.println("Unknown input: " + input.inputId);
				}

			}

			@Override
			public void handleConnection(Connection c) {
				log("Connection " + c.getID() + " made! [" + c.getRemoteAddressTCP() + "]");
				float[] spawnPoint = GameScreen.this.spawnPoints[(int)(Math.random() * GameScreen.this.spawnPoints.length)];
				float spawnX = spawnPoint[0];
				float spawnY = spawnPoint[1];
				TemplateMan man = new TemplateMan(c.getID(), "Minge", spawnX, spawnY);
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
