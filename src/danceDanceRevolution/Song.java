package danceDanceRevolution;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import apgraphicslib.AudioManager;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Physics_object;
import apgraphicslib.Settings;

public class Song extends Physics_object implements KeyListener{
	
	//if these arrows on the mat are pressed down 
	private boolean left = false, down = false, up = false, right = false;
	
	private LinkedList<LeftNote> leftNotes = new LinkedList<LeftNote>();
	private LinkedList<DownNote> downNotes = new LinkedList<DownNote>();
	private LinkedList<UpNote> upNotes = new LinkedList<UpNote>();
	private LinkedList<RightNote> rightNotes = new LinkedList<RightNote>();
	private LinkedList<Note> allNotes = new LinkedList<Note>();
	
	LeftNote leftNoteTarget;
	DownNote downNoteTarget;
	UpNote upNoteTarget;
	RightNote rightNoteTarget;

	private String audioSrc, leftNotesStr, upNotesStr, downNotesStr, rightNotesStr;
	private double difficulty, tempo, startDiff;
	
	public Song(Object_draw drawer, String songSrc) {
		super(drawer);
		
		leftNoteTarget = new LeftNote(drawer,100,0,"./src/danceDanceRevolution/assets/arrowTargetTexture.png");
		downNoteTarget = new DownNote(drawer,100,0,"./src/danceDanceRevolution/assets/arrowTargetTexture.png");
		upNoteTarget = new UpNote(drawer,100,0,"./src/danceDanceRevolution/assets/arrowTargetTexture.png");
		rightNoteTarget = new RightNote(drawer,100,0,"./src/danceDanceRevolution/assets/arrowTargetTexture.png");
		
		allNotes.add(leftNoteTarget);
		allNotes.add(downNoteTarget);
		allNotes.add(upNoteTarget);
		allNotes.add(rightNoteTarget);
		 
		Scanner scan;
		try {
			scan = new Scanner(new File(songSrc));
			scan.useDelimiter(",");
			
			audioSrc = scan.next();
			difficulty = Double.parseDouble(scan.next());
			tempo = Double.parseDouble(scan.next());
			startDiff = Double.parseDouble(scan.next());
			scan.nextLine();
			leftNotesStr = scan.nextLine();
			downNotesStr = scan.nextLine();
			upNotesStr = scan.nextLine();
			rightNotesStr = scan.nextLine();
			
			addNotes();
			
			scan.close();
		} catch (FileNotFoundException e) {
			if (Settings.JOptionPaneErrorMessages) {
				JOptionPane.showMessageDialog(getDrawer(), "File " + songSrc + "not found");
			}else {
				getDrawer().out.println("File " + songSrc + "not found");
			}
			e.printStackTrace();
		}
		
	}

