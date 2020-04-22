package LegendOfJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


/**
 * okay I will admit that I just looked up this one.... 
 * {@code plays sounds... weeee}
 * @author samaim567
 */
public class AudioManager {
	
	private static String damageSoundSrc = "./src/LegendOfJava/assets/oof.wav";
	private static String hitSoundSrc = "./src/LegendOfJava/assets/hit.wav";
	private static String potBreakSrc = "./src/LegendOfJava/assets/potBreak.wav";
	private static String heartRestoreSrc = "./src/LegendOfJava/assets/heartRestore.wav";
	private static String swordSwingSrc = "./src/LegendOfJava/assets/swordSwing.wav";
	
	public static void playAudioFile(String filePath) {
		InputStream audio;
		try { 
			audio = new FileInputStream(new File(filePath));
			AudioStream audios = new AudioStream(audio);
			AudioPlayer.player.start(audios);
		}catch( Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playDamageAudio() {
		playAudioFile(damageSoundSrc);
	}
	
	public static void playHitAudio() {
		playAudioFile(hitSoundSrc);
	}
	
	public static void playPotBreakAudio() {
		playAudioFile(potBreakSrc);
	}
	
	public static void playHeartRestoreAudio() {
		playAudioFile(heartRestoreSrc);
	}
	
	public static void playSwordSwingAudio() {
		playAudioFile(swordSwingSrc);
	}
	
}
