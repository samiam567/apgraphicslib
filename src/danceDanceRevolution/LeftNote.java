package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector;

public class LeftNote extends Note {

	private static double leftNoteXPos = Settings.width/5;
	public LeftNote(Object_draw drawer, double y, double noteSpeed, String textureSrc) {
		super(drawer, leftNoteXPos, y, noteSpeed, textureSrc);
		
	}
	
	@Override
	public void load() {
		if (! loaded) {
			super.load();
			rotatePoints(new Vector(Math.PI/2));
		}
	}
}
