package no.kash.gamedev.jag.game.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.network.JagServerPacketHandler;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerAvailableMaps;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerMapVote;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.levels.MapHandler;
import no.kash.gamedev.jag.game.levels.MiniMap;

public class MapSelectionScreen extends AbstractGameScreen {

	private static final int MAX_MAPS = 3;
	GameSession session;
	List<MiniMap> miniMaps;

	private static BitmapFont font = Assets.font;

	Cooldown selectionTimer;
	GlyphLayout timerLabel;
	GlyphLayout title;

	Map<Integer, Integer> votes;

	public MapSelectionScreen(JustAnotherGame game, GameSession session) {
		super(game);
		this.session = session;
		votes = new HashMap<>();
		selectionTimer = new Cooldown(10.0f);
	}

	@Override
	protected void update(float delta) {
		gameContext.update(delta);
		for (int i = 0; i < miniMaps.size(); i++) {
			MiniMap mm = miniMaps.get(i);
			mm.setY((float) (stage.getHeight() / 2 - mm.getEffectiveMapHeight() / 2
					- 5 * Math.sin(gameContext.getElapsedTime() + (i + 1) * 20)));
		}

		if (selectionTimer.isOnCooldown()) {
			selectionTimer.update(delta);
			timerLabel.setText(font,
					"Round starting in " + String.format("%.0f seconds", selectionTimer.getCooldownTimer()),
					Color.WHITE, -1, -1, false);

		} else {
			decideMap();
		}
	}

	@Override
	protected void draw(SpriteBatch batch, float delta) {
		for (MiniMap miniMap : miniMaps) {
			miniMap.draw(batch);
		}
		font.draw(batch, title, stage.getWidth() / 2 - title.width / 2, stage.getHeight() - title.height);
		font.draw(batch, timerLabel, stage.getWidth() / 2 - timerLabel.width / 2, stage.getHeight() - title.height * 5);
	}

	@Override
	protected void drawHud(SpriteBatch hudBatch, float delta) {

	}

	@Override
	protected void onShow() {
		selectionTimer.start();
		getGame().getServer().broadcast(new PlayerStateChange(JustAnotherGameController.VOTE_MAP));

		// TODO: REMOVE:
		// session.settings.selectNext(Defs.SESSION_GM);

		title = new GlyphLayout(font, "Map Selection", Color.WHITE, -1, -1, false);
		timerLabel = new GlyphLayout(font,
				"Round starting in " + String.format("%.0f seconds", selectionTimer.getCooldownTimer()), Color.WHITE,
				-1, -1, false);

		initMaps();

		game.setReceiver(new JagServerPacketHandler() {

			@Override
			public void handleInput(PlayerInput input) {
				//
			}

			@Override
			public void handleDisconnection(Connection c) {
				session.players.remove(c.getID());
				votes.remove(c.getID());
			}

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerMapVote) {
					PlayerMapVote vote = (PlayerMapVote) m;
					if (!votes.containsKey(c.getID())) {
						votes.put(c.getID(), vote.mapIndex);
						if (votes.size() == session.numberOfPlayers()) {
							decideMap();
						}
					}
				} else if (m instanceof PlayerStateChangeResponse) {
					game.getServer().send(c.getID(), new PlayerAvailableMaps(miniMaps.size()));
				}
			}

		});
	}

	protected void decideMap() {
		int decidedMapIndex;
		if (votes.size() > 0) {
			Map<Integer, Integer> countedVotes = new HashMap<>();

			int maxVal = 0;
			for (int i : votes.values()) {
				int newVal = countedVotes.getOrDefault(i, 0) + 1;
				countedVotes.put(i, newVal);
				if (newVal > maxVal) {
					maxVal = newVal;
				}
			}
			for (Iterator<Entry<Integer, Integer>> it = countedVotes.entrySet().iterator(); it.hasNext();) {
				Entry<Integer, Integer> next = it.next();
				if (next.getValue() < maxVal) {
					it.remove();
				}
			}

			List<Integer> candidateMaps = new ArrayList<>();
			for (int i : countedVotes.keySet()) {
				candidateMaps.add(i);
			}

			decidedMapIndex = candidateMaps.get((int) (Math.random() * candidateMaps.size()));
		} else {
			decidedMapIndex = (int) (Math.random() * miniMaps.size());
		}
		session.mapFilename = miniMaps.get(decidedMapIndex).getFilename();
		game.setScreen(new PlayScreen(game, session));
	}

	private void initMaps() {
		miniMaps = new ArrayList<>();
		FileHandle[] maps = MapHandler.availableMaps();

		// Find compatible maps
		for (int i = 0; i < maps.length; i++) {
			MiniMap mm = MiniMap.build(maps[i]);
			boolean compatible = mm.isCompatible(session);
			if (compatible) {
				miniMaps.add(mm);
			}
		}
		// Trim the list randomly, to fit MAX_MAPS number of maps
		while (miniMaps.size() > MAX_MAPS) {
			miniMaps.remove((int) (Math.random() * miniMaps.size()));
		}

		// Prepare maps
		float tWidth = (stage.getWidth() - 50) / Math.min(miniMaps.size(), MAX_MAPS);
		for (int i = 0; i < miniMaps.size(); i++) {
			MiniMap mm = miniMaps.get(i);
			mm.setTargetWidth(tWidth);
			mm.setX(stage.getWidth() / 2 + (i - miniMaps.size() / 2.0f) * tWidth + (i - miniMaps.size() / 2.0f) * 8f);
			mm.setY((float) (stage.getHeight() / 2
					- mm.getEffectiveMapHeight() / 2 * Math.sin(gameContext.getElapsedTime())));
		}

	}

	@Override
	protected void debugDraw(ShapeRenderer renderer) {

	}

}
