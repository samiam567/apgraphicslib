package essay_writer;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Database_loader extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3733553341926872296L;

	int mode = 0; //0 = idle, 1 = load, 2 = save
	
	public boolean databaseInUse = false;
	public boolean databaseLoaded = false;
	
	private EssayDatabase database;
	private String fileName = "";
	
	public Database_loader(String nameOfFile) {
		fileName = nameOfFile;
		mode = 1;
		start();
	}
	
	public void clearDatabase() {
		if (databaseLoaded) {
			database = new EssayDatabase();
		}else {
			while (databaseInUse) {
				try {Thread.sleep(100);}catch(InterruptedException i) {}
			}
			database = new EssayDatabase();
		}
	}
	
	public EssayDatabase getDatabase() {
		while (databaseInUse) {
			try {Thread.sleep(100);}catch(InterruptedException i) {}
		}
		if (databaseLoaded) {
			return database;
		}else {
			mode = 1;
			System.out.println("waiting for database to load...");
			
			try {Thread.sleep(100);}catch(InterruptedException i) {}
			
			while(databaseInUse) {		
				try {Thread.sleep(100);}catch(InterruptedException i) {}
			}
			return database;
		}
		
	}
	
	public void closeDatabase() {
		while (databaseInUse) {
			try {Thread.sleep(100);}catch(InterruptedException i) {}
		}
		
		mode = 2;
		saveDatabase();
	}
	
	private void loadDatabase() {
		try {
			ObjectInputStream loader = new ObjectInputStream(new FileInputStream(fileName));
			database = (EssayDatabase) loader.readObject();
			loader.close();
			databaseLoaded = true;
			System.out.println("Database loading complete.");
			databaseInUse = false;
			return;
		}catch(InvalidClassException e) {
			System.out.println(fileName + " is missing or corrupted"); 
			e.printStackTrace();
		}catch(EOFException e) {
			System.out.println(fileName + " is missing or corrupted");
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			System.out.println(fileName + " is missing or corrupted");
			e.printStackTrace();
		}catch(IOException e) {
			System.out.println(fileName + " is missing or corrupted");
			e.printStackTrace();
		}
		//if the computer gets past the return statement in the try block, an exception must've been thrown
		System.out.println("Database loading failed. Creating a blank database");
		database = new EssayDatabase();
		databaseLoaded = true;
	}
	
	private void saveDatabase() {
		try {
			databaseInUse = true;
			System.out.println("Saving in progress...");
			ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream(fileName));
			saver.writeObject(database);
			saver.close();
			System.out.println("Save Complete");
			databaseInUse = false;
			return;
		}catch(InvalidClassException e) {
			System.out.println(fileName + " is missing or corrupted"); 
			e.printStackTrace();
		}catch(EOFException e) {
			System.out.println(fileName + " is missing or corrupted");
			e.printStackTrace();
		}catch(IOException e) {
			System.out.println(fileName + " is missing or corrupted");
			e.printStackTrace();
		}
		//if the computer gets past the return statement in the try block, an exception must've been thrown
		System.out.println("Database saving failed.");
		databaseInUse = false;
	}
	
	
	public void run() {
		databaseInUse = true;
		if (mode == 1) {
			loadDatabase();
		}else if (mode == 2) {
			saveDatabase();
		}
		databaseInUse = false;
	}

}
