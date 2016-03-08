package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryonet.Connection;

import no.kash.gamedev.jag.commons.network.MessageListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.input.Joystick;
import no.kash.gamedev.jag.controller.inputschemes.InputEvent;
import no.kash.gamedev.jag.controller.inputschemes.InputScheme;

public class ControllerScreen extends AbstractControllerScreen {

	public static final int JOYSTICK = 1, BUTTON = 2;

	Joystick stick;
	TextButton button;

	GlyphLayout statusText;

	boolean connected = false;

	static final float MAX_TIMEOUT = 2.0f;

	float timeout = 0;

	public ControllerScreen(JustAnotherGameController controller) {
		super(controller);
	}

	@Override
	public void draw(float delta) {
		stick.getTouchpad().draw(batch, 1.0f);
		button.draw(batch, 1.0f);

	}

	@Override
	public void onShow() {
		game.getActionResolver().toast("Initializing controller");

		setBackgroundColor(Color.RED);
		Skin someSkin = new Skin(Gdx.files.internal("uiskin.json"));

		stick = new Joystick(Gdx.graphics.getWidth() / 20, Gdx.graphics.getHeight() / 20, 10);

		stage.addActor(stick.getTouchpad());

		button = new TextButton("A", someSkin, "default");

		button.setWidth(300);
		button.setHeight(300);
		((TextButton) button).getSkin().getFont("default-font").getData().setScale(8);

		button.setX(Gdx.graphics.getWidth() * 8 / 10);
		button.setY(Gdx.graphics.getHeight() * 1 / 10);

		stage.addActor(button);

		InputScheme scheme = new InputScheme() {

			@Override
			protected void handleEvent(InputEvent event) {
				switch (event.getId()) {
				case BUTTON:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), BUTTON,
							new float[] { button.isPressed() ? 1 : 0 }));
					break;
				case JOYSTICK:
					game.getClient().broadcast(new PlayerInput(game.getClient().getId(), JOYSTICK,
							new float[] { stick.getXValue(), stick.getYValue() }));
					break;
				default:
					game.getActionResolver().toast("Unknown input: " + event.getId());
				}
			}
		};

		scheme.addInputElement(BUTTON, button);
		scheme.addInputElement(JOYSTICK, stick.getTouchpad());

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
				game.getClient().connect("152.94.124.196", 13337);
				game.getActionResolver().toast("Connection made!! :D");
			} catch (Exception e) {
				game.getActionResolver().toast("Could not connect: " + e.getMessage());
				timeout = MAX_TIMEOUT;
			}
		}
	}
}
