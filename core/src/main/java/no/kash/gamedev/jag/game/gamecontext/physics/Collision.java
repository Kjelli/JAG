package no.kash.gamedev.jag.game.gamecontext.physics;

public class Collision {
	public static final int UP = 1;
	public static final int BELOW = 2;
	public static final int LEFT = 4;
	public static final int RIGHT = 8;

	Collidable source;
	Collidable target;
	int impactDirection;

	public Collision(Collidable source, Collidable target) {
		this.source = source;
		this.target = target;
		impactDirection = 0;
		boolean above = target.contains(source.position().x + 1, source.position().y + source.getHeight() + 1)
				|| target.contains(source.position().x + source.getWidth() - 1, source.position().y + source.getHeight() + 1);

		boolean below = target.contains(source.position().x + 1, source.position().y - 1)
				|| target.contains(source.position().x + source.getWidth() - 1, source.position().y - 1);

		boolean right = target.contains(source.position().x + source.getWidth() + 1, source.position().y + 1)
				|| target.contains(source.position().x + source.getWidth() + 1, source.position().y + source.getHeight() - 1);

		boolean left = target.contains(source.position().x - 1, source.position().y + 1)
				|| target.contains(source.position().x - 1, source.position().y + source.getHeight() - 1);
		if (above)
			impactDirection += UP;
		if (below)
			impactDirection += BELOW;
		if (right)
			impactDirection += RIGHT;
		if (left)
			impactDirection += LEFT;
	}

	public Collidable getSource() {
		return source;
	}

	public Collidable getTarget() {
		return target;
	}

	public int getImpactDirection() {
		return impactDirection;
	}

	public String toString() {
		return "Collision between " + source + " and " + target + " direction: "
				+ (((impactDirection & UP) > 0 ? "ABOVE " : "") + ((impactDirection & LEFT) > 0 ? "LEFT " : "")
						+ ((impactDirection & BELOW) > 0 ? "BELOW " : "")
						+ ((impactDirection & RIGHT) > 0 ? "RIGHT " : ""));
	}
}