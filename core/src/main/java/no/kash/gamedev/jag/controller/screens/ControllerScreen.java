package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.MessageListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.input.Joystick;
import no.kash.gamedev.jag.controller.inputschemes.InputEvent;
import no.kash.gamedev.jag.controller.inputschemes.InputScheme;

public class ControllerScreen extends AbstractControllerScreen {

	public static final int JOYSTICK_LEFT = 1, JOYSTICK_RIGHT = 2, JOYSTICK_MID = 3;

	Joystick stick_left;
	Joystick stick_right;
	Joystick stick_mid;

	GlyphLayout statusText;

	boolean connected = false;

	static final float MAX_TIMEOUT = 2.0f;

	float timeout = 0;

	public ControllerScreen(JustAnotherGameController controller) {
		super(controller);
	}

	@Override
	public void draw(float delta) {
		stick_left.getTouchpad().draw(batch, 1.0f);
		stick_right.getTouchpad().draw(batch, 1.0f);
		stick_mid.getTouchpad().draw(batch, 1.0f);

	}

	@Override
	public void onShow() {
		game.getActionResolver().toast("Initializing controller");

		setBackgroundColor(Color.RED);

		stick_left = new Joystick(0, Gdx.graphics.getHeight() / 20.0f, 10);
		stick_right = new Joystick(Gdx.graphics.getWidth() - Joystick.WIDTH, Gdx.graphics.getHeight() / 20.0f, 10);
		stick_mid = new Joystick(Gdx.graphics.getWidth() / 2 - Joystick.WIDTH / 2,
				Gdx.graphics.getHeight() / 2 - Joystick.HEIGHT / 2, 10);

		stage.addActor(stick_left.getTouchpad());
		stage.addActor(stick_right.getTouchpad());
		stage.addActor(stick_mid.getTouchpad());

		InputScheme scheme = new InputScheme() {

			@Override
			protected void handleEvent(InputEvent event) {
				switch (event.getId()) {
				case JOYSTICK_LEFT:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK_LEFT,
							new float[] { stick_left.getXValue(), stick_left.getYValue() }));
					break;
				case JOYSTICK_RIGHT:
					if (stick_right.getXValue() == 0 || stick_right.getYValue() == 0) {
						return;
					}
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK_RIGHT,
							new float[] { stick_right.getXValue(), stick_right.getYValue() }));
					break;
				case JOYSTICK_MID:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK_MID,
							new float[] { stick_mid.getXValue(), stick_mid.getYValue() }));
					break;
				default:
					game.getActionResolver().toast("Unknown input: " + event.getId());
				}
			}
		};

		scheme.addInputElement(JOYSTICK_RIGHT, stick_right.getTouchpad());
		scheme.addInputElement(JOYSTICK_LEFT, stick_left.getTouchpad());
		scheme.addInputElement(JOYSTICK_MID, stick_mid.getTouchpad());

		game.getClient().setListener(new MessageListener() {

			@Override
			public void onMessage(Connection c, GamePacket m) {
				System.out.println("Message received: " + m);
			}

			@Override
			public void onDisconnection(Connection connection) {
				setBackgroundColor(Color.RED);
				connected = false;
			}

			@Override
			public void onConnection(Connection c) {
				setBackgroundColor(Color.WHITE);
				connected = true;
			}
		});
	}

	@Override
	protected void update(float delta) {
		if ((timeout -= delta) < 0 && !connected) {
			try {
				game.getClient().connect("152.94.125.165", 13337);
				game.getActionResolver().toast("Connection made!! :D");
			} catch (Exception e) {
				game.getActionResolver().toast("Could not connect: " + e.getMessage());
				timeout = MAX_TIMEOUT;
			}
		}
	}
}
