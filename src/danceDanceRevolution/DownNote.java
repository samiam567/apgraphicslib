package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class DownNote extends Note {

	public DownNote(Object_draw drawer, double y, double noteSpeed, String textureSrc) {
		super(drawer, 2*Settings.width/5, y, noteSpeed, textureSrc);
		
	}
	

}
