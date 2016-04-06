package no.kash.gamedev.jag.commons.selection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Select;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;

public class SelectOption extends TextButton{
	
	public static final float HEIGHT = (Defs.HEIGHT/4);
	public static final float WIDTH = Defs.HEIGHT/6;
	
	public float x,y;
	
	public Sprite sprite;
	public GlyphLayout nameLabel;
	
	
	public SelectOption(Texture texture, float x, float y) {
		super("",new Skin());
		sprite = new Sprite(texture,(int)WIDTH,(int)HEIGHT);
		this.x = x;
		this.y = y;
		nameLabel = new GlyphLayout(Assets.fontSmall,"HERRO");
		
		setX(x);
		setY(y);
		setWidth(WIDTH);
		setHeight(HEIGHT);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
		Assets.fontSmall.draw(batch,nameLabel,x,y);
	}

}
