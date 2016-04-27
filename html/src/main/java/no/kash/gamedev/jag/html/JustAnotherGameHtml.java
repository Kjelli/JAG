package no.kash.gamedev.jag.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import no.kash.gamedev.jag.actionresolvers.ActionResolver;
import no.kash.gamedev.jag.game.JustAnotherGame;

public class JustAnotherGameHtml extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(480, 320);
	}

	@Override
	public ApplicationListener createApplicationListener() {
		ActionResolver rs = new ActionResolver() {
			
			@Override
			public void toast(String text) {
				// Do nothing
			}
		};
		return new JustAnotherGame(rs);
	}
}
