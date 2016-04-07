package no.kash.gamedev.jag.game.announcer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.commons.tweens.accessors.Vector2Accessor;

public class Announcement {
	public static final BitmapFont font = Assets.announcerFont;

	public static final float FADE_AWAY_TIME = 0.1f;
	public static final float FADE_IN_TIME = 0.1f;

	final float timeToLive;
	float timer;
	String text;
	Color color;

	GlyphLayout glyphLayout;

	private Announcer announcer;

	private Vector2 offset;

	private boolean isDestroyed;

	public Announcement(String text) {
		this(text, 3.0f);
	}

	public Announcement(String text, float timeToLive) {
		this(text, Color.WHITE, timeToLive);
	}

	public Announcement(String text, Color textColor, float timeToLive) {
		offset = new Vector2();
		this.text = text;
		this.color = new Color(textColor);
		this.glyphLayout = new GlyphLayout(font, text, color, -1, -1, false);
		this.timeToLive = timeToLive;
		timer = timeToLive;
		TweenGlobal.start(Tween.from(offset, Vector2Accessor.TYPE_X, 0.3f).target(-Gdx.graphics.getWidth())
				.ease(TweenEquations.easeOutExpo));
	}

	public void draw(float x, float y, SpriteBatch batch) {
		font.draw(batch, text, x - glyphLayout.width / 2 + offset.x, y - glyphLayout.height / 2 + offset.y);
	}

	public void update(float delta) {
	}

	public void setText(String text) {
		this.text = text;
		updateGlyphs();
	}

	public String getText() {
		return text;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public float getHeight() {
		return glyphLayout.height * font.getData().scaleY;
	}

	public float getWidth() {
		return glyphLayout.width * font.getData().scaleX;
	}

	public void setColor(Color color) {
		this.color = color;
		updateGlyphs();
	}

	public Color getColor() {
		return color;
	}

	public void setAnnouncer(Announcer announcer) {
		this.announcer = announcer;
	}

	private void updateGlyphs() {
		glyphLayout.setText(font, text, color, -1, Align.center, true);
	}

	public void destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			TweenGlobal.start(Tween.to(offset, Vector2Accessor.TYPE_X, FADE_AWAY_TIME)
					.target(Gdx.graphics.getWidth() + getWidth()).ease(TweenEquations.easeInExpo)
					.setCallback(new TweenCallback() {
						public void onEvent(int arg0, BaseTween<?> arg1) {
							if (arg0 == TweenCallback.COMPLETE) {
								announcer.remove(Announcement.this);
							}
						}
					}));
		}
	}

}
