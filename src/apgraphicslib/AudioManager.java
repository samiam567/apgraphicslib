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
	
	private static Clip clip;
	
	public static void playAudioFile(String filePath) {
		try { 
			File audioFile = new File(filePath);
			
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(audioFile));
			clip.start();
			
		}catch( Exception e) {
			if (Settings.JOptionPaneErrorMessages) {
				JOptionPane.showMessageDialog(null, "Could not read audio file: " + filePath);
			}
			e.printStackTrace();

		}
	}
	
	public static void playAudioFileWaitForEnd(String filePath) {
		playAudioFile(filePath);
		
		while(clip.isOpen()) {
			try {
				Thread.sleep(1);
			}catch(InterruptedException i) {}
		}
	}
}
