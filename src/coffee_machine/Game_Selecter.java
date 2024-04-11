package coffee_machine;

import javax.swing.JOptionPane;

import LegendOfJava.LegendOfJavaRunner;
import LegendOfJava2.LegendOfJava2;
import essay_writer.Essay_Generator;
import jetpack_joyride.JetPack_JoyRide;
import pong.Pong_runner;

public class Game_Selecter {
	public static void main(String[] args) {
		System.out.println("Game Selector!!");
		
		String[] gameOptions = new String[] {"Jetpack Joyride","3D Pong","Legend of Java 1", "Legend of Java 2","Essay Writer", "Exit"};
				
		
		
		boolean exit = false;
		
		while(! exit) {
			int gameSelection = JOptionPane.showOptionDialog(null, "What game do you want to play?", "Game Selector", 0, 0, null, gameOptions, 0);
			
			if (gameOptions[gameSelection].equals("Jetpack Joyride")) {
				new JetPack_JoyRide();
			}else if (gameOptions[gameSelection].equals("3D Pong")) {
				Pong_runner.run();
			}else if (gameOptions[gameSelection].equals("Legend of Java 1")) {
				new LegendOfJavaRunner();
			}else if (gameOptions[gameSelection].equals("Legend of Java 2")) {
				new LegendOfJava2();
			}else if (gameOptions[gameSelection].equals("Essay Writer")) {
				new Essay_Generator();
			}else {
				exit = true;
			}
		}
		
	}
}
