package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.packets.PlayerNewStats;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.TweenableFloat;
import no.kash.gamedev.jag.commons.tweens.accessors.FloatAccessor;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.announcer.Announcement;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.LeadState;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gameobjects.players.guns.GunType;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;
import no.kash.gamedev.jag.game.gamesession.GameMode;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.gamesession.roundhandlers.suddendeaths.SuddenDeathType;
import no.kash.gamedev.jag.game.screens.LobbyScreen;
import no.kash.gamedev.jag.game.screens.PlayScreen;

public abstract class AbstractRoundHandler<T> implements RoundHandler<T> {

	public static final BitmapFont font = Announcement.font;

	protected Map<Integer, Player> players;
	protected PlayScreen gameScreen;
	protected GameContext gameContext;
	protected GameSession gameSession;

	protected GlyphLayout roundTimeLabel;
	protected Cooldown roundTime;

	protected boolean firstFrame = true;
	protected boolean hasStarted;
	protected boolean suddenDeath = false;

	protected int currentRound = 0;

	protected float roundTimeX;
	protected float roundTimeY;

	private boolean roundFinished;

	public AbstractRoundHandler(PlayScreen screen, GameContext gameContext, GameSession gameSession,
			Map<Integer, Player> players) {
		this.players = players;
		this.gameScreen = screen;
		this.gameContext = gameContext;
		this.gameSession = gameSession;
		this.roundTime = new Cooldown(
				(int) gameSession.settings.getSelectedValue(Defs.SESSION_ROUNDTIME, Integer.class));
		this.roundTime.start();
		this.roundTimeLabel = new GlyphLayout(Assets.announcerFont,
				String.format("%.2f", roundTime.getCooldownTimer()));

	}

	@Override
	public void playerKilled(Player killer, Player killed) {
		killed.destroy();
		if (killer.equals(killed)) {
			gameContext.getAnnouncer().announce(killer + " committed suicide", 3.0f);
		} else {
			gameContext.getAnnouncer().announce(killer + " killed " + killed, 3.0f);
		}

		killer.getInfo().killed.add(killed.getInfo());
		killed.getInfo().killedBy.add(killer.getInfo());
		players.remove(killed.getId());
		notifyKD(killer, killed);
	}

	@Override
	public void playerKilled(Player killed, Status status) {
		killed.destroy();

		if (status.causer != null) {
			// Status was caused by someone else
			status.causer.getInfo().killed.add(killed.getInfo());
			killed.getInfo().killedBy.add(status.causer.getInfo());
			gameContext.getAnnouncer()
					.announce(killed + " " + status.type.killMessage + " " + status.causer.getInfo().name);
		} else {
			// SUICIDE OTHERWISE
			killed.getInfo().killedBy.add(killed.getInfo());
		}
		players.remove(killed.getId());
		notifyKD(status.causer, killed);
	}

	private void updateLead() {
		for(Player player : players.values()){
			player.setLeadState(LeadState.NONE);
		}
		int maxWins = -1;
		List<PlayerInfo> lead = new ArrayList<>();
		for (PlayerInfo player : gameSession.players.values()) {
			if (player.roundsWon > maxWins) {
				maxWins = player.roundsWon;
				lead.clear();
				lead.add(player);
			} else if (player.roundsWon == maxWins) {
				lead.add(player);
			}
		}
		if (maxWins == 0) {
			return;
		}
		if (lead.size() > 1) {
			for (PlayerInfo player : lead) {
				if (players.containsKey(player.id)) {
					players.get(player.id).setLeadState(LeadState.TIED_FOR_LEAD);
				}
			}
		} else {
			if (players.containsKey(lead.get(0).id)) {
				players.get(lead.get(0).id).setLeadState(LeadState.LEAD);
			}
		}
	}

	public void notifyKD(Player killer, Player killed) {
		float[][] killerState = new float[1][2];
		float[][] killedState = new float[1][2];
		if (killer.equals(killed)) {
			// Killer got negative kill and death
			killerState[0][0] = -1;
			killerState[0][1] = 1;
		} else {
			// Killer got kill
			killerState[0][0] = 1;
			killerState[0][1] = 0;
			// Killed got death
			killedState[0][0] = 0;
			killedState[0][1] = 1;

			gameScreen.getGame().getServer().send(killed.getInfo().id,
					new PlayerUpdate(1, new int[] { PlayerUpdate.KILL_DEATH }, killedState));
		}
		gameScreen.getGame().getServer().send(killer.getInfo().id,
				new PlayerUpdate(1, new int[] { PlayerUpdate.KILL_DEATH }, killerState));

	}

	public void update(float delta) {

		if (firstFrame) {
			firstFrame = false;
			onPlayersSpawned();
		}

		if (!hasStarted) {
			if (this.roundTime.getCooldownTimer() > 0) {
				this.roundTime.start();
			} else if (!suddenDeath) {
				suddenDeath();
			}
			return;
		}
		if (roundTime.isOnCooldown() && !roundFinished) {
			roundTime.update(delta);
			roundTimeLabel.setText(Assets.announcerFont, String.format("%.2f", roundTime.getCooldownTimer()));
		} else if (!roundFinished && !suddenDeath) {
			suddenDeath = true;
			suddenDeath();
		}
	}

