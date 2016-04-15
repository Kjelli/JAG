package no.kash.gamedev.jag.game.gamesession.roundhandlers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.game.gamecontext.GameContext;
import no.kash.gamedev.jag.game.gameobjects.players.Player;
import no.kash.gamedev.jag.game.gameobjects.players.PlayerInfo;
import no.kash.gamedev.jag.game.gameobjects.players.status.Status;
import no.kash.gamedev.jag.game.gamesession.GameSession;
import no.kash.gamedev.jag.game.screens.PlayScreen;

public class TeamRoundHandler extends AbstractRoundHandler<Integer> {

	Map<Integer, Integer> teamPlayersAlive;

	public TeamRoundHandler(PlayScreen playScreen, GameContext gameContext, GameSession gameSession,
			Map<Integer, Player> players) {
		super(playScreen, gameContext, gameSession, players);
		teamPlayersAlive = new HashMap<>();
	}

	@Override
	public void start() {
		super.start();
		teamPlayersAlive.clear();
		for (PlayerInfo player : gameSession.players.values()) {
			teamPlayersAlive.put(player.teamId, teamPlayersAlive.getOrDefault(player.teamId, 0) + 1);
		}
	}

	@Override
	public void playerKilled(Player killer, Player killed) {
		super.playerKilled(killer, killed);

		updateTeamStatus(killed);
	}

	@Override
	public void playerKilled(Player killed, Status status) {
		super.playerKilled(killed, status);
		updateTeamStatus(killed);
	}

	private void updateTeamStatus(Player killed) {
		int teamId = killed.getInfo().teamId;
		int leftOnTeam = teamPlayersAlive.get(teamId) - 1;
		teamPlayersAlive.put(teamId, leftOnTeam);

		if (leftOnTeam == 0) {
			teamPlayersAlive.remove(teamId);
			gameContext.getAnnouncer().announce("Team " + teamId + " eliminated!");
		}
	}

	@Override
	public RoundResult<Integer> roundEnded() {
		if (teamPlayersAlive.size() == 1) {
			int winningTeam = teamPlayersAlive.keySet().iterator().next();
			int wins = -1;
			for (Player player : players.values()) {
				if (player.getInfo().teamId == winningTeam) {
					player.setInvincible(true);
					player.getInfo().roundsWon++;
					if (wins == -1) {
						wins = player.getInfo().roundsWon;
					}
				}
			}
			int rtw = gameSession.settings.getSelectedValue(Defs.SESSION_RTW, Integer.class);
			if (rtw == -1 || wins < rtw) {
				gameContext.getAnnouncer().announce("=== Team " + winningTeam + " wins the round! ===");
			} else {
				gameContext.getAnnouncer().announce("*** TEAM " + winningTeam + " WINS THE MATCH! ***");
			}
			return new TeamRoundResult(false, winningTeam, wins == rtw);

		} else if (teamPlayersAlive.size() == 0) {
			return TeamRoundResult.NO_RESULT;
		}
		return null;
	}

	@Override
	public void setup() {
		super.setup();
		if (gameSession.settings.getSelectedValue(Defs.SESSION_TEST_MODE, Boolean.class)) {
			// Distribute teams evenly on 1 and 2
			int team = 1;
			int dummyId = -500;
			PlayerInfo dummyPlayer;
			while (gameSession.players.size() < 6) {
				dummyId++;
				dummyPlayer = new PlayerInfo();
				dummyPlayer.temporary = true;
				dummyPlayer.id = dummyId;
				gameSession.players.put(dummyId, dummyPlayer);
			}
			for (PlayerInfo player : gameSession.players.values()) {
				if (player.teamId == -1) {
					team = (team % 2) + 1;
					player.color = team == 1 ? Color.RED : Color.BLUE;
					player.teamId = team;
				}
			}
		}

	}

}
