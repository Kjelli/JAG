package no.kash.gamedev.jag.game.gameobjects.players.item;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Item {

	private ItemType type;
	
	// If throwable
	private Texture texture;
	private int uses;
	private boolean useOnPickUp;
	
	public Item(ItemType type){
		this.type = type;
		texture = type.getTexture();
		uses = type.getUses();
		useOnPickUp = type.isUseOnPickup();
	}

	public ItemType getType() {
		return type;
	}


	
	
}
