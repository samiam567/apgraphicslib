package danceDanceRevolution;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import apgraphicslib.AudioManager;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Physics_object;
import apgraphicslib.Settings;

public class Song extends Physics_object implements KeyListener{
	
	private double audioLatency = 2.6, noteStart, noteSpeed;
	//if these arrows on the mat are pressed down 
	private boolean left = false, down = false, up = false, right = false, noteCapturing = false;
	
	private LinkedList<LeftNote> leftNotes = new LinkedList<LeftNote>();
	private LinkedList<DownNote> downNotes = new LinkedList<DownNote>();
	private LinkedList<UpNote> upNotes = new LinkedList<UpNote>();
	private LinkedList<RightNote> rightNotes = new LinkedList<RightNote>();
	private LinkedList<Note> allNotes = new LinkedList<Note>();
	
	private enum NoteDirections {left, down, up, right};
	
	LeftNote leftNoteTarget;
	DownNote downNoteTarget;
	UpNote upNoteTarget;
	RightNote rightNoteTarget;

	private String songSrc, audioSrc, leftNotesStr, upNotesStr, downNotesStr, rightNotesStr;
	private double difficulty, tempo;
	private static double startDiff;
	
	public Song(Object_draw drawer, String songSrc) {
		super(drawer);
		loadSong(songSrc);
		
	}
		
	private void calculateNoteValues() {
		
		noteStart = Note.noteSize/2 - 100 + Note.noteSize * 4 + audioLatency * Note.noteSize + startDiff * Note.noteSize;
		noteSpeed = tempo * Note.noteSize / 60;
	}
	
