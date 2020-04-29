package jetpack_joyride;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import apgraphicslib.Object_draw;
import apgraphicslib.Settings;

public class JPJR_Shop extends JFrame {
	
	private JLabel label;
	private JButton RedJetpackButton,BlueJetpackButton,GreenJetpackButton,doubleCoinsButton,upgradeJetpackButton,clearPurchasesButton;
	public Container cp;
	
	private final JButton[] boughtButtons1 = {};
	public ArrayList<JButton> boughtButtons = new ArrayList<JButton>(Arrays.asList(boughtButtons1));
	
	private Object_draw drawer;
	
	public JPJR_Shop(Object_draw drawer1) {
		drawer = drawer1;
	}
	
	public void transferPurchases() {
		
		//settings all buttons to default state
		RedJetpackButton.setText("Buy Red Jetpack \n (10 coins)");
		doubleCoinsButton.setText("Double Coins! \n (200 coins)");
		GreenJetpackButton.setText("Buy Green Jetpack \n (10 coins)");
		upgradeJetpackButton.setText("Upgrade Jetpack Power \n (30 coins)");
		
		//transferring purchases from previous games
		for (JButton current_button : boughtButtons) {

			//Red
			if (current_button.getText() != "Buy Red Jetpack \n (10 coins)") {
				RedJetpackButton.setText("Equip Red Jetpack");
			}
			
			//Green
			if (current_button.getText() != "Buy Green Jetpack \n (10 coins)") {
				GreenJetpackButton.setText("Equip Green Jetpack");
			}
			
			//upgrade jetpack button
			if (current_button.getText() != "Upgrade Jetpack Power \n (30 coins)") {
				if (current_button.getText() == "Equip upgraded Jetpack") {
					upgradeJetpackButton.setText("Equip upgraded Jetpack");
				}else {
					upgradeJetpackButton.setText("Unequip upgraded Jetpack");
					JetPack_JoyRide.jetpack.power += 10;
					JetPack_JoyRide.jetpack.fireSize += 0.2;
				}
			}
			
			//double coins
			if (current_button.getText() != "Double Coins! \n (200 coins)")  {
				doubleCoinsButton.setText("Double coins already bought");
				drawer.add( new Coin(drawer,960, 300) );
				drawer.add( new Coin(drawer,1060, 350) );
				drawer.add( new Coin(drawer,1160, 400) );
			}
			
		}
	}
	public void init() {
		setSize(Settings.width/4,Settings.height/2);
		setTitle("Jetpack Joyride V" + JetPack_JoyRide.version + " Shop  -  By Alec Pannunzio");
		setLocation(Settings.width + 20, 25 + Settings.height/2);
		cp = getContentPane();	
		cp.setBackground(Color.CYAN);	
		cp.setLayout(new FlowLayout());
		

		clearPurchasesButton = new JButton("Clear Purchases");
		clearPurchasesButton.addActionListener(new clearPurchases());
		
		BlueJetpackButton = new JButton("Equip Blue Jetpack");
		BlueJetpackButton.addActionListener(new BlueJetpack());
	
		RedJetpackButton = new JButton("Buy Red Jetpack \n (10 coins)");
		RedJetpackButton.addActionListener(new buyRedJetpack());	
		
		GreenJetpackButton = new JButton("Buy Green Jetpack \n (10 coins)");
		GreenJetpackButton.addActionListener(new buyGreenJetpack());	
		
		upgradeJetpackButton = new JButton("Upgrade Jetpack Power \n (30 coins)");
		upgradeJetpackButton.addActionListener(new upgradeJetpack());	
		
		doubleCoinsButton = new JButton("Double Coins! \n (200 coins)");
		doubleCoinsButton.addActionListener(new doubleCoinsButton());	

		transferPurchases(); 
		
		cp.add(RedJetpackButton);
		cp.add(GreenJetpackButton);
		cp.add(BlueJetpackButton);
		cp.add(doubleCoinsButton);
		cp.add(upgradeJetpackButton);
		cp.add(clearPurchasesButton);
	}
	
	private class BlueJetpack implements ActionListener {
		public void actionPerformed(ActionEvent event) {				
			JetPack_JoyRide.jetpack.setColor(Color.BLUE);		
		}
	}
	
