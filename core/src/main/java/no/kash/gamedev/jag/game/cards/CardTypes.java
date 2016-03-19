package no.kash.gamedev.jag.game.cards;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;

public enum CardTypes {

	health("Juggernaut","increases health by x",Assets.health_icon);
	
	private String name;
	private String description;
	private Texture icon;
	
	CardTypes(String name, String description, Texture icon){
		this.name = name;
		this.description = description;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Texture getIcon() {
		return icon;
	}
	
	
}
