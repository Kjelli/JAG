package no.kash.gamedev.jag.game.gameobjects.collectables.items;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum ItemType {
	
	

	grenade("Default", Assets.grenade, -1,false, 0),
	healthpack("Healthpack", Assets.healthpack,1,true, 50),
	tripmine("Tripmine", Assets.tripmine_ground, 3, false, 0);

	
	private Texture texture;
	private int uses;
	private boolean useOnPickup;
	private String displayName;
	private float magnitude;

	
	
	
	private ItemType(String displayName, Texture texture, int uses, boolean useOnPickup, float magnitude){
		this.texture = texture;
		this.uses = uses;
		this.displayName = displayName;
		this.useOnPickup = useOnPickup;
		this.magnitude = magnitude;
	}

	
	@Override
	public String toString() {
		return displayName;
	}


	public Texture getTexture() {
		return texture;
	}



	public int getDefaultUses() {
		return uses;
	}



	public boolean isUseOnPickup() {
		return useOnPickup;
	}


	public float getMagnitude() {
		return magnitude;
	}
	
	
	
	
}
