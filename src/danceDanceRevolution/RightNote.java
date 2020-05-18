package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector;

public class RightNote extends Note {
	
	public RightNote(Song parent, double y, double noteSpeed, String textureSrc) {
		super(parent,  4*Settings.width/5, y, noteSpeed, textureSrc);
		
	}
	
	@Override
	public void load() {
		if (! loaded) {
			super.load();
			rotatePoints(new Vector(-Math.PI/2));
		}
	}
}
