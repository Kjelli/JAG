package no.kash.gamedev.jag.android;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import no.kash.gamedev.jag.actionresolvers.ActionResolver;

public class AndroidActionResolver implements ActionResolver {
	private AndroidApplication app;

	public AndroidActionResolver(AndroidApplication app) {
		this.app = app;
	}

	@Override
	public void toast(String text) {
		// DOESNT WOROOOORORORORKRKKK 
		//Toast.makeText(app, text, 1000);
		Log.i("JustAnotherGame", text);
	}
}
