package no.kash.gamedev.jag.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.game.JustAnotherGame;
import no.kash.gamedev.jag.game.gamecontext.GameContext;

public abstract class AbstractGameScreen implements Screen {

	protected abstract void update(float delta);

	protected abstract void draw(float delta);

	protected abstract void onShow();

	protected final JustAnotherGame game;

	protected final OrthographicCamera camera;
	protected final Stage stage;
	protected GameContext gameContext;
	protected final InputMultiplexer inputMux;

	protected final SpriteBatch batch;

	private Texture background;

	private Color bgcolor;

	private boolean paused;

	public AbstractGameScreen(JustAnotherGame game) {
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new ScalingViewport(Scaling.fill, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
		batch = new SpriteBatch();
		gameContext = new GameContext(game);
		inputMux = new InputMultiplexer(stage);
	}

	@Override
	public void render(float delta) {
		update(delta);
		stage.act(delta);
		game.getServer().update(delta);

		Gdx.gl.glClearColor(0.0f, 0.1f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw(delta);
		batch.end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		onShow();
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", this.getClass().getSimpleName(), this.hashCode());
	}

	/**
	 * Game regains focus. (Unpause?)
	 */

	@Override
	public void resume() {
		setPaused(false);
	}

	/**
	 * Hiding the game triggers this method.
	 */
	@Override
	public void hide() {
	}

	/**
	 * Pausing the game triggers this method.
	 */

	@Override
	public void pause() {
		// setPaused(true);
	}

	/**
	 * Dispose of resources used, ie textures and sounds.
	 */
	@Override
	public void dispose() {
		stage.dispose();
		batch.dispose();
		gameContext.dispose();
		Gdx.input.setInputProcessor(null);
	}

	protected final void setBackground(Texture background) {
		this.background = background;
	}

	protected final Texture getBackground() {
		return background;
	}

	public void setBackgroundColor(Color bgcolor) {
		this.bgcolor = bgcolor;
	}

	public Color getBackgroundColor() {
		return bgcolor;
	}

	protected final void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}

	public boolean getPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
		gameContext.setPaused(paused);
	}

	public GameContext getGameContext() {
		return gameContext;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	

	public JustAnotherGame getGame() {
		return game;
	}

}
