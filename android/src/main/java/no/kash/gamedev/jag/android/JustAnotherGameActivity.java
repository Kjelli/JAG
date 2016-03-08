package no.kash.gamedev.jag.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import no.kash.gamedev.jag.JustAnotherGame;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.controller.JustAnotherGameController;

public class JustAnotherGameActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionResolver aar = new AndroidActionResolver(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new JustAnotherGameController(aar), config);
		
	}
}
