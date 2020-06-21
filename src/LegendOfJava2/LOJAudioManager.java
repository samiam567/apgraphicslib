package LegendOfJava2;

import apgraphicslib.AudioManager;

public class LOJAudioManager extends AudioManager {
	private static String damageSoundSrc = "./src/LegendOfJava/assets/oof.wav";
	private static String hitSoundSrc = "./src/LegendOfJava/assets/hit.wav";
	private static String potBreakSrc = "./src/LegendOfJava/assets/potBreak.wav";
	private static String heartRestoreSrc = "./src/LegendOfJava/assets/heartRestore.wav";
	private static String swordSwingSrc = "./src/LegendOfJava/assets/swordSwing.wav";
	
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
