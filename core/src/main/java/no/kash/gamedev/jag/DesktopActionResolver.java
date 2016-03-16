package no.kash.gamedev.jag;

import no.kash.gamedev.jag.actionresolvers.ActionResolver;

public class DesktopActionResolver implements ActionResolver {

	@Override
	public void toast(String text) {
		System.out.println(text);
	}


}
