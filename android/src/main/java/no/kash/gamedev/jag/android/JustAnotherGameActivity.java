package no.kash.gamedev.jag.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import no.kash.gamedev.jag.JustAnotherGame;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.controller.JustAnotherGameController;

public class JustAnotherGameActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionResolver aar = new AndroidActionResolver(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		initialize(new JustAnotherGameController(aar), config);
	}
}
