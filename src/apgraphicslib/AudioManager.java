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
	
	/**
	 * {@code only works in fast speed}
	 * @param filePath
	 * @param playBackSpeed
	 */
	@Deprecated
	public static void playAudioFile(String filePath, double playBackSpeed, int u) {
		
		try {
			System.out.println("Playback Rate: " + playBackSpeed);
	
			File audioFile = new File(filePath);
	        System.out.println("URL: ");
	        AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
	        AudioFormat af = ais.getFormat();
	
	        int frameSize = af.getFrameSize();
	
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] b = new byte[2^16];
	        int read = 1;
	        
	      
	        while( read>-1 ) {
	      
	        	read = ais.read(b);
				
		        if (read>0) {
		            baos.write(b, 0, read);
		        }
	        }
	      
	        System.out.println("End entire: \t" + new Date());
	
	        byte[] b1 = baos.toByteArray();
	        byte[] b2 = new byte[(int) (b1.length/playBackSpeed)];
	        for (int ii=0; ii<b2.length/frameSize; ii++) {
	            for (int jj=0; jj<frameSize; jj++) {
	                b2[(ii*frameSize)+jj] = b1[(int) ((ii*frameSize*playBackSpeed)+jj)];
	            }
	        }
	        System.out.println("End sub-sample: \t" + new Date());
	
	        ByteArrayInputStream bais = new ByteArrayInputStream(b2);
	        AudioInputStream aisAccelerated =
	            new AudioInputStream(bais, af, b2.length);
	        
			clip = AudioSystem.getClip();
			clip.open(aisAccelerated);
		    clip.loop((int) (2*playBackSpeed));
		    clip.start();

        } catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
