package no.kash.gamedev.jag.android;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
		// Toast.makeText(app, text, 1000);
		Log.i("JustAnotherGame", text);
	}

	@Override
	public void immersiveFullscreen() {
		app.requestWindowFeature(Window.FEATURE_NO_TITLE);
		app.getWindow().getDecorView()
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}
}
