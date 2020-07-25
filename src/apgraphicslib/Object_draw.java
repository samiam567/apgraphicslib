package apgraphicslib;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

/**
 * {@code This class is what puts all of the elements of the engine together. It has lists of all the objects in the engine and will call their Update() and paint() methods when needed and holds an instance of Physics_frame to paint the objects onto. It also controls the multithreading in the engine through its instance of the Object_draw_update_thread object and monitors the frame rate and frameStep (how many times the objects are updated each frame) to keep the in-game time constant with real-world time while compensating for variable processing times. }
 * @author samiam567
 *
 */
public class Object_draw extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2576075113211532691L;
	
	private String name = "Unnamed Object_draw";
	
	public PrintStream out = System.out; //this (should) control the console output of the whole engine
	
	// a list of all the objects in the engine
	private LinkedList<Physics_engine_compatible> objects = new LinkedList<Physics_engine_compatible>();
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>(); //we have to sort this, so it is an ArrayList
	private LinkedList<Updatable> updatables = new LinkedList<Updatable>();
	private LinkedList<Resizable> resizables = new LinkedList<Resizable>();
	private ArrayList<Tangible> tangibles = new ArrayList<Tangible>(); //this one is an arrayList because we have to do fancy manipulation in checkForCollisions()

	private Physics_frame frame;

	private Object_draw_update_thread threader;

	private ArrayList<Camera> cameras = new ArrayList<Camera>();
	
	public int inactivity_timer = 0;
	
	private double updateCount = 0, frameStep = 0.1, actualFPS = Settings.targetFPS;
	private long current_frame = 0, updateStartTime, updateEndTime, frameStartTime, frameEndTime, subCalcTime, lastPaintTime;
	private double frameTime = 1/Settings.targetFPS;
	
	private boolean paintWithCameras = true; //whether we should still paint ourself even if we have cameras
	
	public Object_draw() {
		frame = new Physics_frame(this);
		
		threader = new Object_draw_update_thread(this);
	}
	
	/**
	 * {@code will create an Object_draw that does not have it's own frame. Indicates that you intend to use the passed camera as the main view for the game (the camera controls the painting) Object_draw.frame will equal null}
	 * @param mainCam
	 */
	public Object_draw(Camera mainCam) {
		paintWithCameras = false;
		frame = mainCam.frame;
		threader = new Object_draw_update_thread(this);
		mainCam.setDrawer(this);
		cameras.add(mainCam);
	}
	
	/**
	 * {@summary adds the camera to this drawer. If the cam was not passed as the constructor parameter for this Object_draw this must be called for the camera to work properly}
	 * {@code adds the camera to this drawer's cameras ArrayList, and sets the camera's drawer to this drawer}
	 * @param cam
	 */
	public void addCamera(Camera cam) {
		cameras.add(cam);
		cam.setDrawer(this);
	}
	
	
	public void callRepaint() {
		if (cameras.size() == 0) {
			repaint();
		}else if (paintWithCameras) {
			repaint();
			for (Camera cam : cameras) {
				cam.repaint();
			}
		}else { //if we have cameras let them do the painting
			for (Camera cam : cameras) {
				cam.repaint();
			}
		}
	}
	
	
	public void doUpdate() { //for update thread. Updates the objects
		
		try {			
			frameStartTime = System.nanoTime();	
			prePaintUpdateObjects();
			checkForResize();
			callRepaint(); //paint the objects onto the screen.
			actualFPS = 1000000000/((double)(System.nanoTime() - lastPaintTime));
			lastPaintTime = System.nanoTime();
			
			
			if (frameStep > 1) {
				frameStep = 1;
				frameTime *= frameStep;
			}else if (frameStep != 1){
				frameTime *= Math.sqrt(Settings.targetFPS/frameTime);
			}
			
			frameEndTime = System.nanoTime();	
			
			subCalcTime = 0;
			
			if ( (! Double.isFinite(frameStep)) || (frameStep <= 0) ) frameStep = 1;
			
			for (double frameCount = 0; frameCount < 1/frameStep; frameCount ++) {
				updateStartTime = System.nanoTime();
				updateObjects(Settings.timeSpeed * ((1 / ( (int) 1/frameStep )) / getActualFPS()) );
				checkForCollisions(); //check for collisions between the tangibles
				frameCount += frameStep;
				updateCount++;
				
				updateEndTime = System.nanoTime();	
				subCalcTime += ((long)( (updateEndTime - updateStartTime)));
			}
			
			subCalcTime *= frameStep;
			
			
			/*
			underTime = (long) ((1000000000/Settings.targetFPS) - (subCalcTime+(frameEndTime-frameStartTime)));	
			if (underTime > 0) { try {Thread.sleep(underTime/1000000);}catch(InterruptedException i) {} }
			*/
			
			frameStep = ((double) subCalcTime) / ((1000000000/Settings.targetFPS) - (frameEndTime-frameStartTime));
		
			current_frame++;
	    }catch(ConcurrentModificationException c) {
	    	c.printStackTrace(out);
		}catch(NullPointerException e) {
			out.println("nullPointer in object_draw.java");
			e.printStackTrace(out);
		}catch(NoSuchElementException n) {//if the element was deleted while this process was being run	(hopefully)	
			n.printStackTrace(out);
		} 	
		
	}

	/**
	 * {@summary starts the updater thread. This must be called or the engine won't do anything. It should ideally be called after all objects are added}
	 */
	public void start() {
		threader.state = 1;
		try {
			threader.start();
			current_frame = 0; //reset the current_frame counter
		}catch(IllegalThreadStateException i) {
			System.out.println(i + " in Object_draw.start()");
			resume();
		}
		lastPaintTime = System.nanoTime();
	}
	
	/**
	 * {@summary restarts the updater as if it had just been created}
	 */
	public void restart() {
		threader.state = 0;
		threader = new Object_draw_update_thread(this);
		start();
	}
	
	/**
	 * {@summary pauses the updater without waiting it to finish}
	 */
	public void interruptPause() {
		threader.state = 2;
		threader.interrupt();
		pause();
	}
	
	/**
	 * {@summary pauses the updater without waiting for it to actually pause}
	 */
	public void pauseNoWait() {
		threader.state = 2;
	}
	
	/**
	 * {@summary waits for the updater to finish its current cycle and pauses it}
	 */
	public void pause() {
		threader.state = 2;
		
		//wait for threader to be done with it's current cycle and to pause
		while( (threader.status != 2) && (threader.status != 0) && (Thread.currentThread() != threader) ) {
			try {
				Thread.sleep(1);
				threader.state = 2;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * {@summary resumes the updater without waiting for it to actually resume}
	 */
	public void resumeNoWait() {	
		threader.state = 1;
	}
	
	/**
	 * {@summary resumes the updater and waits for it to actually resume}
	 */
	public void resume() {	
		threader.state = 1;

		//wait for threader to be done with it's current cycle and to pause
		while((threader.status != 1)  && (threader.status != 0) && (Thread.currentThread() != threader)) {
			try {
				Thread.sleep(1);
				threader.state = 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * {@summary stops the update thread without waiting for it to actually stop}
	 */
	public void stopNoWait() {
		threader.state = 0;	
	}
	
	/**
	 * {@summary stops the update thread and waits for it to actually stop=}
	 */
	public void stop() {
		threader.state = 0;
		threader.interrupt();
		while((threader.status != 0) && (Thread.currentThread() != threader) ) {
			try {
				Thread.sleep(1);
				threader.state = 0;
			}catch(InterruptedException e) {}
		}
		System.out.println("thread stopped");
		
	}
	
	public void addDrawOnly(Drawable addOb) {	
		objects.add(addOb);
		getDrawables().add(addOb);
		out.println("Added " +  addOb.getName() + " to " + getName() + " (drawOnly)");		
	}
	
	public void add(Physics_engine_compatible addOb) {
		pause();
		objects.add(addOb);
		
		if (Drawable.class.isAssignableFrom(addOb.getClass())) {
			drawables.add((Drawable) addOb);
		}
		
		if (Tangible.class.isAssignableFrom(addOb.getClass())) {
			tangibles.add((Tangible) addOb);
		}
		
		if (Resizable.class.isAssignableFrom(addOb.getClass())) {
			resizables.add((Resizable) addOb);
		}
		
		if (Updatable.class.isAssignableFrom(addOb.getClass())) {
			updatables.add((Updatable) addOb);
		}
		
		out.println("Added " + ((Physics_engine_compatible) addOb).getName() + " to " + getName());
		
		frameStep = 1; //this keeps the updater from freezing due to the possible sudden change in processing power required
		resume();
	}
	
	public void remove(Physics_engine_compatible removeOb) {
		objects.remove(removeOb);
		
		try {
			getDrawables().remove((Drawable) removeOb);
		}catch(ClassCastException c) {
			//ob is not drawable
		}
		
		try {
			tangibles.remove((Tangible)removeOb);
		}catch(ClassCastException e) {
			//ob is not tangible
		}
		
		try {
			resizables.remove((Resizable) removeOb);
		}catch(ClassCastException e) {
			//ob is not resizable
		}
		
		try {
			updatables.remove((Updatable) removeOb);
		}catch(ClassCastException e) {
			//ob is not updatable
		}

		
		out.println("Removed " + ((Physics_engine_compatible) removeOb).getName() + " from " + getName());
		
	}
	
	public void clearObjects() {
		objects.clear();
		updatables.clear();
		getDrawables().clear();
		tangibles.clear();
		resizables.clear();
	}
	
	private void updateObjects(double frames) {
		try {		
			for (Updatable current_object : updatables) {
				current_object.Update(frames);
			}
		}catch(ConcurrentModificationException c) {
			System.out.println(c.toString());
			c.printStackTrace(out);
		}
	}
	
	private void prePaintUpdateObjects() {
		try {		
			for (Updatable current_object : updatables) {
				current_object.prePaintUpdate();
			}
		}catch(ConcurrentModificationException c) {
			System.out.println(c.toString());
			c.printStackTrace(out);
		}
	}

	private void checkForCollisions() {		
		Coordinate2D pointOfCollision;
		Tangible o1, o2;
		for (int i = 0; i < tangibles.size(); i++) {
			for (int a = i+1; a < tangibles.size(); a++) {
				
				//switch which way we go through through the list each update (first a -> b then b -> a)	
				if (updateCount % 2 == 0) { 
					o1 = tangibles.get(i);
					o2 = tangibles.get(a);
				}else {
					o1 = tangibles.get(a);
					o2 = tangibles.get(i);
				}
				
				if (o1.getIsTangible() && o2.getIsTangible()) {
					pointOfCollision = o1.checkForCollision(o2);
					
					if (! (pointOfCollision == null)) { // we have a collision
						o2.collision(o1.getCollisionEvent(o2,pointOfCollision));
						o1.collision(o2.getCollisionEvent(o1,pointOfCollision));
					}
				}
			}
		}
	}
	
	public void checkForResize() {	
		
		if ( (Settings.width != getFrame().getWidth()) || (Settings.height != getFrame().getHeight())) {
			if (Settings.autoResizeFrame) {
				Settings.width = (getFrame().getWidth());
				Settings.height = (getFrame().getHeight());
				for (Resizable cObject : resizables) cObject.resize(); //resize resizable objects				
			}
		}
	}
	
	
	public PrintStream getOutputStream() {
		return out;
	}

	public void setOutputStream(PrintStream outputStream) {
		this.out = outputStream;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public LinkedList<Physics_engine_compatible> getObjects() {
		return objects;
	}

	public LinkedList<Updatable> getUpdatables() {
		return updatables;
	}
	
	public ArrayList<Tangible> getTangibles() {
		return tangibles;
	}
	
	public LinkedList<Resizable> getResizables() {
		return resizables;
	}

	public void paint(Graphics page) {
		
		try {
			
			drawables.sort(new Comparator<Drawable>() {
				@Override
				public int compare(Drawable o1, Drawable o2) {
					return Double.compare(o2.getPaintOrderValue(),o1.getPaintOrderValue());	
				}
					
			});
			
			page.setColor(getFrame().getBackground());
			page.fillRect(0, 0, getFrame().getWidth(), getFrame().getHeight());
			page.setColor(Color.black);
			
			for (Drawable cOb : getDrawables()) {		
				page.setColor(cOb.getColor());
				if (cOb.getIsVisible()) cOb.paint(page);
			}
				
			
		
		}catch(ConcurrentModificationException c) {
			out.println(c);
			c.printStackTrace(out);
		}
	}

	public double getFrameStep() {
		return frameStep;
	}

	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}

	public Physics_frame getFrame() {
		return frame;
	}
	
	/**
	 * 
	 * @return the actual rate at which frames are being served, rather than the target FPS
	 */
	public double getActualFPS() {
		return actualFPS;
	}
	
	/**
	 * {@summary whether or not we should still paint ourself even if we have cameras}
	 * @param pWC
	 */
	public void setPaintWithCameras(boolean pWC) {
		paintWithCameras = pWC;
	}

	public double getCurrentFrame() {
		return current_frame;
	}
	
	public void resetFrameCounter() {
		current_frame = 0;
	}
	
	public int getFrameWidth() {
		try {
			return frame.getWidth();
		}catch(NullPointerException n) {
			return Settings.width;
		}
	}
	
	public int getFrameHeight() {
		try {
			return frame.getHeight();
		}catch(NullPointerException n) {
			return Settings.height;
		}
	}
	
	public double getFrameCenterX() {
		return ((double) getFrameWidth())/2;
	}
	
	public double getFrameCenterY() {
		return ((double) getFrameHeight())/2;
	}

}
