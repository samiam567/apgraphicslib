package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import apgraphicslib.FCPS_display;
import apgraphicslib.FPS_display;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Physics_frame;
import apgraphicslib.Physics_runner;
import apgraphicslib.ScoreBoard;
import apgraphicslib.Settings;
import apgraphicslib.Vector3D;
import apgraphicslib.Timer.TimerUnits;

public class Pong_runner extends Physics_runner{

	public static final String Version = "2.3.0";
	
	
	public static boolean cheatMode = false;
	
	public static boolean mouseControl = false;
	
	public static boolean p2AI = false;
	public static double gameSetSpeed = 5; //speeds up or slows down all aspects of the game
	public static double gameSpeed;
	private static Object_draw drawer;
	private static Paddle farPaddle;
	private static Paddle nearPaddle;
	public static Ball ball;
	public static ScoreBoard fScore, nScore;
	public static double paddleSpeed = 4 * gameSpeed, ballSpeed = 5 * gameSpeed, AI_difficulty = 1;
	private static SpeedTimer keyStrokeTimer1, keyStrokeTimer2;
	protected static Physics_frame frame;
	private static String[] yesOrNo = {"yes","no"};
	
	public static double diagonal = Math.sqrt(Math.pow(Settings.width,2) + Math.pow(Settings.height,2));
	
	public static void main(String[] args) {
		
		run();
	}
	
	public static void run() {
		
		Settings.timeSpeed = 1;
		Settings.targetFPS = 60;
//		Settings.elasticity = 1;
		Settings.perspective = true;
		
		drawer = new Object_draw();
		frame = drawer.getFrame();
		drawer.setVisible(false);

		
		gameSpeed = gameSetSpeed * diagonal/100;

		drawer.getFrame().setColor(Color.BLACK);

		drawer.getFrame().setTitle("Pong: V" + Version + "         Programmed by Alec Pannunzio");
		
		nearPaddle = new Paddle(drawer,"near");
//		nearPaddle.setName("player",1);
		
	
		farPaddle = new Paddle(drawer,"far");
//		farPaddle.setName("AI",1);
		
		
		ball = new Ball(drawer);
//		ball.setName("pong ball",1);
		
		
		
		FPS_display fps = new FPS_display(drawer,30,30);
		fps.setColor(Color.GRAY);
	
		FCPS_display fcps = new FCPS_display(drawer,30,50);
		fcps.setColor(Color.GRAY);
	
		nScore = new ScoreBoard(drawer, 0.3 * Settings.width, 0.1 * Settings.height,"",0);
		nScore.setFont(new Font("TimesRoman", Font.BOLD, 70));
		nScore.setColor(Color.green);
		
		fScore = new ScoreBoard(drawer, 0.7 * Settings.width, 0.1 * Settings.height,"",0);
		fScore.setFont(new Font("TimesRoman", Font.BOLD, 70));
		fScore.setColor(Color.green);

//		borders = new border_bounce(drawer);
//		borders.isVisible = false;
		
		setSettings();
		
		drawer.setSize(Settings.width + 1,Settings.height + 1);
		
		resize();
		
//		ball.isAnchored = true; //hold the ball till the game starts
		
		drawer.add(nearPaddle);
		drawer.add(farPaddle);
		drawer.add(ball);
		drawer.add(fps);
		drawer.add(fcps);
		drawer.add(nScore);
//		drawer.add(borders);
		drawer.add(fScore);
		
	
//		PongStartScreen startScreen = new PongStartScreen(drawer);
		

		drawer.start();
		
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		drawer.setVisible(true);
		
		drawer.pause();
		
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		
		init();  //start the game
		
//		startScreen.goAway();
		
		resize();
		drawer.resume();
		
		//mouse motion listener
		drawer.addMouseMotionListener( new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {	

			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
				mouseControl = true;

				nearPaddle.setSpeed(new Vector3D(gameSpeed * (e.getX() - nearPaddle.getX() ) /10,gameSpeed * (-nearPaddle.getY() + e.getY())/10,0));
		
			}
			
		});
		
