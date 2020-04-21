package LegendOfJava;

import java.awt.Color;
import javax.swing.JOptionPane;
import apgraphicslib.FCPS_display;
import apgraphicslib.FPS_display;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class LegendOfJavaRunner {
	public static Object_draw drawer;
	public static MainCharacter Ryan;
	
	private static int roomPPSize = 10, playerPPSize = 5;
	
	private static Room currentRoom;
	
	public static void main(String[] args) {
		//set the Settings:
		Settings.perspective = true;
		Settings.frameTime = 1D/30; //attempt to make the FPS 30
		
		
		
		drawer = new Object_draw();
		
		drawer.getFrame().setBackground(Color.black);
		
//		drawer.add(new Object_border_tether(drawer));
		
		drawer.getFrame().setVisible(false);
		
		drawer.add(new FPS_display(drawer, Settings.width * 0.01, Settings.height*0.05));
		drawer.add(new FCPS_display(drawer, Settings.width * 0.01, Settings.height*0.05 + 15));
		Ryan = new MainCharacter(drawer);
		Ryan.setName("Ryan");
		
		JOptionPane.showMessageDialog(drawer.getFrame(), "This program is under development and is not part of A8.");
		
		Room room1 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2);
		Room room2 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2);
		room2.addRoomOb(new EnemyCharacter(Ryan, 0.6 * Settings.width, Settings.height - PlayerTorso.torsoYSize - PlayerHead.headYSize-50, Settings.depth));
		Room room3 = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2);
		Room endRoom = new Room(drawer, Ryan, 3 * Settings.width, 2 * Settings.height/2);
		 
		room1.nextRoom = room2;
		room2.nextRoom = room3;
		room3.nextRoom = endRoom;
		
		currentRoom = room1;
		
		
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
			roomPPSize = 5;
			playerPPSize = 3;
		}else if (options[graphicsSetting].equals("extreme")) {
			roomPPSize = 1;
			playerPPSize = 1;
		}
		
		Room cRoom = room1;
		while (cRoom != null) {
			cRoom.setPPSize(roomPPSize);
			cRoom = cRoom.nextRoom;
		}
		
		Ryan.setPPSize(playerPPSize);
		

		
		System.out.println("RyanZ: " + Ryan.getZ());
		System.out.println("RoomZ: " + room1.getZ());
		
	
		room1.run();
		
		System.out.println("loading complete.");
		drawer.start();
		
		JOptionPane.showMessageDialog(drawer.getFrame(),"Controls: \n Move: WASD");
		
		drawer.getFrame().setVisible(true);
		
		while (drawer.getFrame().isActive()) {
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

	public static void loadNextRoom() {
		System.out.println("loading next room...");
		currentRoom.remove();
		drawer.pause();
		currentRoom = currentRoom.nextRoom;
		if (currentRoom == null) {
			JOptionPane.showMessageDialog(drawer, "Game over\nYou win!");
		}else {
			currentRoom.run();
			drawer.resume();
		}
	}

}
