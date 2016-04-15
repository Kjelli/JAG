package no.kash.gamedev.jag.game.gameobjects.players.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.kash.gamedev.jag.game.gameobjects.particles.HealingParticle;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class StatusHandler {
	public List<Status> statuses;
	public Player player;
	public boolean removeAllNegativeStatuses = false;

	public StatusHandler(Player player) {
		this.player = player;
		statuses = new ArrayList<>();
	}

	public void update(float delta) {
		for (Iterator<Status> it = statuses.iterator(); it.hasNext();) {
			Status next = it.next();
			if (removeAllNegativeStatuses) {
				switch (next.type) {
				case healing:
					break;
				default:
					it.remove();
					continue;
				}
			}
			if (next.finished) {
				it.remove();
			} else {
				next.update(delta);
				next.effect(player);
			}
		}
		if (removeAllNegativeStatuses) {
			removeAllNegativeStatuses = false;
		}
	}

	public void apply(Status status) {
		for (Status s : statuses) {
			if (s.type == status.type) {
				// Already exists? refresh;
				s.duration.start();
				return;
			}
		}
		statuses.add(status);
		status.duration.start();
	}

	public boolean hasNegativeStatuses() {
		for (Status s : statuses) {
			if (s.isNegative()) {
				return true;
			}
		}
		return false;
	}

}