	protected void onPlayersSpawned() {
		for (Player player : players.values()) {
			gameScreen.getGame().getServer().send(player.getInfo().id,
					new PlayerUpdate(4,
							new int[] { PlayerUpdate.GUN, PlayerUpdate.AMMO, PlayerUpdate.ITEM, PlayerUpdate.HEALTH },
							new float[][] { { player.getGun().getType().ordinal() },
									{ player.getGun().getMagasineAmmo(), player.getGun().getMagasineSize(),
											player.getGun().getAmmo() },
									{ player.getThrowable().getType().ordinal(), player.getThrowable().getUses() },
									{ player.getHealth() } }));
		}
		updateLead();
	}

	@Override
	public void setup() {
		if (gameSession.settings.getSelectedValue(Defs.SESSION_GM, GameMode.class) == GameMode.STANDARD_FFA && gameSession.players.size() > gameContext.getLevel().playerSpawns.size()) {
			gameScreen.getGame().setScreen(new LobbyScreen(gameScreen.getGame(), gameSession));
			System.out.println("TOO MANY PLAYERS");
		}
	}

	public void suddenDeath() {
		roundTimeLabel.setText(Assets.announcerFont, "SUDDEN DEATH");
		switch (gameSession.settings.getSelectedValue(Defs.SESSION_SUDDEN_DEATH, SuddenDeathType.class)) {
		case none:
			roundTimeLabel.setText(Assets.announcerFont, "");
			break;
		case ONE_HP:
			for (Player player : players.values()) {
				player.setHealth(1);
				((JustAnotherGame) gameContext.getGame()).getServer().send(player.getInfo().id,
						new PlayerUpdate(2, new int[] { PlayerUpdate.HEALTH, PlayerUpdate.FEEDBACK_VIBRATION },
								new float[][] { { player.getHealth() }, { 30.0f } }));
			}
			break;
		case GOLDEN_GUN:
			gameContext.getLevel().weaponSpawner.stop();
			for (Player player : players.values()) {
				player.equipGun(GunType.goldengun, 97, 3);
			}
			break;
		case PISTOLS_ONLY:
			gameContext.getLevel().weaponSpawner.stop();
			for (Player player : players.values()) {
				player.equipGun(GunType.pistol);
			}
			break;
		default:
			break;

		}
	}

	public boolean isSuddenDeath() {
		return suddenDeath;
	}

	@Override
	public void drawRoundTime(SpriteBatch batch) {
		font.draw(batch, roundTimeLabel, roundTimeX - roundTimeLabel.width / 2, roundTimeY);
	}

	@Override
	public void proceed() {
		roundFinished = true;
		gameContext.setTimeModifier(0.25f);

		// No op
		TweenableFloat f = new TweenableFloat(0);
		final boolean gameOver = gameOver();

		TweenGlobal.start(Tween.from(f, FloatAccessor.TYPE_VALUE, gameOver ? 3.0f : 1.25f).target(1)
				.setCallback(new TweenCallback() {
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						if (arg0 == TweenCallback.COMPLETE) {
							gameContext.setTimeModifier(1.0f);
							currentRound++;
							if (!gameOver) {
								gameScreen.restart();
								firstFrame = true;
							} else {
								statsHandler();
								gameScreen.getGame().setScreen(new LobbyScreen(gameScreen.getGame(), gameSession));

							}
						}
					}

				}));

	}

	private void statsHandler() {
		JustAnotherGame game = (JustAnotherGame) gameContext.getGame();
		for (PlayerInfo info : gameSession.players.values()) {
			int expEarned = 10;
			expEarned += info.killed.size() * 5;
			expEarned += info.roundsWon * 7;
			game.getServer().send(info.id, new PlayerNewStats(expEarned));
		}

	}

	protected boolean gameOver() {
		for (PlayerInfo info : gameSession.players.values()) {
			if (info.roundsWon >= gameSession.settings.getSelectedValue(Defs.SESSION_RTW, Integer.class)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canJoin() {
		// Default is if no one has died yet
		return gameSession.settings.getSelectedValue(Defs.SESSION_TEST_MODE, Boolean.class)
				&& players.size() == gameSession.players.size();
	}

	@Override
	public void start() {
		hasStarted = false;
		suddenDeath = false;
		roundFinished = false;
		roundTimeX = gameContext.getStage().getViewport().getScreenWidth() / 2;
		roundTimeY = gameContext.getStage().getViewport().getScreenHeight();
		roundTime.start();
		roundTimeLabel.setText(Assets.announcerFont, String.format("%.2f", roundTime.getCooldownTimer()));

		if (!gameSession.settings.getSelectedValue(Defs.SESSION_SPAWN_GUNS, Boolean.class)) {
			gameContext.getLevel().weaponSpawner.stop();
		}

		for (PlayerInfo playerInfo : gameSession.players.values()) {
			gameScreen.spawnPlayer(playerInfo);

			Player player = null;

			// If spawning succeeded, block input
			if (players.containsKey(playerInfo.id)) {
				player = players.get(playerInfo.id);
				player.blockInput(true);
			}
		}
		// No op
		TweenableFloat f = new TweenableFloat(0);
		TweenGlobal.start(Tween.from(f, FloatAccessor.TYPE_VALUE, 3.0f).target(1).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				if (arg0 == TweenCallback.COMPLETE) {
					for (PlayerInfo player : gameSession.players.values()) {
						if (players.containsKey(player.id)) {
							players.get(player.id).blockInput(false);

						}
					}
					hasStarted = true;
				}
			}
		}));

		gameContext.getAnnouncer().announceRoundStart(gameSession.roundHandler.currentRound(), 3);
	}

	@Override
	public void reset() {
		currentRound = 0;
	}

	@Override
	public float getRoundTimer() {
		return roundTime.getCooldownTimer();
	}

	@Override
	public int currentRound() {
		return currentRound;
	}

}
