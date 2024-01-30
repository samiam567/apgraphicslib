package essay_writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;

public class EssayDatabase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7778865099879544985L;
	EssayArray words = new EssayArray();

	public void importEssay(String fileName) {
		//import the essay
		File file;
		Scanner scan;
		try {
			file = new File(fileName);
			scan = new Scanner(file);
		
		Word currentWord,previousWord;
		previousWord = getWord(scan.next());
		System.out.println(previousWord.getName());
		while(scan.hasNext()) {
			currentWord = getWord(scan.next());
			previousWord.addWordConnection(currentWord);
			System.out.println(currentWord.getName());
			previousWord = currentWord;
		}
		
		scan.close();
		System.out.println("File successfully scanned");		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File not found");
		} catch (Exception e) {
			System.out.println("There was an unknown error reading the file");
			e.printStackTrace();
		}
	}
	
	public void importEssay(Scanner scan) {
		//import the essay
		
		try {
			Word currentWord,previousWord;
			previousWord = getWord(scan.next());
			System.out.println(previousWord.getName());
			while(scan.hasNext()) {
				currentWord = getWord(scan.next());
				previousWord.addWordConnection(currentWord);
				System.out.println(currentWord.getName());
				previousWord = currentWord;
			}
			
			scan.close();
			System.out.println("File successfully scanned");		
		} catch (Exception e) {
			System.out.println("There was an unknown error reading the scanner");
			e.printStackTrace();
		}
	}
	
	

	public  Word getWord(String wordName) { //inefficient method of getting words (optimize later)
		Word w;
		for (int i = 0; i < words.size(); i++) { //search for the word
			w = (Word) words.get(i);
			if (w.getName().equals(wordName)) { //if we find it, return the word object...
				return w;
			}
		}
		
		Word wordToCreate = new Word(wordName, Essay_Generator.getDatabaseLoader()); //if not, create one
		words.add(wordToCreate);
		System.out.println("Added " + wordToCreate.getName() + " wordsSize: " + words.size());
		return wordToCreate;
	}
	
	public Word getWord(int wordIndex) {
		return (Word) words.get(wordIndex);
	}
	
}
