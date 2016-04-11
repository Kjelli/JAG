package no.kash.gamedev.jag.game.levels.pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PathResult {
	public final boolean success;
	public final DefaultGraphPath<MapNode> path;

	public PathResult(boolean success, DefaultGraphPath<MapNode> path) {
		this.success = success;
		this.path = path;
	}

	public void render(ShapeRenderer renderer) {
		renderer.end();
		renderer.begin(ShapeRenderer.ShapeType.Line);
		for (MapNode node : path) {
			if (node != null) {
				node.render(renderer);
			}
		}
		renderer.end();
		renderer.begin();
	}
}
