package danceDanceRevolution;

import java.awt.Color;
import java.util.ArrayList;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class DDRRunner {
	private static Object_draw drawer = new Object_draw();
	
	private ArrayList<Song> songs = new ArrayList<Song>();
	
	static Song currentSong;
	
	public static void main(String[] args) {
		Settings.perspective = false;
		Settings.targetFPS = 60;
		Settings.frameColor = Color.black;
		
	//	Song song = new Song(drawer,"./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer, "What song?") + ".dat");
		Song song = new Song(drawer,"./src/danceDanceRevolution/assets/september.dat");
	//	Song song = new Song(drawer,"./src/danceDanceRevolution/assets/Only The Good Die Young (Mom).dat");
	
		
		song.play();
		System.out.println(song);
	
		while (drawer.getFrame().isShowing()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
		drawer.stop();
		System.exit(1);
		
	}
	
	@Deprecated
	private static void noteTest() {
		double noteSpeed = 100;
		UpNote up = new UpNote(drawer,Settings.height/5,noteSpeed,"./src/danceDanceRevolution/assets/arrowTexturePurple.png");
		up.run();
		
		LeftNote left = new LeftNote(drawer,2*Settings.height/5,noteSpeed,"./src/danceDanceRevolution/assets/arrowTexturePurple.png");
		left.run();
		
		DownNote down = new DownNote(drawer,1.5*Settings.height/5,noteSpeed,"./src/danceDanceRevolution/assets/arrowTexturePurple.png");
		down.run();

		RightNote right = new RightNote(drawer,2.5*Settings.height/5,noteSpeed,"./src/danceDanceRevolution/assets/arrowTexturePurple.png");
		right.run();
	}
}
