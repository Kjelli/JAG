package no.kash.gamedev.jag.game.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import no.kash.gamedev.jag.assets.Assets;

public class Card {
	
	protected CardTypes card;
	protected Sprite border;
	
	public Card(CardTypes card){
		this.card = card;
		border = new Sprite(Assets.icon_border);
	} 
	
	
}
