package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.MessageListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.input.Joystick;
import no.kash.gamedev.jag.controller.inputschemes.InputEvent;
import no.kash.gamedev.jag.controller.inputschemes.InputScheme;

public class ControllerScreen extends AbstractControllerScreen {

	public static final int JOYSTICK_LEFT = 1, JOYSTICK_RIGHT = 2, JOYSTICK_MID = 3, BUTTON_RELOAD = 4;

	// Controller layout
	Joystick stick_left;
	Joystick stick_right;
	Joystick stick_mid;
	TextButton reload;

	GlyphLayout statusText;

	boolean connected = false;

	static final float MAX_TIMEOUT = 2.0f;

	float timeout = 0;

	String connectionString;
	
	private InGameHud hud;

	public ControllerScreen(JustAnotherGameController controller) {
		super(controller);
		game.getActionResolver().toast("Making screen " + this);
	}

	@Override
	public void onShow() {
		hideUI();
		hud = new InGameHud(0,stage.getHeight()-stage.getHeight()/2,stage.getWidth()/3,stage.getHeight()/2);
		
		setBackgroundColor(Color.WHITE);

		game.getActionResolver().toast("Initializing controller");

		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		skin.getFont("default-font").getData().scale(0.15f);

		stick_left = new Joystick(0, stage.getHeight() / 20.0f, 90, 40, 10);
		stick_mid = new Joystick(stage.getWidth() / 2 - 60, stage.getHeight() / 2 - 60, 120, 10, 10);
		stick_right = new Joystick(stage.getWidth() - 90, stage.getHeight() / 20.0f, 90, 40, 10);
		reload = new TextButton("Reload", skin);
		reload.setX(stage.getWidth() - reload.getWidth());
		reload.setY(stage.getHeight() - reload.getHeight());

		stage.addActor(stick_left.getTouchpad());
		stage.addActor(stick_right.getTouchpad());
		stage.addActor(stick_mid.getTouchpad());
		stage.addActor(reload);

		InputScheme scheme = new InputScheme() {

			@Override
			protected void handleEvent(InputEvent event) {
				switch (event.getId()) {
				case JOYSTICK_LEFT:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK_LEFT,
							new float[] { stick_left.getXValue(), stick_left.getYValue() }));
					break;
				case JOYSTICK_RIGHT:

					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK_RIGHT,
							new float[] { stick_right.getXValue(), stick_right.getYValue() }));
					break;
				case JOYSTICK_MID:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK_MID,
							new float[] { stick_mid.getXValue(), stick_mid.getYValue() }));
					break;
				case BUTTON_RELOAD:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), BUTTON_RELOAD,
							new float[] { reload.isPressed() ? 1 : 0 }));
					break;
				default:
					game.getActionResolver().toast("Unknown input: " + event.getId());
				}
			}
		};

		scheme.addInputElement(JOYSTICK_RIGHT, stick_right.getTouchpad());
		scheme.addInputElement(JOYSTICK_LEFT, stick_left.getTouchpad());
		scheme.addInputElement(JOYSTICK_MID, stick_mid.getTouchpad());
		scheme.addInputElement(BUTTON_RELOAD, reload);

		game.getClient().setListener(new MessageListener() {

			@Override
			public void onMessage(Connection c, GamePacket m) {
				if (m instanceof PlayerUpdate) {
					PlayerUpdate pf = (PlayerUpdate) m;
					handlePlayerUpdate(pf);
				}
			}
			
			

			@Override
			public void onDisconnection(Connection connection) {
				nextScreen = new LoadingScreen(game, "Connection lost, retrying...");
			}

			@Override
			public void onConnection(Connection c) {
			}
		});

		game.getClient().broadcast(new PlayerStateChangeResponse(JustAnotherGameController.PLAY_STATE));
	}
	
	public void handlePlayerUpdate(PlayerUpdate m){
		if(m.feedbackId == PlayerUpdate.FEEDBACK_VIBRATION){
			Gdx.input.vibrate((int) m.state[0]);
		}
		if(m.feedbackId == PlayerUpdate.HEALTH){
			hud.setHealth((int)m.state[0]);
		}
	}

	private void hideUI() {
		stage.unfocusAll();
	}

	@Override
	public void draw(float delta) {
		stick_left.getTouchpad().draw(batch, 1.0f);
		stick_right.getTouchpad().draw(batch, 1.0f);
		stick_mid.getTouchpad().draw(batch, 1.0f);
		reload.draw(batch, 1.0f);
		hud.draw(batch);
		
	}

	@Override
	protected void update(float delta) {
	}

}
