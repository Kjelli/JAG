package no.kash.gamedev.jag.game.levels.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ManhattanDistanceHeuristic implements Heuristic<MapNode> {

	@Override
	public float estimate(MapNode node, MapNode endNode) {
		return Math.abs(endNode.mX - node.mX) + Math.abs(endNode.mY - node.mY);
	}

}