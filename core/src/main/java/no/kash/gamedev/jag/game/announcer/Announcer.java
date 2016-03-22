package no.kash.gamedev.jag.game.announcer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;

public class Announcer {

	float x, y;

	List<Announcement> active;
	List<Announcement> newlyAdded;

	Cooldown announceCd;

	public Announcer(float x, float y) {
		active = new ArrayList<>();
		newlyAdded = new ArrayList<>();
		this.x = x;
		this.y = y;

		announceCd = new Cooldown(Announcement.FADE_IN_TIME + Announcement.FADE_AWAY_TIME);
	}

	public void update(float delta) {
		announceCd.update(delta);
		if (!announceCd.isOnCooldown() && newlyAdded.size() > 0) {
			announceCd.startCooldown();
			Announcement newAnn = newlyAdded.remove(0);
			active.add(0, newAnn);
			newAnn.setAnnouncer(this);
			for (int i = 1; i < active.size(); i++) {
				Announcement oldAnno = active.get(i);
				TweenGlobal.start(Tween.to(oldAnno.getOffset(), Vector2Accessor.TYPE_Y, Announcement.FADE_IN_TIME)
						.target(oldAnno.getOffset().y - oldAnno.getHeight() - 5f));
			}
		}

		for (Announcement a : active) {
			a.update(delta);
			if ((a.timer -= delta) < 0) {
				a.destroy();
			}
		}
	}

	public void draw(SpriteBatch batch) {
		for (Announcement announcement : active) {
			announcement.draw(x, y, batch);
		}
	}

	@Deprecated
	public void announce(String announcement, float timeToLive, Color color) {
		newlyAdded.add(new Announcement(announcement, color, timeToLive));
	}

	public void announce(String announcement, float timeToLive) {
		newlyAdded.add(new Announcement(announcement, timeToLive));
	}

	public void announce(String announcement) {
		newlyAdded.add(new Announcement(announcement));
	}

	private void push(Announcement anno) {
		newlyAdded.add(anno);
	}

	public void remove(Announcement anno) {
		active.remove(anno);
	}

	public void announceRoundStart(int currentRound, int secondsToStart) {
		push(new RoundStartAnnouncement(secondsToStart));
		push(new Announcement("Round " + (currentRound + 1)));
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
}
