package essay_writer;

import java.io.Serializable;

public class wordConnection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4573882098947397034L;
	private Word connectedWord;
	private int instances = 1;
	
	public wordConnection(Word connectedWord1) {
		connectedWord = connectedWord1;
	}
	
	public void addInstance() {
		instances++;
	}
	
	public int getInstances() {
		return instances;
	}
	
	public String getWordName() {
		return connectedWord.getName();
	}
	
	public Word getWord() {
		return connectedWord;
	}
}