	private class clearPurchases implements ActionListener {
		public void actionPerformed(ActionEvent event) {				
			boughtButtons.clear();	
			transferPurchases();
			JetPack_JoyRide.jetpack.setColor(Color.blue);
		}
	}
	
	private class buyRedJetpack implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			if (RedJetpackButton.getText() == "Buy Red Jetpack \n (10 coins)") {
				if (JetPack_JoyRide.coins >= 10) {
					boughtButtons.add(RedJetpackButton);
					JetPack_JoyRide.jetpack.setColor(Color.RED);
					RedJetpackButton.setText("Equip Red Jetpack");
					JetPack_JoyRide.coins -= 10;	
					JetPack_JoyRide.coinScore.setScore(JetPack_JoyRide.coins);
					JetPack_JoyRide.drawer.repaint();
				}else {
					JOptionPane.showMessageDialog(JetPack_JoyRide.shop, "You don't have enough money for this", "insufficient funds", 1,JetPack_JoyRide.coin_img);
				}
			}else {
				JetPack_JoyRide.jetpack.setColor(Color.RED);				
			}	
		}
	}
	
	private class upgradeJetpack implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			if (upgradeJetpackButton.getText() == "Upgrade Jetpack Power \n (10 coins)") {
				if (JetPack_JoyRide.coins >= 30) {
					boughtButtons.add(upgradeJetpackButton);
					JetPack_JoyRide.jetpack.power += 10;
					JetPack_JoyRide.jetpack.fireSize += 0.2;
					upgradeJetpackButton.setText("Unequip Upgraded Jetpack");
					JetPack_JoyRide.coins -= 30;	
					JetPack_JoyRide.coinScore.setScore(JetPack_JoyRide.coins);
					JetPack_JoyRide.drawer.repaint();
					cp.repaint();
				}else {
					JOptionPane.showMessageDialog(JetPack_JoyRide.shop, "You don't have enough money for this", "insufficient funds", 1,JetPack_JoyRide.coin_img);
				}
			}else if (upgradeJetpackButton.getText() == "Unequip Upgraded Jetpack") {
				JetPack_JoyRide.jetpack.power -= 10;	
				JetPack_JoyRide.jetpack.fireSize -= 0.2;
				upgradeJetpackButton.setText("Equip Upgraded Jetpack");
				cp.repaint();
			}else {
				upgradeJetpackButton.setText("Unequip Upgraded Jetpack");
				JetPack_JoyRide.jetpack.power += 10;
				JetPack_JoyRide.jetpack.fireSize += 0.2;
				cp.repaint();
			}
		}
	}
	
	private class buyGreenJetpack implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			if (GreenJetpackButton.getText() == "Buy Green Jetpack \n (10 coins)") {
				if (JetPack_JoyRide.coins >= 10) {
					boughtButtons.add(GreenJetpackButton);
					JetPack_JoyRide.jetpack.setColor(Color.GREEN);
					GreenJetpackButton.setText("Equip Green Jetpack");
					JetPack_JoyRide.coins -= 10;	
					JetPack_JoyRide.coinScore.setScore(JetPack_JoyRide.coins);
					JetPack_JoyRide.drawer.repaint();
				}else {
					JOptionPane.showMessageDialog(JetPack_JoyRide.shop, "You don't have enough money for this", "insufficient funds", 1,JetPack_JoyRide.coin_img);
				}
			}else {
				JetPack_JoyRide.jetpack.setColor(Color.GREEN);				
			}	
		}
	}
	
	private class doubleCoinsButton implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			if (doubleCoinsButton.getText() == "Double Coins! \n (200 coins)") {
				if (JetPack_JoyRide.coins >= 200) {
					boughtButtons.add(doubleCoinsButton);
					doubleCoinsButton.setText("Double coins already bought");
					JetPack_JoyRide.coins -= 200;	
					JetPack_JoyRide.coinScore.setScore(JetPack_JoyRide.coins);
					JetPack_JoyRide.drawer.repaint();
					drawer.add( new Coin(drawer,960, 300) );
					drawer.add( new Coin(drawer,1060, 350) );
					drawer.add( new Coin(drawer,1160, 400) );
				}else {
					JOptionPane.showMessageDialog(JetPack_JoyRide.shop, "You don't have enough money for this", "insufficient funds", 1,JetPack_JoyRide.coin_img);	
				}
			}	
		}
	}
	
}
