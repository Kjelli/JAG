package no.kash.gamedev.jag.game.gameobjects.collectables.items;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.game.gameobjects.players.Player;

public class Item {

	private ItemType type;

	private Texture texture;
	private int uses;
	private boolean useOnPickUp;

	public Item(ItemType type) {
		this(type, type.getDefaultUses());
	}

	public Item(ItemType type, int uses) {
		this.type = type;
		this.uses = uses;
		texture = type.getTexture();
		useOnPickUp = type.isUseOnPickup();
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}

	public boolean isSpent() {
		return uses == 0;
	}

	public boolean isUseOnPickUp() {
		return useOnPickUp;
	}

	public ItemType getType() {
		return type;
	}

	public void decrementUses() {
		if (uses > 0) {
			uses = Math.max(uses - 1, 0);
		}
	}

}
