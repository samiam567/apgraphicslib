package danceDanceRevolution;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import apgraphicslib.Object_draw;
import apgraphicslib.ScoreBoard;
import apgraphicslib.Settings;
import apgraphicslib.Vector2D;

public class DDRRunner {
	private static Object_draw drawer;
	
	
	static Song currentSong;
	
	static ScoreBoard score;
	public static void main(String[] args) {
		
	//	Settings.width = 1200;
	//	Settings.height = 1000;
		Settings.perspective = false;
		Settings.targetFPS = 120;
		
		drawer = new Object_draw();
		drawer.getFrame().setBackground(Color.black);
		
		score = new ScoreBoard(drawer,100,100,"Score",0);
		score.setColor(Color.green);
		drawer.add(score);
		
		String[] songChoices = {"september","Sultans Of Swing","Only The Good Die Young AubioMix", "Star Wars (Emily)", "Other", "Create new song"};
		String songChoice = songChoices[JOptionPane.showOptionDialog(drawer, "Which Song do you want?", "Choose a song", 1, 1, null, songChoices, 0)];
		
		Song song;
		if (songChoice.equals("Other")) {
			song = new Song(drawer,"./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer, "What song?") + ".dat");
		}else if (songChoice.equals("Create new song")) {
			song = new Song(drawer);
			System.exit(1);
		}else {
			song = new Song(drawer,"./src/danceDanceRevolution/assets/" + songChoice + ".dat");
		}
		
		song.play(1);
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
