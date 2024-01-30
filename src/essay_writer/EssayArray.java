package essay_writer;

import java.io.Serializable;

public class EssayArray implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3788384047745888658L;
	private Object[] array;
	private int nextIndx = 0;
	
	public EssayArray() {
		array = new Object[1];
	}
	
	public Object get(int indx) {
		return array[indx];
	}
	
	public void add(Object newOb) {
		if (nextIndx < array.length) {
			array[nextIndx] = newOb;
		}else {
			resize(nextIndx+5);
			array[nextIndx] = newOb;
		}
		nextIndx++;
	}
	
	private void resize(int newSize) {
//		System.out.println("too many words. resizing array...");
		Object[] newArray = new Object[newSize];
		for (int i = 0; i < array.length; i++) {
			newArray[i] = array[i];
		}
		array = newArray;
		System.out.println("resize complete");
	}

	public int size() {
		return nextIndx;
	}
	
}

