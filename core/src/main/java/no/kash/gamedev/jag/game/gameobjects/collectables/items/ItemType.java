package no.kash.gamedev.jag.game.gameobjects.collectables.items;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum ItemType {
	
	

	grenade("Default", Assets.grenade, -1,false, 0,0f,1),
	healthpack("Healthpack", Assets.healthpack,1,true, 35,0.1f,1),
	tripmine("Tripmine", Assets.tripmine_ground, 3, false, 0,0.1f,1),
	snakebite("Snakebite",Assets.snakebite,2,false,8,0.1f,1);

	
	private Texture texture;
	private int uses;
	private boolean useOnPickup;
	private String displayName;
	private float magnitude;
	
	private float probability;
	private int tier;

	
	
	
	private ItemType(String displayName, Texture texture, int uses, boolean useOnPickup, float magnitude,
			float probablity, int tier){
		this.texture = texture;
		this.uses = uses;
		this.displayName = displayName;
		this.useOnPickup = useOnPickup;
		this.magnitude = magnitude;
		
		this.probability = probablity;
		this.tier = tier;
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


	public float getProbability() {
		return probability;
	}


	public int getTier() {
		return tier;
	}


	
	
}
