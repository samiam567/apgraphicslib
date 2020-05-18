package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector;

public class UpNote extends Note {
	
	public UpNote(Song parent, double y, double noteSpeed, String textureSrc) {
		super(parent, 3*Settings.width/5, y, noteSpeed, textureSrc);
		
	}
	
	@Override
	public void load() {
		if (! loaded) {
			super.load();
			rotatePoints(new Vector(Math.PI));
		}
	}

}
