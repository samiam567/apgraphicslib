package danceDanceRevolution;


import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JOptionPane;

import apgraphicslib.AudioManager;
import apgraphicslib.Object_draw;
import apgraphicslib.Physics_2DDrawMovable;
import apgraphicslib.Physics_engine_toolbox;
import apgraphicslib.Settings;

public class Song extends Physics_2DDrawMovable implements KeyListener{
	
	private double audioLatency = 3910, noteStart;
	double noteSpeed;
	//if these arrows on the mat are pressed down 
	private boolean left = false, down = false, up = false, right = false;
	
	private boolean noteCapturing = false; //0 if not notecapturing, playbackspeed if capturing
	
	private double beats;
	
	public Queue<Double> lPosQueue = new LinkedList<Double>();
	public Queue<Double> dPosQueue = new LinkedList<Double>();
	public Queue<Double> uPosQueue = new LinkedList<Double>();
	public Queue<Double> rPosQueue = new LinkedList<Double>();
	
	
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
		super(drawer,0,0);
		loadSong(songSrc);
		
	}
	
	
		
	private void calculateNoteValues() {
		
		noteStart = (startDiff+4) * Note.noteSize + 100;//Note.noteSize/2 - 100 + Note.noteSize * 4 + audioLatency * Note.noteSize + startDiff * Note.noteSize;
		noteSpeed = getTempo() * Note.noteSize / 60;
		beats = 0;
	}
	
	private void loadSong(String songSrc) {
		
		this.songSrc = songSrc;
		Note.noteSize = getDrawer().getWidth()/5;
		leftNoteTarget = new LeftNote(this,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		
		downNoteTarget = new DownNote(this,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		upNoteTarget = new UpNote(this,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		rightNoteTarget = new RightNote(this,10 + Note.noteSize/2,0,"./src/danceDanceRevolution/assets/arrowTargetTextureGreen.png");
		
		
		allNotes.add(leftNoteTarget);
		allNotes.add(downNoteTarget);
		allNotes.add(upNoteTarget);
		allNotes.add(rightNoteTarget);
		 
		Scanner scan;
		try {
			scan = new Scanner(new File(songSrc));
			scan.useDelimiter(",");
			
			audioSrc = scan.next();
			
			try { //if this is an Aubio file the first token will be the tempo
				tempo = Double.parseDouble(audioSrc); 
				SongGenerator.generateSongInto(songSrc,this);
				calculateNoteValues();
				addNotes();
				loadNotes();
			
			}catch(NumberFormatException n) { //if the first token is not a number it is a user file
				difficulty = Double.parseDouble(scan.next());
				tempo = Double.parseDouble(scan.next());
				startDiff = Double.parseDouble(scan.next());
				scan.nextLine();
				leftNotesStr = scan.nextLine();
				downNotesStr = scan.nextLine();
				upNotesStr = scan.nextLine();
				rightNotesStr = scan.nextLine();
				
				calculateNoteValues();
				readNotes();
				addNotes();
			}
			
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

	private void logNote(NoteDirections direction) {
		
		double noteBeatPos = beats;
//		System.out.println("logging note (unrounded : " + noteBeatPos);
		
		noteBeatPos = ((double)Math.round(noteBeatPos*4))/4; //make 0.5 the smallest increment
		
	//	System.out.println("logging note: " + noteBeatPos);

		if (direction.equals(NoteDirections.left)) {				
			leftNotesStr += "" + noteBeatPos + ",";
		}else if (direction.equals(NoteDirections.down)) {
			downNotesStr += "" + noteBeatPos + ",";
		}else if (direction.equals(NoteDirections.up)) {
			upNotesStr += "" + noteBeatPos + ",";
		}else if (direction.equals(NoteDirections.right)) {
			rightNotesStr += "" + noteBeatPos + ",";
		}
						
//		System.out.println("note logged");		
	}

	public Song(Object_draw drawer) {
		super(drawer,0,0);
		
		double playBackSpeed = 0.5;
		
		
		songSrc = "./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer,"What is the song name?") + ".dat";
		
		
		try { //if the song already exists we will try to load it in
			loadSong(songSrc);
			calculateNoteValues();
			loadNotes(playBackSpeed);
		}catch(Exception e) {
			audioSrc = "./src/danceDanceRevolution/assets/" + JOptionPane.showInputDialog(drawer,"What is the audio file?") + ".wav";	
			tempo = Physics_engine_toolbox.getDoubleFromUser(getDrawer().getFrame(), "What is the tempo? (in bpm) look it up!");
		}
		
		calculateNoteValues();
		
		JOptionPane.showMessageDialog(getDrawer(), "Now capturing. Press ENTER to save capture");
		
		
		
		noteCapturing = true;
		
		play(playBackSpeed);
		
		System.out.println("input end");

		
		
		outputSong();
			
	}

	private void outputSong() {
		
		String songOut = "";
		songOut += audioSrc + "," + difficulty + "," + getTempo() + "," + startDiff + "," + "\n";
		

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


	public void readNotes() {
		double noteDistMultiplier = Note.noteSize;
	
	
		//leftNotes
		Scanner leftScan = new Scanner(leftNotesStr);
		leftScan.useDelimiter(",");
		
		
		while (leftScan.hasNext()) {
			try {
				lPosQueue.add(noteStart + Double.parseDouble(leftScan.next())*noteDistMultiplier);
			}catch(NumberFormatException n ) {}
		}
		
		
		//downNotes
		Scanner downScan = new Scanner(downNotesStr);
		downScan.useDelimiter(",");
		
		
		while (downScan.hasNext()) {
			try {
				dPosQueue.add(noteStart + Double.parseDouble(downScan.next())*noteDistMultiplier);
			}catch(NumberFormatException n ) {}
		}
		
		
		//upNotes
		Scanner upScan = new Scanner(upNotesStr);
		upScan.useDelimiter(",");
		
		
		while (upScan.hasNext()) {
			try {
				uPosQueue.add(noteStart + Double.parseDouble(upScan.next())*noteDistMultiplier);
			}catch(NumberFormatException n ) {}
		}
		
		
		//rightNotes
		Scanner rightScan = new Scanner(rightNotesStr);
		rightScan.useDelimiter(",");
		
		
		while (rightScan.hasNext()) {
			try {
				rPosQueue.add(noteStart + Double.parseDouble(rightScan.next())*noteDistMultiplier);
			}catch(NumberFormatException n ) {}
		}
		
		leftScan.close();
		downScan.close();
		upScan.close();
		rightScan.close();
	}
	
	/**
	 * {@code since we will have tons of notes, override the rotation calculations}
	 */
	@Override
	public void Update(double frames) {		
		getCoordinates().add(speed.tempStatMultiply(frames));
		
	}
	
	public void addNotes() {
		LeftNote newLftNote;
		DownNote newDwnNote;
		UpNote newUpNote;
		RightNote newRgtNote;
		
		int notesToAdd = 3;
		for (int i = 0; i < notesToAdd; i++) {
			newLftNote = new LeftNote(this,lPosQueue.remove(), noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
			leftNotes.add(newLftNote);
			allNotes.add(newLftNote);
			System.out.println("note added");
		}
		
		for (int i = 0; i < notesToAdd; i++) {
			newDwnNote = new DownNote(this,dPosQueue.remove(), noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
			downNotes.add(newDwnNote);
			allNotes.add(newDwnNote);
			System.out.println("note added");
		}
		
		for (int i = 0; i < notesToAdd; i++) {
			newUpNote = new UpNote(this,uPosQueue.remove(), noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
			upNotes.add(newUpNote);
			allNotes.add(newUpNote);
			System.out.println("note added");
		}
		
		for (int i = 0; i < notesToAdd; i++) {
			newRgtNote = new RightNote(this,rPosQueue.remove(), noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
			rightNotes.add(newRgtNote);
			allNotes.add(newRgtNote);
			System.out.println("note added");
		}
		
	}
	
	public void loadNotes() {
		for (Note note : allNotes) {
			note.run();
		}
		
		//load song speed to record where we are in the song
		getDrawer().add(this);
		getSpeed().setJ(-noteSpeed);
	}
	
	public void loadNotes(double playBackSpeed) {
		getSpeed().multiply(playBackSpeed);
		for (Note note : allNotes) {
			note.run();
			note.getSpeed().multiply(playBackSpeed);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void play(double playBackSpeed) {
		
		DDRRunner.currentSong = this;
	
		getDrawer().getFrame().getContentPane().addKeyListener(this);
		getDrawer().getFrame().getGlassPane().addKeyListener(this);
		getDrawer().getFrame().addKeyListener(this);
		getDrawer().addKeyListener(this);
		
		
		AudioManager.playAudioFile(audioSrc, playBackSpeed);
		
		getDrawer().repaint();
		/*
		try {
			Thread.sleep((long) (audioLatency));
		}catch(InterruptedException e) {}
		*/
		
		getDrawer().start();
		getDrawer().resetFrameCounter();
		
		try {
			Thread.sleep((long) (60.0 * (11 + startDiff) / (getTempo()*playBackSpeed) ) * 1000 );
		}catch(InterruptedException e) {}
		
		

		while (AudioManager.isPlaying()) {	
			try {
				Thread.sleep((long) (15000*2/getTempo()/playBackSpeed));
			}catch(InterruptedException c) {}
			beats+=0.25;
		}
		
		
		
		
		getDrawer().getFrame().getContentPane().removeKeyListener(this);
		getDrawer().getFrame().getGlassPane().removeKeyListener(this);
		getDrawer().getFrame().removeKeyListener(this);
		getDrawer().removeKeyListener(this);
		
		getDrawer().stopNoWait();
		
		getDrawer().getFrame().setVisible(false);
		
	
	}
	
	


	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println(arg0.getKeyChar());

		if (arg0.getKeyCode() == 37) { //LEFT
			
			
			if (! left) {
				left = true;
				
				if (noteCapturing) {
					logNote(NoteDirections.left);
				}else {
					for (Note n : leftNotes) {
						
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), leftNoteTarget.getCoordinates());
						if (distance < Note.noteSize/2) {
							n.reposition();
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
					logNote(NoteDirections.down);
				}else {
					for (Note n : downNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), downNoteTarget.getCoordinates());
						if (distance < Note.noteSize/2) {
							n.reposition();
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
					logNote(NoteDirections.up);
				}else {
					for (Note n : upNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), upNoteTarget.getCoordinates());
						if (distance < Note.noteSize/2) {
							n.reposition();
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
					logNote(NoteDirections.right);
				}else {
					for (Note n : rightNotes) {
						double distance = Physics_engine_toolbox.distance2D(n.getCoordinates(), rightNoteTarget.getCoordinates());
						if (distance < Note.noteSize/2) {
							n.reposition();
							DDRRunner.score.AddScore(1000/distance);
							break;
						}
					}
				}
				
			}
		}
		
		if (arg0.getKeyCode() == 10) { //ENTER
			System.out.println("ENTER");
			AudioManager.stop();
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
	public void keyTyped(KeyEvent arg0) {}
	
	@Override
	public String toString() {
		return "audioSrc: " + audioSrc + "\nDifficulty: " + difficulty + "\nTempo: " + getTempo() + "\nleftNotes: " + leftNotesStr + "\ndownNotes: " + downNotesStr + "\nupNotes: " + upNotesStr +  "\nrightNotes: " + rightNotesStr;
	}
	
	public LinkedList<LeftNote> getLeftNotes() {
		return leftNotes;
	}
	
	public LinkedList<DownNote> getDownNotes() {
		return downNotes;
	}
	
	public LinkedList<UpNote> getUpNotes() {
		return upNotes;
	}
	
	public LinkedList<RightNote> getRightNotes() {
		return rightNotes;
	}

	public void setAudioSrc(String newAudioSrc) {
		audioSrc = newAudioSrc;
	}



	public double getTempo() {
		return tempo;
	}



	public LinkedList<Note> getAllNotes() {
		return allNotes;
	}



	@Override
	public void paint(Graphics page) {
		//do nothing
	}



	public double getAudioLatency() {
		return audioLatency;
	}
}
