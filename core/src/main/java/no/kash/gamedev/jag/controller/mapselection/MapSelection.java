package no.kash.gamedev.jag.controller.mapselection;

import com.badlogic.gdx.graphics.Texture;

import no.kash.gamedev.jag.assets.Assets;
import no.kash.gamedev.jag.commons.utils.Callback;
import no.kash.gamedev.jag.commons.utils.selection.SelectOption;

public class MapSelection extends SelectOption {
	public int index;

	public MapSelection(int i, Callback onClick) {
		super(Assets.card_square_bg, "Map " + i, onClick);
		this.index = i;
	}

}
