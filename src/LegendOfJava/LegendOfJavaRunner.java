package LegendOfJava;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import apgraphicslib.APLabel;
import apgraphicslib.FCPS_display;
import apgraphicslib.FPS_display;
import apgraphicslib.Object_border_tether;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class LegendOfJavaRunner {
	public static Object_draw drawer;
	public static MainCharacter Ryan;
	public static APLabel console;
	
	private static int roomPPSize = 10, playerPPSize = 5;
	private static HealthBar hpBar;
	private static Room currentRoom;
	private static Room endRoom;
	
	public static void main(String[] args) {
		//set the Settings:
		Settings.perspective = true;
		Settings.advancedRotation = true;
		Settings.targetFPS = 20; //attempt to make the FPS 20
		Settings.width = 1000;
		Settings.height = 800;
		Settings.depth = 1000;
	//	Settings.autoResizeFrame = false;
		
		
		drawer = new Object_draw();
		
		drawer.getFrame().setBackground(Color.black);
		
//		drawer.add(new Object_border_tether(drawer));
		
		drawer.getFrame().setVisible(false);
		
		FPS_display fpsView = new FPS_display(drawer, Settings.width * 0.01 + 28, Settings.height*0.05);
		fpsView.setColor(Color.white);
		FCPS_display fcpsView = new FCPS_display(drawer, Settings.width * 0.01 + 43, Settings.height*0.05 + 15);
		fcpsView.setColor(Color.white);
		drawer.add(fpsView);
		drawer.add(fcpsView);
		
		console = new APLabel(drawer,Settings.width * 0.5, Settings.height * 0.1);
		console.setColor(Color.cyan);
		drawer.add(console);
		console.setMessage("The Legend of JAVA");
		console.setFont(new Font("ZapfDingbats",Font.BOLD,30));
		
		Ryan = new MainCharacter(drawer);
		Ryan.setName("Ryan");
		
		hpBar = new HealthBar(drawer, Settings.width * 0.01, Settings.height * 0.1, Ryan,25);
		drawer.add(hpBar);
		
		try {
			JOptionPane.showMessageDialog(drawer.getFrame(), "Welcome to the Legend of Java. \n If something doesn't work, try re-starting the program and/or a different graphics setting.");
		}catch(HeadlessException h) {
			System.out.println("your machine does not support Legend of Java. Please use your personal computer");
		}
		
		Room room0 = new Room(drawer, Ryan, 2 * Settings.width, 2 * Settings.height/2,3.5 * Settings.width);
		room0.setName("Room 0");
		
		Room room1 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		room1.addRoomOb(new EnemyCharacter(Ryan, 0.5 * Settings.width, Settings.depth));
		room1.setName("Room 1");
		
		Room room1L = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		room1L.addRoomOb(new EnemyCharacter(Ryan, 0.8 * Settings.width, Settings.depth));
		room1L.addRoomOb(new EnemyCharacter(Ryan, 0.2 * Settings.width, Settings.depth));
		room1.setLeftRoom(room1L);
		room1L.setName("Room 1 L");
		
		Room room1L2 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		room1L2.addRoomOb(new EnemyCharacter(Ryan, 0.8 * Settings.width, -Settings.depth/3));
		room1L2.addRoomOb(new EnemyCharacter(Ryan, 0.2 * Settings.width, Settings.depth));
		room1L.setNextRoom(room1L2);
		room1L2.setName("Room 1 L 2");
		
		Room room2 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		room2.addRoomOb(new EnemyCharacter(Ryan, 0.8 * Settings.width, Settings.depth));
		room2.addRoomOb(new EnemyCharacter(Ryan, 0.2 * Settings.width, Settings.depth));
		room2.setName("Room 2");
		
		Room room3 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		room3.addRoomOb(new EnemyCharacter(Ryan, 0.8 * Settings.width, Settings.depth));
		room3.addRoomOb(new EnemyCharacter(Ryan, 0.2 * Settings.width, Settings.depth));
		room3.addRoomOb(new EnemyCharacter(Ryan, 0.5 * Settings.width, -0.5 * Settings.depth));
		room3.setName("Room 3");
		room3.setLeftRoom(room1L2);
		
		Room room4 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		room4.setName("This is the last room. \n You win!");
		
		endRoom = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2,3 * Settings.width);
		endRoom.setName("End");
		
		room0.setNextRoom(room1);
		room1.setNextRoom(room2);
		room2.setNextRoom(room3);
		room3.setNextRoom(room4); 
		room4.setNextRoom(endRoom);
		
		currentRoom = room0;
		
		
		//getting graphics settings
		String[] options = {"terrible","low", "medium", "high", "extreme"};
		int graphicsSetting = JOptionPane.showOptionDialog(drawer.getFrame(), "What graphics level do you want?", "Graphics settings", 0, 0, null,options, 1);
		
		if (options[graphicsSetting].equals("terrible")) {
			roomPPSize = 20;
			playerPPSize = 15;
		}else if (options[graphicsSetting].equals("low")) {
			roomPPSize = 15;
			playerPPSize = 10;
		}else if (options[graphicsSetting].equals("medium")) {
			roomPPSize = 10;
			playerPPSize = 5;
		}else if (options[graphicsSetting].equals("high")) {
			roomPPSize = 6;
			playerPPSize = 3;
		}else if (options[graphicsSetting].equals("extreme")) {
			roomPPSize = 2;
			playerPPSize = 1;
		}
		
		setRoomPPSizeRecursive(room0,roomPPSize);
			
		
		Ryan.setPPSize(playerPPSize);
		

		
		System.out.println("RyanZ: " + Ryan.getZ());
		System.out.println("RoomZ: " + room1.getZ());
		
	
		room0.run();
		
		System.out.println("loading complete.");
		drawer.start();
		
		JOptionPane.showMessageDialog(drawer.getFrame(),"Controls: \n Move: WASD \n Attack: SPACE \n Dodge: SHIFT ");
		
		drawer.getFrame().setVisible(true);
		
		JOptionPane.showMessageDialog(drawer, "Proceed to the door to enter the first room");
	
		while (drawer.getFrame().isShowing()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		
		drawer.stop();
		System.exit(1);
	}
	
	
	//yayyy recursion
	private static void setRoomPPSizeRecursive(Room cRoom, int ppSize) {
		
		if (! cRoom.ppSizeSet) { // if we haven't already been to this node
			System.out.println("Set platePointSize of " + cRoom.getName());
			cRoom.setPPSize(ppSize);
			if (cRoom.nextRoom != null) {
				setRoomPPSizeRecursive(cRoom.nextRoom, ppSize);
			}
			
			if (cRoom.leftRoom != null) {
				setRoomPPSizeRecursive(cRoom.leftRoom, ppSize);
			}
			
			if (cRoom.rightRoom != null) {
				setRoomPPSizeRecursive(cRoom.rightRoom, ppSize);
			}
			
			if (cRoom.prevRoom != null) {
				setRoomPPSizeRecursive(cRoom.prevRoom, ppSize);
			}
		}
	}

	public static void loadRoom( Room nextRoom) {
		System.out.println("loading next room...");
		
		drawer.pause();
		Ryan.swordArm.swinging = false; //stop any sword swings
		currentRoom.remove();
		currentRoom = nextRoom;
		console.setMessage(currentRoom.getName());
		if (currentRoom.equals(endRoom)) {
			drawer.getFrame().setVisible(false);
			drawer.stop();
			JOptionPane.showMessageDialog(drawer, "Game over\nYou win!");
			System.exit(1);
		}else {
			currentRoom.run();
			drawer.resume();
		}
	}

}
