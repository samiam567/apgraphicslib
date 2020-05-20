package apgraphicslib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;


/**
 * {@code plays sounds... weeee}
 * @author samaim567
 */
public class AudioManager {
	
	public static Clip clip;
	
	private static boolean isPlaying = false;
	
	private static class AudioThread extends Thread {
		
		private String filePath; 
		private double playBackSpeed;
		public AudioThread(String filePath, double playBackSpeed) {
			this.filePath = filePath;
			this.playBackSpeed = playBackSpeed;
		}
		public void run() {
			playAudioFileWaitForEnd(filePath, playBackSpeed);
			Thread.yield();
		}
	}

	
	public static void playAudioFile(String filePath) {
		try { 
			File audioFile = new File(filePath);
			
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
					
			clip.open(ais);
			
			
			clip.start();
		}catch( Exception e) {
			if (Settings.JOptionPaneErrorMessages) {
				JOptionPane.showMessageDialog(null, "Could not read audio file: " + filePath);
			}
			e.printStackTrace();

		}
	}
	
	public static void playAudioFile(String filePath, double playBackSpeed) {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	    AudioThread t = new AudioThread(filePath, playBackSpeed);
	    t.start();
	}
	
	
	public static void playAudioFileWaitForEnd(String filePath, double playBackSpeed) {
		isPlaying = true;
		try {
		      File fileIn = new File(filePath);
		      AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(fileIn);
		      AudioFormat formatIn=audioInputStream.getFormat();
		      AudioFormat format=new AudioFormat((float) (formatIn.getSampleRate()*playBackSpeed), formatIn.getSampleSizeInBits(), formatIn.getChannels(), true, formatIn.isBigEndian());
		          System.out.println(formatIn.toString());
		          System.out.println(format.toString());
		      byte[] data=new byte[1024];
		      DataLine.Info dinfo=new DataLine.Info(SourceDataLine.class, format);
		      SourceDataLine line=(SourceDataLine)AudioSystem.getLine(dinfo);
		      if(line!=null) {
		        line.open(format);
		        line.start();
		        while(isPlaying) {
		          int k=audioInputStream.read(data, 0, data.length);
		          if(k<0) break;
		          line.write(data, 0, k);
		        }
		        line.stop();
		        line.close();
		      }
		    }
		    catch(Exception ex) { ex.printStackTrace(); }
		
		isPlaying = false;
	}
	
	
    
	
	public static void playAudioFileWaitForEnd(String filePath) {
		isPlaying = true;
		playAudioFile(filePath);
		
		while(clip.isOpen()) {
			try {
				Thread.sleep(10);
			}catch(InterruptedException i) {}
		}
		
		isPlaying = false;
		
	}
	
	public static void stop() {
		clip.close();
		isPlaying = false;
		
		//wait for all song threads to stop execution
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * {@code only works with playback speed varying versions and waitforend normal version}
	 * @return
	 */
	@Deprecated
	public static boolean isPlaying() {
		return isPlaying;
	}
}
