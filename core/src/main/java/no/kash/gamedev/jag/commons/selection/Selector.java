package no.kash.gamedev.jag.commons.selection;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Selector {
	
	private SelectOption s1,s2,s3;
	private ArrayList<SelectOption> choices;
	
	public Selector(Texture t1, Texture t2, Texture t3){
		choices = new ArrayList<SelectOption>();
		
		s1 = new SelectOption(t1,0,0);
		choices.add(s1);
		
		s2 = new SelectOption(t2,t1.getWidth(),0);
		choices.add(s2);
		
		s3 = new SelectOption(t3,t1.getWidth()+t2.getWidth(),0);
		choices.add(s3);
		
		generate();
	}

	private void generate() {
		for(SelectOption choices:choices){
			choices.addListener(new ClickListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					// TODO Auto-generated method stub
					return super.touchDown(event, x, y, pointer, button);
				}
			});
		}
	}
	
	
	
}
