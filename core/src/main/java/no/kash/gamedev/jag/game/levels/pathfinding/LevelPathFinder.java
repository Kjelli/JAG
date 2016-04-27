package no.kash.gamedev.jag.game.levels.pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import no.kash.gamedev.jag.game.levels.Level;

public class LevelPathFinder {

	public static IndexedAStarPathFinder<MapNode> pathfinder;
	public static MapNode[][] nodes;
	private static final ManhattanDistanceHeuristic HEURISTIC = new ManhattanDistanceHeuristic();

	public static void build(Level level) {
		nodes = new MapNode[level.width][level.height];

		LevelGraph graph = new LevelGraph(20);

		int index = 0;
		for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {

				TiledMapTileLayer layer = (TiledMapTileLayer) level.map.getLayers().get("obstacles");
				Cell cell = layer.getCell(x, y);
				if (cell == null) {
					nodes[x][y] = new MapNode((x + 0.25f) * level.tileWidth, (y + 0.25f) * level.tileHeight,
							level.tileWidth / 2, index++);
					graph.addNode(nodes[x][y]);
				}

			}
		}

		// Add connection to every neighbour of this node.
		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes[0].length; y++) {
				if (null != nodes[x][y]) {
					addNodeNeighbour(nodes, nodes[x][y], x - 1, y); // Node to
																	// left

					addNodeNeighbour(nodes, nodes[x][y], x + 1, y); // Node to
																	// right

					addNodeNeighbour(nodes, nodes[x][y], x, y - 1); // Node
																	// below

					addNodeNeighbour(nodes, nodes[x][y], x, y + 1); // Node
																	// above
				}
			}
		}

		pathfinder = new IndexedAStarPathFinder<MapNode>(graph, true);

	}

	public static PathResult findPath(int startX, int startY, int endX, int endY) {
		DefaultGraphPath<MapNode> path = new DefaultGraphPath<>();
		path.clear();

		try {
			MapNode start = nodes[startX][startY], end = nodes[endX][endY];

			pathfinder.searchNodePath(start, end, HEURISTIC, path);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		PathResult result = null;
		if (path == null || path.nodes.size == 0) {
			result = new PathResult(false, path);
		} else {
			result = new PathResult(true, path);
		}
		return result;

	}

	private static void addNodeNeighbour(MapNode[][] nodes, MapNode mapNode, int aX, int aY) {
		if (aX >= 0 && aX < nodes.length && aY >= 0 && aY < nodes[0].length) {
			mapNode.addNeighbour(nodes[aX][aY]);
		}
	}

	public static void render(ShapeRenderer renderer) {
		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes[0].length; y++) {
				if (null != nodes[x][y]) {
					nodes[x][y].render(renderer);
				}
			}
		}
	}
}
