package essay_writer;

import java.io.Serializable;

public class Word implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5139378856859499487L;
	private String name;
	private EssayArray wordConnections = new EssayArray();
	private Database_loader databaseLoader;
	private int wordInstanceCount = 0;
	
	public Word(String name1,Database_loader dbl) {
		name = name1;
		databaseLoader = dbl;
	}
	public String getName() {
		return name;
	}
	
	public Word getNextWord() {
		System.out.println("getting next word...");
		int currentWordConnectionIndx = 0;
		int instanceOfNextWord = (int) (Math.random() * wordInstanceCount);
		int instanceCounter = 0;
		while (instanceCounter < instanceOfNextWord) {
			instanceCounter += ((wordConnection) wordConnections.get(currentWordConnectionIndx)).getInstances();
			currentWordConnectionIndx++;
		}
		System.out.println("wordName: " + name + "counter: " + instanceCounter + " wordInstanceCount: " + wordInstanceCount);
		try {
			return ((wordConnection) wordConnections.get(currentWordConnectionIndx)).getWord();
		}catch(IndexOutOfBoundsException i) {
			System.out.println("That word does not connect to anything");
			return new Word("terminationWord934582934820",databaseLoader);
		}catch(NullPointerException n) {
			System.out.println("That word does not connect to anything");
			return new Word("terminationWord934582934820",databaseLoader);
		}
	}
	
	public void addWordConnection(Word newWord) {
		System.out.println("Connecting " + getName() + " to " + newWord.getName());
		//search for the connection & add connection
		wordConnection w;
		for (int i = 0; i < wordConnections.size(); i++) {
			w = (wordConnection) wordConnections.get(i);
			if (w.getWordName().equals(newWord.getName())) {
				w.addInstance();
				wordInstanceCount++;
				return;
			}
		}
		
		wordConnections.add(new wordConnection(newWord));
		wordInstanceCount++;
		System.out.println("creatingNewconnection");
	}
	
	public void addWordConnection(String newWordName) {
		//search for the connection & add connection
		wordConnection w;
		for (int i = 0; i < wordConnections.size(); i++) {
			w = (wordConnection) wordConnections.get(i);
			if (w.getWordName().equals(newWordName)) {
				w.addInstance();
				return;
			}
		}
		wordConnections.add(new wordConnection(databaseLoader.getDatabase().getWord(newWordName)));
	}
	
	public String toString() {
		String output = "";
		
		for (int i = 0; i < wordConnections.size(); i++) {

			for (int z = 0; z < ( (wordConnection)wordConnections.get(i) ).getInstances(); z++ ) {
				output += (name);
				output += " ";
				output += ((wordConnection)wordConnections.get(i)).getInstances();
				output += (" terminationWord934582934820 ");
				
			}
		}
		
		return output;
		
	}

}
