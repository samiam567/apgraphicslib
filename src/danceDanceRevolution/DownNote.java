package danceDanceRevolution;

import java.util.NoSuchElementException;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class DownNote extends Note {

	public DownNote(Song parent, double y, double noteSpeed, String textureSrc) {
		super(parent, 2*Settings.width/5, y, noteSpeed, textureSrc);
		
	}
	
	
	@Override
	public void reposition() {
		try {
			getCoordinates().setY(parentSong.dPosQueue.remove() + parentSong.getY());
			super.reposition();
		}catch(NoSuchElementException n) {
			getDrawer().remove(this);
			setIsVisible(false);
		}
	}

}
