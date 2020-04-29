package jetpack_joyride;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
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
import java.util.ConcurrentModificationException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import apgraphicslib.Object_draw;


public class JetPack_JoyRide {

	public static final String version = "3.1.6";
	

	static Object_draw drawer = new Object_draw();
	

	private static JPJR_GUI GUI = new JPJR_GUI();
	
	static JPJR_Shop shop = new JPJR_Shop(drawer);
	
	static int coins = 0, coinsEarned = 0, game_over = 0;  // 0 is false, 1 is true, and 2 & 3 are other
	
	static double distance = 0, distanceHighScore = 0, frames, jetpack_speed = 40;
	
	private static int gravityStart = 600, gravity;
	
	static final boolean pictureGraphics = false;
	
	static boolean pause = false, error = false;
	
	static ImageIcon jetpack_img = new ImageIcon("jetpack.txt"), coin_img = new ImageIcon("coin.txt");
	static border_bounce boundries;
	static JetPack jetpack;
	static Laser laser1,laser2;
	static MIT_Laser MITLaser1;
	static Missile Missile1;
	static ScoreBoard coinScore,distanceScore,distanceHighScoreBoard;
	static Coin coin1,coin2,coin3;
	

	
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		init();
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
	
	public static void setDrawer(object_draw drawer1) {
		drawer = drawer1;
		drawer.setFrame(frame);
	}
	
	public static void run() {
		init();
	}
	
	public static void init()  {
		
		
		FPS_display fps = new FPS_display(drawer,30,30);
		drawer.add(fps);
	
		FCPS_display fcps = new FCPS_display(drawer,30,50);
		drawer.add(fcps);
		
		boundries = new border_bounce(drawer);
		
		boundries.name = "boundries";
		boundries.drawMethod = "listedPointsAlgorithm";
		boundries.setPos(boundries.getXReal(),boundries.getYReal()-100,10);
		boundries.setSize(Settings.width *1.06, Settings.height*1.15,10);
		boundries.isVisible = false;
		
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
		jetpack.name = "jetpack";
		
		laser1 = new Laser(drawer,Settings.width/3,200,Settings.width/100,Settings.width/10);
		laser1.setName("_Laser1",0);
		laser2 = new Laser(drawer,Settings.width/2,200,Settings.width/100,Settings.width/10);
		laser2.setName("_Laser2",0);
		
		
		MITLaser1 = new MIT_Laser(drawer, 4 * Settings.width/5,200);
		MITLaser1.setName("_MITLaser1", 0);
		drawer.add(MITLaser1);
		
		
		Missile1 = new Missile(drawer,0,200);
		Missile1.setName("thing",0);
		
		coin1 = new Coin(drawer,350, 270);
		coin2 = new Coin(drawer,530, 300);
		coin3 = new Coin(drawer,760, 230);
		
	
			
		//mouseListener +==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
				drawer.addMouseListener(new MouseListener() {
						
						public void mousePressed(MouseEvent arg0) {
							  jetpack.setAccel(0, 0, 0);
			            	  jetpack.applyComponentForce(0, -jetpack.current_power, 0);
			            	  jetpack.fireSize = 0.75;
			            	  drawer.inactivity_timer = 0;
			            	  
			            	  JetPack_fire jetPackFire = new JetPack_fire(drawer,jetpack.getX() + jetpack.getXSize()* 0.3,jetpack.getY() + jetpack.getYSize() + jetpack.getYSize()*0.2 ,Math.random() /2 ,-1);
			            	  
			            	  JetPack_fire jetPackFire2 = new JetPack_fire(drawer,(jetpack.getX() + jetpack.getXSize()) - jetpack.getXSize()*0.3,jetpack.getY() + jetpack.getYSize() + jetpack.getYSize()*0.2,-Math.random() / 2 ,1);
				            
							
						}
						
						@Override
						public void mouseReleased(MouseEvent e) {
							jetpack.setAccel(0, 0, 0);
							jetpack.applyComponentForce(0, jetpack.current_power, 0);
							jetpack.applyComponentForce(0, gravity, 0);
							jetpack.fireSize = 0.35;
							drawer.inactivity_timer = 0;
						}
						
						public void mouseClicked(MouseEvent arg0) {}
						@Override
						public void mouseEntered(MouseEvent e) {}
						@Override
						public void mouseExited(MouseEvent e) {}
						
				
				});
						
			//key listener
			drawer.addKeyListener(new KeyListener() {
				   
	              @Override
	              public void keyPressed(KeyEvent e) {
	            	 
	            	  jetpack.setAccel(0, 0, 0);
	            	  jetpack.applyComponentForce(0, -jetpack.current_power, 0);
	            	  jetpack.fireSize = 0.75;
	            	  drawer.inactivity_timer = 0;
	            	  
	            	  JetPack_fire jetPackFire = new JetPack_fire(drawer,jetpack.getX() + jetpack.getXSize()* 0.3,jetpack.getY() + jetpack.getYSize() + jetpack.getYSize()*0.2 ,Math.random() /2 ,-1);
	            	  
	            	  JetPack_fire jetPackFire2 = new JetPack_fire(drawer,(jetpack.getX() + jetpack.getXSize()) - jetpack.getXSize()*0.3,jetpack.getY() + jetpack.getYSize() + jetpack.getYSize()*0.2,-Math.random() / 2 ,1);
		            	 
	            
	              }
	
				@Override
				public void keyReleased(KeyEvent arg0) {
				
					jetpack.setAccel(0, 0, 0);
					jetpack.applyComponentForce(0, jetpack.current_power, 0);
					jetpack.applyComponentForce(0, gravity, 0);
					jetpack.fireSize = 0.35;
					drawer.inactivity_timer = 0;
					
				}
				@Override
				public void keyTyped(KeyEvent arg0) { 
					drawer.inactivity_timer = 0;
				}
	          });	  
					
				
	
		drawer.add(jetpack);
		drawer.add(boundries);
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
			
		
	
		
		frame.setVisible(true);
		GUI.setVisible(true);
		
		distance = 0;	
		game_over = 0;
		
		for (physics_object pObject : drawer.getObjects()) {
		
			if (pObject.getObjectName().substring(0, 1) == "_") {
				((Physics_drawable) pObject).setSpeed(-jetpack_speed, ((Physics_drawable) pObject).getYSpeed(), 0);
			}
			
		}
		
		
		resize();
		
		drawer.start();
		runGame();
		
	}

