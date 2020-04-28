package apgraphicslib;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


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
			e.printStackTrace();
		}
	}
}
