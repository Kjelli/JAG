package no.kash.gamedev.jag.game.gameobjects.players.item;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum ItemType {
	
	
	grenade("Default", Assets.grenade, -1,false), tripmine("Tripmine", Assets.tripmine_ground, 3, false);
	
	private Texture texture;
	private int uses;
	private boolean useOnPickup;
	private String displayName;
	
	
	
	private ItemType(String displayName, Texture texture, int uses, boolean useOnPickup){
		this.texture = texture;
		this.uses = uses;
		this.displayName = displayName;
		this.useOnPickup = useOnPickup;
	}

	
	@Override
	public String toString() {
		return displayName;
	}


	public Texture getTexture() {
		return texture;
	}



	public int getUses() {
		return uses;
	}



	public boolean isUseOnPickup() {
		return useOnPickup;
	}
	
	
}
