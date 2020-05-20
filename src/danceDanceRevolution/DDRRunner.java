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
		
		Settings.width = 1200;
		Settings.height = 1000;
		Settings.perspective = false;
		Settings.targetFPS = 60;
		
		drawer = new Object_draw();
		drawer.getFrame().setBackground(Color.black);
		
		score = new ScoreBoard(drawer,100,0.9 * Settings.height,"Score",0);
		score.setColor(Color.green);
		drawer.add(score);
		
		String[] songChoices = {"quit", "September AubioMix","Sultans Of Swing AubioMix","Only The Good Die Young AubioMix","Aint Even Done With the Night AubioMix","Bohemian Rhapsody AubioMix","Gangnam Style AubioMix","Axel F AubioMix", "Happy AubioMix", "Faster AubioMix", "Get Lucky AubioMix", "SexyBack AubioMix", "If I Never See Your Face Again AubioMix", "Faith AubioMix", "Wake Up Call AubioMix", "Party In The USA AubioMix","Jump Jump Jump AubioMix", "Wheres the Exit AubioMix", "Wheres the Exit AubioMix onsetVersion", "Never Gonna Give You Up AubioMix", "Pirates of the Carribean Piano AubioMix","Main Theme AubioMix","Other", "Create new song"};
		
		String songChoice = (String) JOptionPane.showInputDialog(drawer, "Which Song do you want?", "Choose a song",  JOptionPane.PLAIN_MESSAGE, null, songChoices, songChoices[0]);
	
		if (! songChoice.equals("quit")) {
			do {
	
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
				
				songChoice = (String) JOptionPane.showInputDialog(drawer, "Which Song do you want?", "Choose a song",  JOptionPane.PLAIN_MESSAGE, null, songChoices, songChoices[0]);
				
			} while (! songChoice.equals("quit"));
		}
	
		drawer.getFrame().setVisible(false);
		drawer.stop();
		System.exit(1);
		
	}
	
	
}
