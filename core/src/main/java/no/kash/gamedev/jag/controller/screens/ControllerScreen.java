package no.kash.gamedev.jag.controller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryonet.Connection;

import aurelienribon.tweenengine.Tween;
import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.defs.Defs;
import no.kash.gamedev.jag.commons.defs.Prefs;
import no.kash.gamedev.jag.commons.network.JagClientPacketHandler;
import no.kash.gamedev.jag.commons.network.NetworkListener;
import no.kash.gamedev.jag.commons.network.packets.GamePacket;
import no.kash.gamedev.jag.commons.network.packets.PlayerInput;
import no.kash.gamedev.jag.commons.network.packets.PlayerStateChange;
import no.kash.gamedev.jag.commons.network.packets.PlayerUpdate;
import no.kash.gamedev.jag.commons.tweens.TweenGlobal;
import no.kash.gamedev.jag.commons.tweens.accessors.ColorAccessor;
import no.kash.gamedev.jag.controller.JustAnotherGameController;
import no.kash.gamedev.jag.controller.hud.InGameHud;
import no.kash.gamedev.jag.controller.input.Joystick;
import no.kash.gamedev.jag.controller.inputschemes.InputEvent;
import no.kash.gamedev.jag.controller.inputschemes.InputScheme;
import no.kash.gamedev.jag.game.commons.utils.Cooldown;

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

	private GlyphLayout deadLabel;

	private Cooldown vibrationCooldown;

	public ControllerScreen(JustAnotherGameController controller) {
		super(controller);
		game.getActionResolver().toast("Making screen " + this);
	}

	@Override
	public void onShow() {

		hideUI();
		hud = new InGameHud(stage, 0, stage.getHeight(), stage.getWidth() / 3, stage.getHeight() / 2);
		bgColor = new Color(Color.GRAY);
		setBackgroundColor(bgColor);

		vibrationCooldown = new Cooldown(0.5f);

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

		deadLabel = new GlyphLayout(Assets.fontLarge, "RIP");

		final InputScheme scheme = new InputScheme() {
			/*
			 * TODO
			 * 
			 * Remove InputScheme and replace with listeners sending packets
			 * manually.
			 * 
			 * Reason: Reload button does not work using the given
			 * eventlisteners, requiring a clicklistener which is not suitable
			 * universally (i.e. for joysticks)
			 * 
			 */
			@Override
			public void handleInputEvent(InputEvent event) {
				switch (event.getId()) {
				case JOYSTICK_LEFT:
					game.getClient().send(new PlayerInput(game.getClient().getId(), JOYSTICK_LEFT,
							new float[] { stick_left.getXValue(), stick_left.getYValue() }));
					break;
				case JOYSTICK_RIGHT:

					game.getClient().send(new PlayerInput(game.getClient().getId(), JOYSTICK_RIGHT,
							new float[] { stick_right.getXValue(), stick_right.getYValue() }));
					break;
				case JOYSTICK_MID:
					game.getClient().send(new PlayerInput(game.getClient().getId(), JOYSTICK_MID,
							new float[] { stick_mid.getXValue(), stick_mid.getYValue() }));
					break;
				case BUTTON_RELOAD:
					game.getClient().send(new PlayerInput(game.getClient().getId(), BUTTON_RELOAD,
							new float[] { event.isReleased() ? 0 : 1 }));
					break;
				default:
					game.getActionResolver().toast("Unknown input: " + event.getId());
				}
			}
		};

		scheme.addInputElement(JOYSTICK_RIGHT, stick_right.getTouchpad());
		scheme.addInputElement(JOYSTICK_LEFT, stick_left.getTouchpad());
		scheme.addInputElement(JOYSTICK_MID, stick_mid.getTouchpad());
		scheme.addInputElement(BUTTON_RELOAD, reload, true);

		game.setReceiver(new JagClientPacketHandler() {

			@Override
			public void handlePacket(Connection c, GamePacket m) {
				if (m instanceof PlayerUpdate) {
					PlayerUpdate pf = (PlayerUpdate) m;
					handlePlayerUpdate(pf);
				}
			}

			@Override
			public void handleDisconnection(Connection c) {
			}

			@Override
			public void handleConnection(Connection c) {
				// TODO Auto-generated method stub

			}
		});

		inputMux.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.A:
					stick_left.overrideXValue(-1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;
				case Keys.D:
					stick_left.overrideXValue(1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;
				case Keys.S:
					stick_left.overrideYValue(-1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;
				case Keys.W:
					stick_left.overrideYValue(1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;

				case Keys.LEFT:
					stick_right.overrideXValue(-1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				case Keys.RIGHT:
					stick_right.overrideXValue(1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				case Keys.DOWN:
					stick_right.overrideYValue(-1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				case Keys.UP:
					stick_right.overrideYValue(1);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				}
				return true;
			}

			@Override
			public boolean keyUp(int keycode) {
				switch (keycode) {
				case Keys.A:
					if (stick_left.getXValue() < 0)
						stick_left.overrideXValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;
				case Keys.D:
					if (stick_left.getXValue() > 0)
						stick_left.overrideXValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;
				case Keys.S:

					if (stick_left.getYValue() < 0)
						stick_left.overrideYValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;
				case Keys.W:
					if (stick_left.getYValue() > 0)
						stick_left.overrideYValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_LEFT, stick_left.getTouchpad(), null));
					break;

				case Keys.LEFT:
					if (stick_right.getXValue() < 0)
						stick_right.overrideXValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				case Keys.RIGHT:
					if (stick_right.getXValue() > 0)
						stick_right.overrideXValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				case Keys.DOWN:
					if (stick_right.getYValue() < 0)
						stick_right.overrideYValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				case Keys.UP:
					if (stick_right.getYValue() > 0)
						stick_right.overrideYValue(0);
					scheme.handleInputEvent(new InputEvent(JOYSTICK_RIGHT, stick_right.getTouchpad(), null));
					break;
				}

				return true;
			}

		});
	}

	public void handlePlayerUpdate(PlayerUpdate m) {
		for (int i = 0; i < m.fields; i++)
			switch (m.fieldId[i]) {
			case PlayerUpdate.FEEDBACK_VIBRATION:
				if (vibrationCooldown.isReady()) {
					vibrationCooldown.start();
					Gdx.input.vibrate((int) m.state[i][0]);
				}
				break;
			case PlayerUpdate.HEALTH:
				int oldHealth = hud.getHealth();
				int newHealth = (int) (m.state[i][0]);
				hud.setHealth(newHealth);

				if (newHealth < oldHealth) {
					bgColor.r = Color.GRAY.r;
					bgColor.g = Color.GRAY.g;
					bgColor.b = Color.GRAY.b;
					bgFadeTween.kill();
					bgFadeTween = newFadeFromTween();
					TweenGlobal.start(bgFadeTween);
				}
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
			case PlayerUpdate.ITEM:
				hud.updateItem((int) m.state[i][0], (int) m.state[i][1]);
				break;
			case PlayerUpdate.KILL_DEATH:
				hud.updateKD((int) m.state[i][0], (int) m.state[i][1]);
				break;
			}
	}

	private void hideUI() {
		stage.unfocusAll();
	}

	@Override
	public void draw(float delta) {
		if (hud.getHealth() > 0) {
			if (stick_left.getTouchpad().getColor().a == 0) {
				stick_left.getTouchpad().setColor(1, 1, 1, 0.25f);
				stick_right.getTouchpad().setColor(1, 1, 1, 0.25f);
				stick_mid.getTouchpad().setColor(1, 1, 1, 0.25f);
			}
			reload.setColor(1, 1, 1, 1);
			hud.draw(batch);
		} else {
			stick_left.getTouchpad().setColor(1, 1, 1, 0);
			stick_right.getTouchpad().setColor(1, 1, 1, 0);
			stick_mid.getTouchpad().setColor(1, 1, 1, 0);
			reload.setColor(1, 1, 1, 0);
			Assets.fontLarge.draw(batch, deadLabel, stage.getWidth() / 2 - deadLabel.width / 2, stage.getHeight() / 2);
			hud.draw(batch);
		}

	}

	@Override
	protected void update(float delta) {
		vibrationCooldown.update(delta);
	}

	private Tween newFadeFromTween() {
		return Tween.from(bgColor, ColorAccessor.TYPE_RGBA, 1.0f).target(1, 0, 0, 1);
	}

}
