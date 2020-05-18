package danceDanceRevolution;

import java.util.NoSuchElementException;

import com.sun.source.tree.ParenthesizedTree;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;
import apgraphicslib.Vector;

public class LeftNote extends Note {

	
	public LeftNote(Song parent, double y, double noteSpeed, String textureSrc) {
		super(parent, Settings.width/5, y, noteSpeed, textureSrc);
		
	}
	
	@Override
	public void load() {
		if (! loaded) {
			super.load();
			rotatePoints(new Vector(Math.PI/2));
		}
	}
	
	@Override
	public void reposition() {
		try {
			getCoordinates().setY(parentSong.lPosQueue.remove() + parentSong.getY());
			super.reposition();
		}catch(NoSuchElementException n) {
			getDrawer().remove(this);
		}
	}
}
