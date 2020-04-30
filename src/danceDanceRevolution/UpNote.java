package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector;

public class UpNote extends Note {

	private static double upNoteXPos = 3*Settings.width/5;
	
	public UpNote(Object_draw drawer, double y, double noteSpeed, String textureSrc) {
		super(drawer, upNoteXPos, y, noteSpeed, textureSrc);
		
	}
	
	@Override
	public void load() {
		if (! loaded) {
			super.load();
			rotatePoints(new Vector(Math.PI));
		}
	}

}
