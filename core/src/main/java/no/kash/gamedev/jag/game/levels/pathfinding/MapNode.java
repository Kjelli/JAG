package no.kash.gamedev.jag.game.levels.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class MapNode implements IndexedNode<MapNode> {

	static final int SPACE_BETWEEN_TILES = 0;

	Array<Connection<MapNode>> mConnections = new Array<Connection<MapNode>>();
	/** mIndex needs to be unique for every node starting from 0 */
	public final int mIndex;
	/** X pos of node. */
	public final float mX;
	/** Y pos of node. */
	public final float mY;
	/** Whether or not this tile is in the path */
	private boolean mSelected;

	public final float tileSize;

	/** aIndex needs to be unique */
	public MapNode(float aX, float aY, float tileSize, int aIndex) {
		mIndex = aIndex;
		mX = aX;
		mY = aY;
		this.tileSize = tileSize;
	}

	@Override
	public Array<Connection<MapNode>> getConnections() {
		return mConnections;
	}

	@Override
	public int getIndex() {
		return mIndex;
	}

	public void addNeighbour(MapNode aNode) {
		if (aNode != null) {
			mConnections.add(new DefaultConnection<MapNode>(this, aNode));
		}
	}

	public void select() {
		mSelected = true;
	}

	public void render(ShapeRenderer aShapeRenderer) {
		if (mSelected) {
			aShapeRenderer.setColor(Color.RED);
		} else {
			aShapeRenderer.setColor(Color.WHITE);
		}
		aShapeRenderer.line(mX, mY, mX, mY + tileSize - SPACE_BETWEEN_TILES);
		aShapeRenderer.line(mX, mY, mX + tileSize - SPACE_BETWEEN_TILES, mY);
		aShapeRenderer.line(mX, mY + tileSize - SPACE_BETWEEN_TILES, mX + tileSize - SPACE_BETWEEN_TILES,
				mY + tileSize - SPACE_BETWEEN_TILES);
		aShapeRenderer.line(mX + tileSize - SPACE_BETWEEN_TILES, mY, mX + tileSize - SPACE_BETWEEN_TILES,
				mY + tileSize - SPACE_BETWEEN_TILES);
	}

	public String toString() {
		return String.format("Index:%d x:%f y:%f", mIndex, mX, mY);
	}

}