		//key listener
		drawer.addKeyListener(new KeyListener() {
			   
			@Override
	         public void keyPressed(KeyEvent e) {
	        	  drawer.inactivity_timer = 0;      	     	
	        	  
	        	  mouseControl = false;
//	        	  nearPaddle.affectedByBorder = true;
					
	        	  //player1
	        	  switch (e.getKeyCode()) {
	        	  	case(87): //w
	        	  		drawer.remove(keyStrokeTimer1);
	        	  		nearPaddle.setSpeed(new Vector3D( (((Vector3D) nearPaddle.getSpeed()).getI()), -paddleSpeed, 0));
	        	  		break;
	        	  	
	        	  	case(65): //a
	        	  		drawer.remove(keyStrokeTimer1);
	        	  		nearPaddle.setSpeed(new Vector3D( -paddleSpeed, ((Vector3D) nearPaddle.getSpeed()).getJ(), 0));
	        	  		
	        	  	break;
	        	  	
	        	  	case(83): //s
	        	  		drawer.remove(keyStrokeTimer1);
	        	  		nearPaddle.setSpeed(new Vector3D( ((Vector3D) nearPaddle.getSpeed()).getI(), paddleSpeed, 0));
	        	  		
	        	  	break;
	        	  	
	        	  	case(68): //d
	        	  		drawer.remove(keyStrokeTimer1);
	        	  		nearPaddle.setSpeed(new Vector3D(paddleSpeed, ((Vector3D) nearPaddle.getSpeed()).getJ(), 0));
	        	  	break;
	        	  }
	        	  
	        	  //special keys
	        	  switch (e.getKeyCode()) {
	        	  
	        	  	case(32): //Space
	        	  		drawer.pause();
	        	  
	        	  	break;
	        	  	
	        	  	case(10): //Enter
	        	  		drawer.resume();
	        	  	break;
	        	  	
	        	  	case (72): // H
	        	  		drawer.pause();
	        	  		JOptionPane.showMessageDialog(frame, "Press H for this screen \n press / to enter a command \n P1 Controls: WASD \n P2 Controls: Arrow keys \n Press Space to pause \n press Enter to resume", "Pong  V " + Version + "    Programed by Alec Pannunzio", 3);
	        	  		drawer.resume();
	        	  	break;
	        		
	        	  	case(74): //J
	        	  		drawer.pause();
	        	  		JOptionPane.showMessageDialog(frame, "\npress / to enter a command \nHit the ball while moving up/down to put spin on it \nHit the ball while moving left/right to make it go faster \nDon't hit the ball at all to lose the point ;)", "Pong  V " + Version + "    Programed by Alec Pannunzio", 3);
	        	  		drawer.resume();
	        	  	break;
	        	  		        	  	
	        	  	case(47): // /
	        	  		drawer.pause();
	        	  		doCommand();
	        	  		drawer.resume();
	        	  	break;
	        	  		
	        	  }
	        	  
	        	  //player2
	        	  if (! p2AI) {
		        	  switch (e.getKeyCode()) {
			        	  case(38): //Up
			        	  	drawer.remove(keyStrokeTimer2);
			        	  	farPaddle.setSpeed(new Vector3D( 0, -paddleSpeed, 0));
			        	  	break;
			        	  	
			        	  case(40): //Down
				        	  	drawer.remove(keyStrokeTimer2);
				        	  	farPaddle.setSpeed(new Vector3D(0, paddleSpeed, 0));
				        	  	break;
				        	  	
			        	  case(37): //Left
				        	  	drawer.remove(keyStrokeTimer2);
				        	  	farPaddle.setSpeed(new Vector3D(-paddleSpeed, 0, 0));
				        	  	break;
				        	  	
			        	  case(39): //right
				        	  	drawer.remove(keyStrokeTimer2);
				        	  	farPaddle.setSpeed(new Vector3D(paddleSpeed, 0, 0));
				        	  	break;
			        	  	
		        	  }
	        	  }
	          }

			@Override
			public void keyReleased(KeyEvent arg0) {
				drawer.inactivity_timer = 0;
				
				int key = arg0.getKeyCode();
				
				//add timers so that the paddle will stop when the key is released
				if ((key == 87) || (key == 65) || (key == 83) || (key == 68)) {
					keyStrokeTimer1 = new SpeedTimer(drawer,10/paddleSpeed,TimerUnits.seconds,0,0,0,nearPaddle);
					drawer.add(keyStrokeTimer1);
				}else if ((key == 38) || (key == 40) || (key == 37) || (key == 39)) {
					keyStrokeTimer2 = new SpeedTimer(drawer,10/paddleSpeed,TimerUnits.seconds,0,0,0,farPaddle);
					drawer.add(keyStrokeTimer2);
				}
				
				
			}
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				drawer.inactivity_timer = 0;
				
			}
			
	      });
		
		
		//wait for the user to close the window to end the game
		while (frame.isShowing()) {
			try {
				Thread.sleep(200);
			
				if (mouseControl) nearPaddle.setSpeed(new Vector3D(0, 0, 0));
				farPaddle.setSpeed(0, 0, 0);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		drawer.stop();
		frame.remove(drawer);
		frame.dispose();
	
	}
	
	public static void init() {
		
		JOptionPane.showMessageDialog(frame, "Press H for this screen \n press J for advanced instructions \n P1 Controls: WASD \n P2 Controls: Arrow keys \n Press Space to pause \n press Enter to resume", "Pong  V " + Version + "    Programed by Alec Pannunzio", 3);
		
		
		
		int p2 = JOptionPane.showOptionDialog(frame, "Is player 2 a Human?", "P2", 1, 1, null, yesOrNo, 1);
		System.out.println(p2);
		if (p2 == 1) {
			
			setAIDiff(); //set the difficulty
			
			p2AI = true;
			System.out.println("Pong Terminator Uploaded");
			
		}else{
			p2AI = false;
			
			System.out.println("P2 Pilot Systems Uploaded");
			
			farPaddle.setSpeed(0, 0, 0);  //stopping the paddle
		}
		
//		ball.isAnchored = false; //releasing the ball to start the game
	}
	
//	public static void setDrawer(object_draw drawer1) {
//		drawer = drawer1;
//		drawer.setFrame(frame);
//	}

	public static void setAIDiff() {
		String[] difficulties = {"Easy","Normal","Hard","EXTREME"};
		int p2AI_diff_input = JOptionPane.showOptionDialog(frame, "What AI difficulty", "Difficulty", 1, 1, null, difficulties, 1);
		
		switch(p2AI_diff_input) {
			case(0):
				System.out.println("Difficulty: Easy");
				AI_difficulty = 0.45;
				gameSetSpeed *= 1.5;
	
				break;
			
			case(1):
				System.out.println("Difficulty: Normal");
				AI_difficulty = 0.6;
				gameSetSpeed *= 2;
		
				break;
			
			case(2):
				System.out.println("Difficulty: Hard");
				AI_difficulty = 1;
				gameSetSpeed *= 3;
		
				break;
				
			case(3):
				System.out.println("Difficulty: EXTREME!!");
				AI_difficulty = 1.6;
				gameSetSpeed *= 3.5;
		
				break;
		}
		
		ball.Update(1);
	}
	
	public static void reColor(Color primary, Color secondary, Color tertiary) {
//		frame.setColor(primary);
		
		nearPaddle.setColor(secondary);
		farPaddle.setColor(secondary);
		ball.setColor(secondary);
		
		nScore.setColor(tertiary);
		fScore.setColor(tertiary);
		
//		drawer.setFrame(frame);
		
	}
	
	public static void doCommand() {
		String[] commandsStr = {"/help","/AIDifficulty","/cheatMode","/gameSpeed","/switchColors"};

		
		String command = JOptionPane.showInputDialog(frame, "Command \n Type /help for a list of possible commands");
		
		switch(command) {
			case("/help"):
				JOptionPane.showMessageDialog(frame, commandsStr);
				break;
				
			case("/AIDifficulty"):
				setAIDiff();
				break;
				
			case("/cheatMode"):
				if (JOptionPane.showOptionDialog(frame, "Do you want cheat mode on?", "cheatMode", 2, 1, null, yesOrNo, 1) == 0) {
					cheatMode = true;
				}else {
					cheatMode = false;
				}
				break;
				
			case("/gameSpeed"):
				try {
					gameSetSpeed = Double.parseDouble(JOptionPane.showInputDialog(frame, "What do you want the gameSpeed to be?"));
					resize();
				}catch(NumberFormatException n) {
					JOptionPane.showMessageDialog(frame, "Invalid gameSpeed","Error", 0);
				}
				break;
			case("/switchColors"):
				reColor(Physics_engine_toolbox.getColorFromUser(frame),Physics_engine_toolbox.getColorFromUser(frame),Physics_engine_toolbox.getColorFromUser(frame));
				break;
				
			default:
				JOptionPane.showMessageDialog(frame, "Either the command you typed doesn't exist or it hasn't been programmed yet","Command Not found", 0);
				break;
		}
		
		resize();
	}
	
	public static void setSettings() {
//		Settings.collision_algorithm = 5;
//		Settings.rotationAlgorithm = 6;
//		Settings.timeOutTime = 1000;

		
		Settings.distanceFromScreenMeters = 0.0001;
		
		Settings.width = 1400;
		Settings.height = 1000;
		Settings.depth = 5000;
	}
	
	public static void resize() {
		
		System.out.println("resizing pong");
		
		diagonal = Math.sqrt(Math.pow(Settings.width,2) + Math.pow(Settings.height,2));
		
//		borders.resize();
		
		nScore.setPos(0.2 * Settings.width, 0.1 * Settings.height);
		fScore.setPos(0.7 * Settings.width, 0.1 * Settings.height);
		
		
		
		//make the game faster when the frame gets bigger
		gameSpeed = gameSetSpeed * diagonal/100;
		paddleSpeed = 4 * gameSpeed;
		ballSpeed = 5 * gameSpeed;
	
		
		ball.reset();
		
//		drawer.resize();
		
	}
}