	private void loadSong(String songSrc) {
		Object_draw drawer = getDrawer();
		
		
		this.songSrc = songSrc;
		Note.noteSize = Settings.width/5;
		leftNoteTarget = new LeftNote(drawer,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		
		downNoteTarget = new DownNote(drawer,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		upNoteTarget = new UpNote(drawer,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		rightNoteTarget = new RightNote(drawer,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		
		
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
			
			calculateNoteValues();
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

	private void logNote(NoteDirections direction, double noteFrame) {
		
		double noteDistMultiplier = Note.noteSize;
		
		double noteBeatPos = noteFrame / 60 / getDrawer().getActualFPS() * tempo /2 - 4;
		
		System.out.println("logging note (unrounded): " + noteBeatPos);
		
		noteBeatPos = ((double)Math.round(noteBeatPos*4))/4; //make 0.25 the smallest increment
		
		System.out.println("logging note: " + noteBeatPos);

		if (direction.equals(NoteDirections.left)) {				
			leftNotesStr += "" + noteBeatPos + ",";
		}else if (direction.equals(NoteDirections.down)) {
			downNotesStr += "" + noteBeatPos + ",";
		}else if (direction.equals(NoteDirections.up)) {
			upNotesStr += "" + noteBeatPos + ",";
		}else if (direction.equals(NoteDirections.right)) {
			rightNotesStr += "" + noteBeatPos + ",";
		}
						
		System.out.println("note logged");		
	}

	public Song(Object_draw drawer) {
		super(drawer);
		
		
		songSrc = "./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer,"What is the song name?") + ".dat";
		
		try { //if the song already exists we will try to load it in
			loadSong(songSrc);
			loadNotes();
		}catch(Exception e) {
			audioSrc = "./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer,"What is the audio file?") + ".wav";	
			tempo = Physics_engine_toolbox.getDoubleFromUser(getDrawer().getFrame(), "What is the tempo? (in bpm) look it up!");
		}
		
		calculateNoteValues();
		
		JOptionPane.showMessageDialog(getDrawer(), "Now capturing. Press ENTER to save capture");
		
		getDrawer().getFrame().getContentPane().addKeyListener(this);
		getDrawer().getFrame().getGlassPane().addKeyListener(this);
		getDrawer().getFrame().addKeyListener(this);
		getDrawer().addKeyListener(this);
		
		
		noteCapturing = true;
		
		AudioManager.playAudioFile(audioSrc);
		drawer.restart();
		
		while (noteCapturing) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("input end");

		
		getDrawer().getFrame().getContentPane().removeKeyListener(this);
		getDrawer().getFrame().getGlassPane().removeKeyListener(this);
		getDrawer().getFrame().removeKeyListener(this);
		getDrawer().removeKeyListener(this);
		
		outputSong();
			
	}

	private void outputSong() {
		
		String songOut = "";
		songOut += audioSrc + "," + difficulty + "," + tempo + "," + startDiff + "," + "\n";
		

		songOut += leftNotesStr + "\n" + downNotesStr + "\n" + upNotesStr + "\n" + rightNotesStr;

		
		
		try {
			PrintWriter writer = new PrintWriter(new File(songSrc));
			writer.print(songOut);
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("song logged");
	}


	public void addNotes() {
		double noteDistMultiplier = Note.noteSize;
		double noteSpeed = tempo * Note.noteSize / 60;
	
		//leftNotes
		Scanner leftScan = new Scanner(leftNotesStr);
		leftScan.useDelimiter(",");
		
		LeftNote newLftNote;
		while (leftScan.hasNext()) {
			try {
				newLftNote = new LeftNote(getDrawer(),noteStart + Double.parseDouble(leftScan.next())*noteDistMultiplier, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
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
				newDwnNote = new DownNote(getDrawer(),noteStart + Double.parseDouble(downScan.next())*noteDistMultiplier, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
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
				newUpNote = new UpNote(getDrawer(),noteStart + Double.parseDouble(upScan.next())*noteDistMultiplier, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
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
				newRgtNote = new RightNote(getDrawer(),noteStart + Double.parseDouble(rightScan.next())*noteDistMultiplier, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
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
	
	public void loadNotes() {
		for (Note note : allNotes) {
			note.run();
		}
	}
	public void play() {
		DDRRunner.currentSong = this;
		for (Note note : allNotes) {
			note.run();
		}
		getDrawer().getFrame().getContentPane().addKeyListener(this);
		getDrawer().getFrame().getGlassPane().addKeyListener(this);
		getDrawer().getFrame().addKeyListener(this);
		getDrawer().addKeyListener(this);
		
		
		AudioManager.playAudioFile(audioSrc);
		getDrawer().start();
	
	}
	
	


	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if (arg0.getKeyCode() == 37) { //LEFT
			if (! left) {
				left = true;
				
				if (noteCapturing) {
					logNote(NoteDirections.left,getDrawer().getCurrentFrame());
				}else {
					for (Note n : leftNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), leftNoteTarget.getCoordinates());
						if (distance < Note.noteSize/4) {
							getDrawer().remove(n);
							leftNotes.remove(n);
							DDRRunner.score.AddScore(1000/distance);
							break;
						}
					}
				}
			}
		}
		
		if (arg0.getKeyCode() == 40) { //DOWN
			if (! down) {
				down = true;
				
				if (noteCapturing) {
					logNote(NoteDirections.down,getDrawer().getCurrentFrame());
				}else {
					for (Note n : downNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), downNoteTarget.getCoordinates());
						if (distance < Note.noteSize/4) {
							getDrawer().remove(n);
							downNotes.remove(n);
							DDRRunner.score.AddScore(1000/distance);
							break;
						}
					}
				}
			}
		}
		
		if (arg0.getKeyCode() == 38) { //UP
			if (! up) {
				up = true;
				
				if (noteCapturing) {
					logNote(NoteDirections.up,getDrawer().getCurrentFrame());
				}else {
					for (Note n : upNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), upNoteTarget.getCoordinates());
						if (distance < Note.noteSize/4) {
							getDrawer().remove(n);
							upNotes.remove(n);
							DDRRunner.score.AddScore(1000/distance);
							break;
						}
					}
				}
			}
		}
		
		if (arg0.getKeyCode() == 39) { //RIGHT
			if (! right) {
				right = true;
				
				if (noteCapturing) {
					logNote(NoteDirections.right,getDrawer().getCurrentFrame());
				}else {
					for (Note n : rightNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), rightNoteTarget.getCoordinates());
						if (distance < Note.noteSize/4) {
							getDrawer().remove(n);
							rightNotes.remove(n);
							DDRRunner.score.AddScore(1000/distance);
							break;
						}
					}
				}
				
			}
		}
		
		if (arg0.getKeyCode() == 10) { //ENTER
			System.out.println("ENTER");
			noteCapturing = false;
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
