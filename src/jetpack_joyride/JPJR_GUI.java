package jetpack_joyride;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Physics_engine.Settings;

public class JPJR_GUI extends JFrame{
	private JLabel label;
	private JButton resetJetpack,restart,pause,endGame,resetJetpackPos,openShop,increaseSpeedButton;
	public Container cp;
	
	
	public void init() {
		setSize(Settings.width/4,Settings.height/2);
		setTitle("Jetpack Joyride V" + JetPack_JoyRide.version + " GUI  -  By Alec Pannunzio");
		
		setLocation(Settings.width + 20, 20);
		cp = getContentPane();	
		cp.setBackground(Color.CYAN);	
		cp.setLayout(new FlowLayout());
		
		resetJetpack = new JButton("Reset Jetpack");
		resetJetpack.addActionListener(new ResetJetpackButton());
		
		resetJetpackPos = new JButton("Reset Jetpack Position");
		resetJetpackPos.addActionListener(new ResetJetpackPosButton());
		
		pause = new JButton("Pause");
		pause.addActionListener(new PauseButton());
		
		restart = new JButton("Restart");
		restart.addActionListener(new restartButton());
		
		increaseSpeedButton = new JButton("Increase speed by 5");
		increaseSpeedButton.addActionListener(new increaseSpeed());
		
		endGame = new JButton("End Game");
		endGame.addActionListener(new endGame());
		
		openShop = new JButton("Open Shop");
		openShop.addActionListener(new openShop());
		
		cp.add(openShop);
		cp.add(resetJetpack);
		cp.add(resetJetpackPos);
		cp.add(pause);
		cp.add(restart);
		cp.add(endGame);
		cp.add(increaseSpeedButton);
		

	}
		
	
	private class openShop implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {			
			if (openShop.getText() == "Open Shop") {
				openShop.setText("Close Shop");
				JetPack_JoyRide.shop.setVisible(true);
				JetPack_JoyRide.pause = true;
			}else {
				JetPack_JoyRide.shop.setVisible(false);
				openShop.setText("Open Shop");		
				JetPack_JoyRide.pause = false;
			}	
		}	
	}
	
	private class ResetJetpackButton implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JetPack_JoyRide.jetpack.setAccel(0, 0, 0);
			JetPack_JoyRide.jetpack.setSpeed(0, 0, 0);
			JetPack_JoyRide.jetpack.applyComponentForce(0, 9.8, 0);
		}	
	}
	
	private class ResetJetpackPosButton implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JetPack_JoyRide.jetpack.setPos(Settings.width/2,Settings.height - 100, 0);
		}	
	}
	
	private class increaseSpeed implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JetPack_JoyRide.jetpack_speed += 5;
		}	
	}
	
	private class PauseButton implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			if (pause.getText() == "Pause") {
				JetPack_JoyRide.pause = true;
				pause.setText("Resume");
			}else {
				JetPack_JoyRide.pause = false;
				pause.setText("Pause");				
			}	
		}
	}	
	
	private class restartButton implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JetPack_JoyRide.resetGame();
		}	
	}
	
	private class endGame implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JetPack_JoyRide.game_over = 4;
		}	
	}
		
		
		
}