	public void addNotes() {
		double audioLatency = 0.1 * tempo;
		double noteDistMultiplier = Note.noteSize;
		double noteStart = Note.noteSize * 5 + audioLatency + startDiff * tempo;
		
		//leftNotes
		Scanner leftScan = new Scanner(leftNotesStr);
		leftScan.useDelimiter(",");
		
		LeftNote newLftNote;
		while (leftScan.hasNext()) {
			try {
				newLftNote = new LeftNote(getDrawer(),noteStart + Double.parseDouble(leftScan.next())*noteDistMultiplier, tempo, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
				leftNotes.add(newLftNote);
				allNotes.add(newLftNote);
				System.out.println("note added");
			}catch(NumberFormatException n ) {}
		}
		
		
		//downNotes
		Scanner downScan = new Scanner(downNotesStr);
		downScan.useDelimiter(",");
		
		DownNote newDwnNote;
		while (downScan.hasNext()) {
			try {
				newDwnNote = new DownNote(getDrawer(),noteStart + Double.parseDouble(downScan.next())*noteDistMultiplier, tempo, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
				downNotes.add(newDwnNote);
				allNotes.add(newDwnNote);
				System.out.println("note added");
			}catch(NumberFormatException n ) {}
		}
		
		
		//upNotes
		Scanner upScan = new Scanner(upNotesStr);
		upScan.useDelimiter(",");
		
		UpNote newUpNote;
		while (upScan.hasNext()) {
			try {
				newUpNote = new UpNote(getDrawer(),noteStart + Double.parseDouble(upScan.next())*noteDistMultiplier, tempo, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
				upNotes.add(newUpNote);
				allNotes.add(newUpNote);
				System.out.println("note added");
			}catch(NumberFormatException n ) {}
		}
		
		
		//rightNotes
		Scanner rightScan = new Scanner(rightNotesStr);
		rightScan.useDelimiter(",");
		
		RightNote newRgtNote;
		while (rightScan.hasNext()) {
			try {
				newRgtNote = new RightNote(getDrawer(),noteStart + Double.parseDouble(rightScan.next())*noteDistMultiplier, tempo, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
				rightNotes.add(newRgtNote);
				allNotes.add(newRgtNote);
				System.out.println("note added");
			}catch(NumberFormatException n ) {}
		}
		
		leftScan.close();
		downScan.close();
		upScan.close();
		rightScan.close();
		
	}
	
	public void play() {
		DDRRunner.currentSong = this;
		for (Note note : allNotes) {
			note.run();
		}
		getDrawer().start();
		AudioManager.playAudioFile(audioSrc);
		
		getDrawer().getFrame().getContentPane().addKeyListener(this);
		getDrawer().getFrame().getGlassPane().addKeyListener(this);
		getDrawer().getFrame().addKeyListener(this);
		getDrawer().addKeyListener(this);
	}
	
	

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == 37) { //LEFT
			if (! left) {
				left = true;
				for (Note n : leftNotes) {
					if (Physics_engine_toolbox.distance2D(n.getCoordinates(), leftNoteTarget.getCoordinates()) < Note.noteSize/4) {
						getDrawer().remove(n);
						leftNotes.remove(n);
						break;
					}
				}
			}
		}
		
		if (arg0.getKeyCode() == 40) { //DOWN
			if (! down) {
				down = true;
				for (Note n : downNotes) {
					if (Physics_engine_toolbox.distance2D(n.getCoordinates(), downNoteTarget.getCoordinates()) < Note.noteSize/4) {
						getDrawer().remove(n);
						downNotes.remove(n);
						break;
					}
				}
			}
		}
		
		if (arg0.getKeyCode() == 38) { //UP
			if (! up) {
				up = true;
				for (Note n : upNotes) {
					if (Physics_engine_toolbox.distance2D(n.getCoordinates(), upNoteTarget.getCoordinates()) < Note.noteSize/4) {
						getDrawer().remove(n);
						upNotes.remove(n);
						break;
					}
				}
			}
		}
		
		if (arg0.getKeyCode() == 39) { //RIGHT
			if (! right) {
				right = true;
				for (Note n : rightNotes) {
					if (Physics_engine_toolbox.distance2D(n.getCoordinates(), rightNoteTarget.getCoordinates()) < Note.noteSize/4) {
						getDrawer().remove(n);
						rightNotes.remove(n);
						break;
					}
				}
				
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == 37) { //LEFT
			left = false;
		}
		
		if (arg0.getKeyCode() == 40) { //DOWN
			down = false;
		}
		
		if (arg0.getKeyCode() == 38) { //UP
			up = false;
		}
		
		if (arg0.getKeyCode() == 39) { //RIGHT
			right = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "audioSrc: " + audioSrc + "\nDifficulty: " + difficulty + "\nTempo: " + tempo + "\nleftNotes: " + leftNotesStr + "\ndownNotes: " + downNotesStr + "\nupNotes: " + upNotesStr +  "\nrightNotes: " + rightNotesStr;
	}
}
