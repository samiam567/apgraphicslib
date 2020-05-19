package danceDanceRevolution;

import java.util.NoSuchElementException;

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
	
	
	@Override
	public void reposition() {
		try {
			getCoordinates().setY(parentSong.rPosQueue.remove() + parentSong.getY());
			super.reposition();
		}catch(NoSuchElementException n) {
			getDrawer().remove(this);
			setIsVisible(false);
		}
		
	}
}
