package no.kash.gamedev.jag.game.levels.pathfinding;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;

public class LevelGraph extends DefaultIndexedGraph<MapNode> {

	/**
	 * @param aSize
	 *            Just an estimate of size. Will grow later if needed.
	 */
	public LevelGraph(int aSize) {
		super(aSize);
	}

	public void addNode(MapNode aNodes) {
		nodes.add(aNodes);
	}

	public MapNode getNode(int aIndex) {
		return nodes.get(aIndex);
	}
}
