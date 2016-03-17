package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryonet.Connection;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChangeResponse;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.hud.InGameHud;
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

	private InGameHud hud;

	private Tween bgFadeTween = newFadeFromTween();

	private Color bgColor;

	public ControllerScreen(JustAnotherGameController controller) {
		super(controller);
		game.getActionResolver().toast("Making screen " + this);
	}

	@Override
	public void onShow() {
		hideUI();
		hud = new InGameHud(0, stage.getHeight(), stage.getWidth() / 3, stage.getHeight() / 2);
		bgColor = new Color(Color.GRAY);
		setBackgroundColor(bgColor);

		game.getActionResolver().toast("Initializing controller");

		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		skin.getFont("default-font").getData().scale(0.15f);

		float joystickLRPadSize = stage.getWidth() * 0.3f;
		float joystickLRKnobSize = joystickLRPadSize * 0.3f;
		float joystickMPadSize = stage.getWidth() * 0.4f;
		float joystickMKnobSize = joystickMPadSize * 0.10f;
		stick_left = new Joystick(0, stage.getHeight() / 20.0f, joystickLRPadSize, joystickLRKnobSize, 10);
		stick_mid = new Joystick(stage.getWidth() / 2 - joystickMPadSize / 2,
				stage.getHeight() / 2 - joystickMPadSize / 2, joystickMPadSize, joystickMKnobSize, 10);
		stick_right = new Joystick(stage.getWidth() - joystickLRPadSize, stage.getHeight() / 20.0f, joystickLRPadSize,
				joystickLRPadSize / 3, 10);
		reload = new TextButton("Reload", skin);
		reload.setWidth(stage.getWidth() / 4);
		reload.setHeight(stage.getWidth() / 6);
		reload.setX(stage.getWidth() - reload.getWidth());
		reload.setY(stage.getHeight() - reload.getHeight());

		stage.addActor(stick_left.getTouchpad());
		stage.addActor(stick_right.getTouchpad());
		stage.addActor(stick_mid.getTouchpad());
		stage.addActor(reload);

		InputScheme scheme = new InputScheme() {

			@Override
			protected void handleInputEvent(InputEvent event) {
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

		game.getClient().setListener(new NetworkListener() {

			@Override
			public void receivedPacket(Connection c, GamePacket m) {
				if (m instanceof PlayerUpdate) {
					PlayerUpdate pf = (PlayerUpdate) m;
					handlePlayerUpdate(pf);
				}
			}

			@Override
			public void disconnected(Connection connection) {
				queueNextScreen(new LoadingScreen(game, "Connection lost, retrying..."));
			}

			@Override
			public void connected(Connection c) {
			}
		});
	}

	public void handlePlayerUpdate(PlayerUpdate m) {
		for (int i = 0; i < m.fields; i++)
			switch (m.fieldId[i]) {
			case PlayerUpdate.FEEDBACK_VIBRATION:
				Gdx.input.vibrate((int) m.state[i][0]);
				break;
			case PlayerUpdate.HEALTH:
				hud.setHealth((int) m.state[i][0]);

				bgColor.r = Color.GRAY.r;
				bgColor.g = Color.GRAY.g;
				bgColor.b = Color.GRAY.b;
				bgFadeTween.kill();
				bgFadeTween = newFadeFromTween();
				TweenGlobal.start(bgFadeTween);
				break;
			case PlayerUpdate.AMMO:
				int magAmmo = (int) m.state[i][0];
				int magSize = (int) m.state[i][1];
				int ammo = (int) m.state[i][2];
				hud.updateAmmo(magAmmo, magSize, ammo);
				break;
			case PlayerUpdate.GUN:
				hud.updateGun((int) m.state[i][0]);
				break;
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

	private Tween newFadeFromTween() {
		return Tween.from(bgColor, ColorAccessor.TYPE_RGBA, 1.0f).target(1, 0, 0, 1);
	}

}