	public static void runGame() {
		
		jetpack.applyComponentForce(0, gravity, 0);
		
		while (game_over == 0) {
			
			if (! pause) {	
					if ((jetpack.getY() < -40) || (jetpack.getY() > Settings.height*1.25)){
						jetpack.setPos(Settings.width/2, Settings.height - 100, 0);
					}		
					
					coinScore.setScore(coins);
					distanceScore.setScore(Math.round(distance));
					distance += jetpack_speed;
					jetpack_speed += (Math.pow(drawer.current_frame,1/100) / 800) * frames;
					
					gravity = (int) (gravityStart + jetpack_speed);
					
					Settings.width = frame.getWidth();
					Settings.height = frame.getHeight();
					
					try {
						for (physics_object pObject : drawer.getObjects()) {
							if (pObject.getObjectName().substring(0, 1) == "_") {
								((Physics_drawable) pObject).setSpeed(-jetpack_speed, ((Physics_drawable) pObject).getYSpeed(), 0);
							}
						}
					}catch(ConcurrentModificationException c) {
					
					}catch(NullPointerException n) {}
					
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
	

		drawer.end();
		frame.remove(drawer);
	
		frame.dispose();
		GUI.dispose();
		shop.dispose();
		
		//end
	
	}

	public static void resize() { //resizes the objects for a changed frame size
		System.out.println("resizing game...");
		
		double diagonal = Math.sqrt(Math.pow(Settings.width, 2) + Math.pow(Settings.height, 2));
		
		distanceHighScoreBoard.setPos(0.4 * Settings.width,Settings.height-100,0);
		distanceScore.setPos(0.7 * Settings.width,Settings.height-100,0);		
		coinScore.setPos(0.05 * Settings.width,Settings.height-100,0);
		jetpack.setPos(Settings.width/2,Settings.height/2, 0);
		jetpack.setSize(diagonal/50, diagonal/50, 0);
		GUI.setLocation(Settings.width + 20, 20);
		
		boundries.resize();
		
		setSettings();
		
		
		for (Physics_drawable pOb : drawer.getDrawables()) {
			if (pOb.getObjectName().substring(0, 1) == "_") {
				try {
					pOb = (Missile) pOb;
					pOb.setSize(diagonal/35,diagonal/120,0);
				}catch(ClassCastException e) {}
				
				try {
					pOb = (MIT_Laser) pOb;
					pOb.setSize(15 * diagonal/100 + diagonal/20, diagonal/10, 1);
				}catch(ClassCastException c) {
					try {
						pOb = (Laser) pOb;
						pOb.setSize(diagonal/100,diagonal/10,0);
					}catch(ClassCastException b) {}
				}
				
				try {
					pOb = (Coin) pOb;
					pOb.setSize(diagonal/60,diagonal/60,0);
				}catch(ClassCastException d) {
					
				}

			}
		}
		Missile1.setSize(diagonal/37,diagonal/120,0);
		MITLaser1.setSize(15 * diagonal/100 + diagonal/20, diagonal/10, 1);
		shop.setLocation(Settings.width + 20, 25 + Settings.height/2);
		shop.setSize(Settings.width/4,Settings.height/2);
	
	
	
	}
	
	public static void resetGame() {
		
		drawer.pause();
		
		System.out.println("Resetting game");
		
		jetpack.setPos(Settings.width/2,Settings.height/2, 0);
		jetpack.setSpeed(0, 0, 0);
		jetpack.setAccel(0,0,0);
		jetpack.applyComponentForce(0, 9.8, 0);
		
		laser1.setPos(Math.random() * 100,Math.random() * 300,0);
		laser2.setPos(Math.random() * Settings.width/2,Math.random() * 200,0);
		MITLaser1.setPos(Math.random() * 4 * Settings.width/5,Math.random() * 200,0);
		
		Missile1.setPos(0,200,0);
		
		coin1.setPos(350, 270,0);
		coin2.setPos(530, 300,0);
		coin3.setPos(760, 230,0);
		
		distance = 0;		
		game_over = 0;
		jetpack_speed = 40;
		coinsEarned = 0;
		
		
		distanceHighScoreBoard.setScore(distanceHighScore);	
	
		

		
		drawer.resume();
	}
	
	public static void setSettings() {
		Settings.frameTime = 100;	
		Settings.elasticity = 1;
		Settings.collision_algorithm = 4;
		Settings.rotationAlgorithm = 6;
		Settings.timeOutTime = 5000000;
		drawer.setFrameTimeMultiplier(100);
		Settings.fixedFPS_FStep = false;
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
	
}
