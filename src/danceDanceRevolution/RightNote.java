package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector;

public class RightNote extends Note {
	
	private static double rightNoteXPos = 4*Settings.width/5;
	public RightNote(Object_draw drawer, double y, double noteSpeed, String textureSrc) {
		super(drawer, rightNoteXPos, y, noteSpeed, textureSrc);
		
	}
	
	@Override
	public void load() {
		if (! loaded) {
			super.load();
			rotatePoints(new Vector(-Math.PI/2));
		}
	}
}
