package no.kash.gamedev.jag.game.gameobjects.players.ai;

import java.awt.Point;

import no.kash.gamedev.jag.game.gameobjects.GameObject;

public class Goal implements Comparable<Goal> {
	public final GameObject target;
	public final float priority;
	public final Point targetPoint;
	public final boolean objectTarget;
	public boolean evasive;

	public Goal(GameObject target, float priority, boolean evasive) {
		this.target = target;
		this.priority = priority;
		this.targetPoint = null;
		this.objectTarget = true;
		this.evasive = evasive;
	}

	public Goal(Point targetPoint, float priority, boolean evasive) {
		this.targetPoint = targetPoint;
		this.priority = priority;
		this.target = null;
		this.objectTarget = false;
		this.evasive = evasive;
	}

	@Override
	public int compareTo(Goal that) {
		return -Float.compare(this.priority, that.priority);
	}
}
