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
		
		String[] songChoices = {"September AubioMix","Sultans Of Swing AubioMix","Only The Good Die Young AubioMix","Aint Even Done With the Night AubioMix","Axel F AubioMix", "Happy AubioMix", "Faster AubioMix", "Get Lucky AubioMix", "SexyBack AubioMix", "Jump Jump Jump AubioMix", "Wheres the Exit AubioMix", "Pirates of the Carribean Piano AubioMix","Main Theme AubioMix","Other", "Create new song"};
		String songChoice = (String) JOptionPane.showInputDialog(drawer, "Which Song do you want?", "Choose a song",  JOptionPane.PLAIN_MESSAGE, null, songChoices, songChoices[0]);
		
		Song song;
		if (songChoice.equals("Other")) {
			song = new Song(drawer,"./src/danceDanceRevolution/assets/Songs/" + JOptionPane.showInputDialog(drawer, "What song?") + ".dat");
		}else if (songChoice.equals("Create new song")) {
			song = new Song(drawer);
			System.exit(1);
		}else {
			song = new Song(drawer,"./src/danceDanceRevolution/assets/Songs/" + songChoice + ".dat");
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
	
	
}
