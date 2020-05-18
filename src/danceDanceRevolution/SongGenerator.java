package danceDanceRevolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class SongGenerator {
	
	private static int notesToSkip = 0;
	private static int notesToRead = 150;
	
	
	//this is the difficulty
	private static int notesPerBeat = 2; 
	private static double timeInBetweenNotes = 0.25;
	
	private static double[][] notePitches;

	public static void generateSongInto(String audioSrc, Song target) {
		
		LinkedList<Note> allNotes = target.getAllNotes();
		LinkedList<LeftNote> leftNotes = target.getLeftNotes();
		LinkedList<DownNote> downNotes = target.getDownNotes();
		LinkedList<UpNote> upNotes = target.getUpNotes();
		LinkedList<RightNote> rightNotes = target.getRightNotes();
		
	
		
		
		
		double noteSpeed = target.getTempo() * Note.noteSize / 30 , noteTimeStamp, notePitch;
		double startPos = 10 + Note.noteSize/2;// make the zero point at the pos of the noteTargets
		
		int noteDirection;
		
		
		ArrayList<Double> noteTimes = new ArrayList<Double>();
		notePitches = new double[10000][2];
		
		try {
	
			Scanner scan = new Scanner(new File(audioSrc));
			
			scan.nextLine();
			target.setAudioSrc(scan.nextLine());
			
			String scanInput = scan.next();
			
			//input note times
			while (! scanInput.equals(",") ) {
				System.out.println(scanInput);
				noteTimes.add(Double.parseDouble(scanInput));
				scanInput = scan.next();
			
				
				//skip notes
				for (int a = 0; a < notesToSkip; a++) {
					scan.next();
				}
				
			}
			
			//input pitches
			int notePitchIndx = 0;
			while(scan.hasNext()) {
				try {
					if (notePitchIndx >= notePitches.length) notePitches = resizeNotePitches(notePitches, 2*notePitchIndx); //add some extra space to notepitches
					notePitches[notePitchIndx][0] = Double.parseDouble(scan.next());
					notePitches[notePitchIndx][1] = Double.parseDouble(scan.next());
					notePitchIndx++;
				}catch(NumberFormatException n) {System.out.println("bad note");}
			}
			notePitches = resizeNotePitches(notePitches, notePitchIndx); //cut off the extra from notePitches
	
			int  notesToReadIn = notesToRead;
			
			int notesThisBeat = 0;
			double beat = 0, timePerSecond; //notes per second method
			
			
			double prevTimeStamp = 0, timePerBeat = 120/target.getTempo();
			
			timeInBetweenNotes *= 60/target.getTempo();			
			
			for (int i = 0; ((i < noteTimes.size()) && (i < notesToReadIn)); i++ ) { 
				
								
				noteTimeStamp = noteTimes.get(i)/2;
				notePitch = getPitch(noteTimeStamp, 0, notePitches.length);
				
				
				
				
				
				//make sure there is more than timeInBetweenNotes in between each notes
				if (noteTimeStamp - prevTimeStamp < timeInBetweenNotes/2) {
					notesToReadIn++; //we didn't read this note so it doesn't count in our counter
					continue;
				}else {
					prevTimeStamp = noteTimeStamp;
				}
				
				
				//make sure we don't get too many notes this beat
				if ( ((int) ((noteTimeStamp)/timePerBeat)) == beat) {
					if (notesThisBeat > notesPerBeat) { //we have too many notes in this second
						notesToReadIn++; //we didn't read this note so it doesn't count in our counter
						continue;
					}else {
						notesThisBeat++;
					}
				}else {
					beat = ((int) ((noteTimeStamp)/timePerBeat));
					notesThisBeat = 0;
				}
				
			
				
				
				
				
				System.out.print(noteTimeStamp);
				System.out.print("," + notePitch + " - ");
				
				//0 - left
				//1 - down
				//2 - up
				//3 - right
				//4 - left/right
				//5 - up/down
				//6 - left/down
				//7 - right/up
				//8 - left/up
				//9 - right/down
				if (notePitch == 0) {
					noteDirection = 4;			
				}else {
					noteDirection = (int) (Math.round(notePitch) % 4);	
				}
				System.out.println(noteDirection);
				
				
				if ( (noteDirection == 0) || (noteDirection == 4) || (noteDirection == 6) || (noteDirection == 8) ) {
					//left
					LeftNote newLftNote = new LeftNote(target.getDrawer(),startPos + noteTimeStamp*noteSpeed, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
					leftNotes.add(newLftNote);
					allNotes.add(newLftNote);
				}
				if ( (noteDirection == 1) || (noteDirection == 5) || (noteDirection == 6) || (noteDirection == 9) ) {
					//down
					DownNote newDwnNote = new DownNote(target.getDrawer(),startPos + noteTimeStamp*noteSpeed, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
					downNotes.add(newDwnNote);
					allNotes.add(newDwnNote);
				}
				if ( (noteDirection == 2) || (noteDirection == 5) || (noteDirection == 7) || (noteDirection == 8) ) {
					//up
					UpNote newUpNote = new UpNote(target.getDrawer(),startPos + noteTimeStamp*noteSpeed, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
					upNotes.add(newUpNote);
					allNotes.add(newUpNote);
				}
				if ( (noteDirection == 3) || (noteDirection == 4) || (noteDirection == 7) || (noteDirection == 9) ) {
					//right
					RightNote newRgtNote = new RightNote(target.getDrawer(),startPos + noteTimeStamp*noteSpeed, noteSpeed, "./src/danceDanceRevolution/assets/arrowTexturePurple.png");
					rightNotes.add(newRgtNote);
					allNotes.add(newRgtNote);
				}
				
			}
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * {@code binary search for the pitch at a timeStamp}
	 * @param noteTimeStamp
	 * @param i
	 * @param length
	 * @return
	 */
	private static double getPitch(double noteTimeStamp, int lower, int upper) {
		
		if ( (noteTimeStamp == notePitches[(lower+upper)/2][0])  || (upper - lower) == 1 ) { //we are at position
			return notePitches[(lower+upper)/2][1]; //return the pitch at our position
		}else if (noteTimeStamp > notePitches[(lower+upper)/2][0]) { //we are too low
			return getPitch(noteTimeStamp, lower+(upper-lower)/2,upper); //search higher
		}else { //we are too high
			return getPitch(noteTimeStamp, lower, upper-(upper-lower)/2); //search lower
		}
	}


	private static double[][] resizeNotePitches(double[][] notePitches, int size) {
		double[][] prevPitches = notePitches;
		notePitches = new double[size][2];
				
		double refillSize;
		if (size > prevPitches.length) {
			refillSize = prevPitches.length;
		}else {
			refillSize = size;
		}
		
		for (int i = 0; i < refillSize; i++) {
			notePitches[i][0] = prevPitches[i][0];
			notePitches[i][1] = prevPitches[i][1];
		}
		
		return notePitches;
	}
	

}
