package no.kash.gamedev.jag;

import com.badlogic.gdx.Game;

import no.kash.gamedev.jag.actionresolvers.ActionResolver;

public abstract class JagEndpoint extends Game {
	ActionResolver resolver;

	public JagEndpoint(ActionResolver resolver) {
		this.resolver = resolver;
	}

	public ActionResolver getActionResolver() {
		return resolver;
	}

}
