package no.kash.gamedev.jag.controller.mapselection;

import no.kash.gamedev.jag.commons.utils.Callback;
import no.kash.gamedev.jag.commons.utils.selection.Selector;
import no.kash.gamedev.jag.controller.screens.MapSelectionControllerScreen;

public class MapSelector extends Selector<MapSelection> {

	MapSelectionControllerScreen screen;

	public MapSelector(MapSelectionControllerScreen screen, float x, float y) {
		super(x, y, 50f);
		this.screen = screen;
	}

	public void setAvailableMaps(int count) {
		for (int i = 0; i < count; i++) {
			final int index = i;
			add(new MapSelection(i, new Callback() {
				@Override
				public void callback() {
					screen.select(index);
				}
			}));
		}
	}
	
	@Override
	public float getWidth() {
		float width = 0;
		for(MapSelection s : options){
			width += s.getWidth() + spacing;
		}
		return width - spacing;
	}

}
