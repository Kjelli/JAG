package no.kash.gamedev.jag.commons.utils.selection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.utils.Callback;

public class SelectOption extends TextButton {

	public float x, y;

	public Sprite sprite;
	public GlyphLayout nameLabel;
	public Callback onClick;

	public SelectOption(Texture texture, String label, Callback onClick) {
		super("", new Skin(Gdx.files.internal("uiskin.json")));
		sprite = new Sprite(texture, (int) texture.getWidth(), (int) texture.getHeight());
		nameLabel = new GlyphLayout(Assets.fontSmall, label);
		this.onClick = onClick;

		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SelectOption.this.onClick.callback();
			}
		});
	}

	public void setSize(float x, float y, float width, float height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
		Assets.fontSmall.draw(batch, nameLabel, x + sprite.getWidth() / 2 - nameLabel.width / 2, y);
	}

	@Override
	public void setX(float x) {
		super.setX(x);
		this.x = x;
		sprite.setX(x);
	}

	@Override
	public void setY(float y) {
		super.setY(y);
		this.y = y;
		sprite.setY(y);
	}

	@Override
	public void setWidth(float width) {
		super.setWidth(width);
		sprite.setSize(width, sprite.getHeight());
	}

	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		sprite.setSize(sprite.getWidth(), height);
	}

}
