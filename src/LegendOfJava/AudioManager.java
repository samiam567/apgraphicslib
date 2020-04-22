package LegendOfJava;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import apgraphicslib.Object_draw;

/**
 * okay I will admit that I just looked up this one
 * @author samaim567
 * {@code plays sounds... weeee}
 */
public class AudioManager {
	
	private String damageSoundSrc = "src/LegendOfJava/assets/oof.wav";
	
	
	
	private Object_draw drawer;
	public AudioManager(Object_draw drawer) {
		 this.drawer = drawer;
         
	}
	
	public void playDamageAudio() {
		try {
			URL url = drawer.getFrame().getClass().getClassLoader().getResource(damageSoundSrc);
			System.out.println(drawer.getFrame().getClass().getClassLoader().getResource(damageSoundSrc));
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip damageAudioClip = AudioSystem.getClip();
			damageAudioClip.open(audioIn);
			damageAudioClip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
