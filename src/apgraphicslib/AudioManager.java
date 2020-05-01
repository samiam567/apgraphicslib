package apgraphicslib;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;


/**
 * {@code plays sounds... weeee}
 * @author samaim567
 */
public class AudioManager {

	
	public static void playAudioFile(String filePath) {
		try { 
			File audioFile = new File(filePath);
			
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(audioFile));
			clip.start();
		}catch( Exception e) {
			if (Settings.JOptionPaneErrorMessages) {
				JOptionPane.showMessageDialog(null, "Could not read audio file: " + filePath);
			}
			e.printStackTrace();
		}
	}
}
