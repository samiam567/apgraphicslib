package jetpack_joyride;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import apgraphicslib.Drawable;
import apgraphicslib.FCPS_display;
import apgraphicslib.FPS_display;
import apgraphicslib.Movable;
import apgraphicslib.Object_draw;
import apgraphicslib.Resizable;
import apgraphicslib.ScoreBoard;
import apgraphicslib.Settings;
import apgraphicslib.Vector2D;

public class JetPack_JoyRide implements MouseListener, KeyListener{

	public static final String version = "3.1.6";
	

	static Object_draw drawer = new Object_draw();
	

	private static JPJR_GUI GUI = new JPJR_GUI();
	
	static JPJR_Shop shop = new JPJR_Shop(drawer);
	
	static int coins = 0, coinsEarned = 0, game_over = 0;  // 0 is false, 1 is true, and 2 & 3 are other
	
	static double distance = 0, distanceHighScore = 0, frames,jetpack_startSpeed = 1000, jetpack_speed = jetpack_startSpeed;
	
	private static int gravityStart = 4000, gravity;
	
	static final boolean pictureGraphics = false;
	
	static boolean pause = false, error = false;
	
	static ImageIcon jetpack_img = new ImageIcon("jetpack.txt"), coin_img = new ImageIcon("coin.txt");
	
	static JetPack jetpack;
	static Laser laser1,laser2;
	static Missile Missile1;
	static ScoreBoard coinScore,distanceScore,distanceHighScoreBoard;
	static Coin coin1,coin2,coin3;
	
