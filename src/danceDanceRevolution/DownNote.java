package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class DownNote extends Note {
	
	private static double downNoteXPos = 2*Settings.width/5;
	public DownNote(Object_draw drawer, double y, double noteSpeed, String textureSrc) {
		super(drawer, downNoteXPos, y, noteSpeed, textureSrc);
		
	}
	

}
