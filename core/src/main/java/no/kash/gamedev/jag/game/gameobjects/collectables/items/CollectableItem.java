package no.kash.gamedev.jag.game.gameobjects.collectables.items;

import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.game.gameobjects.collectables.AbstractCollectable;
import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class CollectableItem extends AbstractCollectable{

	
	public Item item;
	
	public CollectableItem(float x, float y, Item item) {
		super(x, y, 32,32 );
		this.item = item;
		
		Sprite sprite = new Sprite(item.getTexture());
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
	
	}
	
	@Override
	public void onSpawn() {
		getGameContext().bringToFront(this);
	}

	@Override
	public boolean isCollected() {
		return !isAlive();
	}

}