	private static long keyPressedTimes = 0;
	
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		new JetPack_JoyRide();
		System.exit(1);
	}
	
	public static void playSound(File soundFile) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(soundFile));
			clip.start();
		}catch(Exception e) {
			
		}
	}
	
	public JetPack_JoyRide() {
		init();
	}
	
	public void run() {
		init();
	}
	
	public void init()  {
		
		
		
		drawer.getFrame().setBackground(Color.black);
		FPS_display fps = new FPS_display(drawer,30,30);
		fps.setColor(Color.white);
		drawer.add(fps);
	
		FCPS_display fcps = new FCPS_display(drawer,30,50);
		fcps.setColor(Color.white);
		fcps.decimalsToRound = 5;
		drawer.add(fcps);

		
		
		coinScore = new ScoreBoard(drawer);
		coinScore.setScore(coins);
		coinScore.setScorePhrase("Coins:");
		coinScore.setColor(Color.WHITE);
		
		distanceScore = new ScoreBoard(drawer,0.7 * Settings.width,Settings.height-100,"Distance:",distance);
		distanceScore.setColor(Color.WHITE);
		distanceScore.setEndPhrase("m");
		
		distanceHighScoreBoard = new ScoreBoard(drawer,0.4 * Settings.width,Settings.height-100,"HighScore:",distance);
		distanceHighScoreBoard.setColor(Color.WHITE);
		distanceHighScoreBoard.setEndPhrase("m");
		
		jetpack = new JetPack(drawer,Settings.width/2,Settings.height-150, 0, 20,200);
		jetpack.setColor(Color.blue);
		jetpack.setName("jetpack");
		
		JetPack_fire.loadFires(drawer);
		
		laser1 = new Laser(drawer,Settings.width/3,200,Settings.width/100,Settings.width/10);
		laser1.setName("_Laser1");
		laser2 = new Laser(drawer,Settings.width/2,200,Settings.width/100,Settings.width/10);
		laser2.setName("_Laser2");
		
		
		
		
		Missile1 = new Missile(drawer,0,200);
		Missile1.setName("thing");
		
		coin1 = new Coin(drawer,350, 270);
		coin2 = new Coin(drawer,530, 300);
		coin3 = new Coin(drawer,760, 230);
		
			  
					
	    drawer.getFrame().getContentPane().addMouseListener(this);
	  	drawer.getFrame().getGlassPane().addMouseListener(this);
	  	drawer.getFrame().addMouseListener(this);
	  	drawer.addMouseListener(this);
	  	
	  	drawer.getFrame().getContentPane().addKeyListener(this);
	  	drawer.getFrame().getGlassPane().addKeyListener(this);
	  	drawer.getFrame().addKeyListener(this);
	  	drawer.addKeyListener(this);
	
		drawer.add(jetpack);
		drawer.add(coinScore);
		drawer.add(distanceScore);
		drawer.add(distanceHighScoreBoard);
		drawer.add(laser1);
		drawer.add(laser2);
		drawer.add(Missile1);
		drawer.add(coin1);
		drawer.add(coin2);
		drawer.add(coin3);
	
		
		
		
	
		
		
		
		//load the game
			try {
				loadGame(); 
			}catch(FileNotFoundException e) {
				System.out.println("Missing Save File");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
		//initializing the GUIs
		GUI.init();
		shop.init();
			
			
		distanceHighScoreBoard.setScore(distanceHighScore);	
			
		
	
		
		drawer.getFrame().setVisible(true);
		GUI.setVisible(true);
		
		distance = 0;	
		game_over = 0;
		
		for (Drawable pObject : drawer.getDrawables()) {
		
			if (pObject.getName().substring(0, 1) == "_") {
				try {
					((Vector2D)((Movable) pObject).getSpeed()).setI(-jetpack_speed);
				}catch(ClassCastException c) {}
			}
			
		}
		
		
		resize();
		
		drawer.start();
		runGame();
		
	}

	public static void runGame() {
		

		
		while (game_over == 0) {
			
			if (! pause) {	
					if ((jetpack.getY() < -40) || (jetpack.getY() > Settings.height*1.25)){
						jetpack.setPos(Settings.width/2, Settings.height - 100);
					}		
					
					coinScore.setScore(coins);
					distanceScore.setScore(Math.round(distance));
					distance += jetpack_speed;
				//	jetpack_speed += (Math.pow(drawer.current_frame,1/100) / 800) * frames;
					
					gravity = (int) (gravityStart + jetpack_speed);
					
					Settings.width = drawer.getFrame().getWidth();
					Settings.height = drawer.getFrame().getHeight();
					
					
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
			}else {
				
				drawer.pause();
				while (pause) {
					GUI.repaint();	
				}
				drawer.resume();
			}
		}
		
		if ((game_over == 1) || (game_over == 4)) {
			drawer.pause();
		
			String mes = "";
			if (distance > distanceHighScore) {
				mes = "\n You beat the high score!";
			}
			
			if (coins == 1) {
				JOptionPane.showMessageDialog(drawer, "GAME OVER\nYou earned " + coinsEarned + " coin!\nYou traveled " + Math.round(distance) + " meters!" + mes);
			} else {
				JOptionPane.showMessageDialog(drawer, "GAME OVER\nYou earned " + coinsEarned + " coins!\nYou traveled " + Math.round(distance) + " meters!" + mes);
			}
			
		
				
			
			try {
				saveGame(); //save the game
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		
	
				//See if user wants to play another game and if so, starting another game  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
				int another = 2;
				while (another == 2) {
					another = JOptionPane.showConfirmDialog(null,"Do you want to play another game?", "Another?", 1, 1, null);
				}
				
				if (another == 0) {
					resetGame();
					drawer.resume();
					runGame();
				}
				//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			
		}
	
		System.out.println("stopping");
		drawer.stopNoWait();
		drawer.getFrame().remove(drawer);
		drawer.getFrame().dispose();
		GUI.dispose();
		shop.dispose();
		System.exit(1);
		
		//end
	
	}

	public static void resize() { //resizes the objects for a changed frame size
		System.out.println("resizing game...");
		
		double diagonal = Math.sqrt(Math.pow(Settings.width, 2) + Math.pow(Settings.height, 2));
		
		distanceHighScoreBoard.setPos(0.4 * Settings.width,Settings.height-100);
		distanceScore.setPos(0.7 * Settings.width,Settings.height-100);		
		coinScore.setPos(0.05 * Settings.width,Settings.height-100);
		jetpack.setPos(Settings.width/2,Settings.height/2);
		jetpack.setSize(diagonal/50, diagonal/50);
		GUI.setLocation(Settings.width + 20, 20);
		
		
		setSettings();
	
		shop.setLocation(Settings.width + 20, 25 + Settings.height/2);
		shop.setSize(Settings.width/4,Settings.height/2);
	
	
	
	}
	
	public static void resetGame() {
		
		drawer.pause();
		
		System.out.println("Resetting game");
		
		jetpack.setPos(Settings.width/2,Settings.height/2);
		jetpack.getSpeed().setR(0);
		jetpack.getAcceleration().setR(0);
		jetpack.getAcceleration().setJ(gravity);
		
		laser1.setPos(Math.random() * 100,Math.random() * 300);
		laser2.setPos(Math.random() * Settings.width/2,Math.random() * 200);

		
		Missile1.setPos(0,200);
		
		coin1.setPos(350, 270);
		coin2.setPos(530, 300);
		coin3.setPos(760, 230);
		
		distance = 0;		
		game_over = 0;
		jetpack_speed = jetpack_startSpeed;
		coinsEarned = 0;
		
		
		distanceHighScoreBoard.setScore(distanceHighScore);	
	
		

		
		drawer.resume();
	}
	
	public static void setSettings() {
		Settings.perspective = false;
		Settings.advancedRotation = true;
		Settings.JOptionPaneErrorMessages = false;
		Settings.targetFPS = 60; //attempt to make the FPS 60
		Settings.autoResizeFrame = true;
		Settings.timeSpeed = 1;
	}
	
	public static void loadGame() throws ClassNotFoundException, IOException{

		try {
			System.out.println("Loading from save_file...");
			ObjectInputStream loader = new ObjectInputStream(new FileInputStream("JetPack_JoyRide_save.txt"));
			JetPack_JoyRide_SaveFIle save = (JetPack_JoyRide_SaveFIle) loader.readObject();
			coins = save.coins;
			distanceHighScore = save.distanceHighScore;
			shop.boughtButtons = save.boughtButtons;
			loader.close();
		}catch(InvalidClassException e) {
			System.out.println("Corrupted Save_file"); 
		}catch(EOFException e) {
			System.out.println("Corrupted Save_file");
		}
	}
	
	public static void saveGame() throws FileNotFoundException, IOException {
		System.out.println("Saving in progress...");
		ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream("JetPack_JoyRide_save.txt"));
		
		if (distance > distanceHighScore) {
			distanceHighScore = distance;
		}
		
		
		JetPack_JoyRide_SaveFIle save = new JetPack_JoyRide_SaveFIle();
		save.distanceHighScore = distanceHighScore;
		save.coins = coins;
		save.boughtButtons = shop.boughtButtons;
		
		saver.writeObject(save);
		saver.close();
		System.out.println("Save Complete");
		
	}
	
	 @Override
     public void keyPressed(KeyEvent e) {
   	 
   	  jetpack.getAcceleration().setJ(-jetpack.current_power);
   	  jetpack.fireSize = 0.75;
   	  drawer.inactivity_timer = 0;
   	  
   	  if (keyPressedTimes % 1 == 0) {
	   	  JetPack_fire.showFire();
	   	  JetPack_fire.showFire();
   	  }  	 
   	  
   	  keyPressedTimes++;
   
     }

	@Override
	public void keyReleased(KeyEvent arg0) {
	
		jetpack.getAcceleration().setJ(gravity);
		jetpack.fireSize = 0.35;
		drawer.inactivity_timer = 0;
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) { 
		drawer.inactivity_timer = 0;
	}
	
	public void mousePressed(MouseEvent arg0) {
		jetpack.getAcceleration().setR(0);
		jetpack.getAcceleration().setJ(-jetpack.current_power);
    	jetpack.fireSize = 0.75;
    	
    	  
    	JetPack_fire.showFire();
    	JetPack_fire.showFire();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		jetpack.getAcceleration().setR(0);
		jetpack.getAcceleration().setJ(gravity);
		jetpack.fireSize = 0.35;
		drawer.inactivity_timer = 0;
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
}
