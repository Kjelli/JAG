package no.kash.gamedev.jag.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import no.kash.gamedev.jag.JustAnotherGame;

public class JustAnotherGameHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new JustAnotherGame();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
