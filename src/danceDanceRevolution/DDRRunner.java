package danceDanceRevolution;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class DDRRunner {
	private static Object_draw drawer = new Object_draw();
	
	private ArrayList<Song> songs = new ArrayList<Song>();
	
	static Song currentSong;
	
	public static void main(String[] args) {
		Song song = new Song(drawer,"./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer, "What song?") + ".dat");
		
		
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
