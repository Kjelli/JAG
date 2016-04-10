package no.kash.gamedev.jag.game.gameobjects.players.item;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.game.gameobjects.collectables.AbstractCollectable;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class CollectableItem extends AbstractCollectable{

	
	public ItemType type;
	
	
	
	public CollectableItem(float x, float y, ItemType type) {
		super(x, y, 32,32 );
		this.type = type;
		
		Sprite sprite = new Sprite(type.getTexture());
		setSprite(sprite);
	}
	
	@Override
	public void collect(Player player) {
		super.collect(player);
		player.pickUpItem(this);
		destroy();
	}

	@Override
	public boolean canCollect(Player player) {
		return true;
	}

	@Override
	public void update(float delta) {
		setScale((float) (Math.sin(getGameContext().getElapsedTime() * 5.0f)) * 0.1f + 1.0f);
		setX(getX() - getWidth() / 2);
		setY(getY() - getHeight() / 2);
	}
	
	@Override
	public void onSpawn() {
		getGameContext().bringToFront(this);
	}

}
