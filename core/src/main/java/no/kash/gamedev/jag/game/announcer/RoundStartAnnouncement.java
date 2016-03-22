package no.kash.gamedev.jag.game.announcer;

public class RoundStartAnnouncement extends Announcement {

	float timeToStart;

	public RoundStartAnnouncement(int seconds) {
		super(seconds + "", seconds + 3);
		timeToStart = seconds + 1;
	}

	@Override
	public void update(float delta) {
		timeToStart -= delta;
		if (timeToStart > 1) {
			setText((int) timeToStart + "");
		} else {
			setText("GO!");
		}
	}

}
