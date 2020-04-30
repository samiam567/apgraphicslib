package danceDanceRevolution;

import apgraphicslib.Object_draw;
import apgraphicslib.Physics_2DTexturedPolygon;
import apgraphicslib.Resizable;
import apgraphicslib.Settings;

public class Note extends Physics_2DTexturedPolygon implements Resizable {
	private static double NotePPSize = 2;
	public static double noteSize = Settings.width/5;
	private String textureSrc = "noTexture";
	
	private double noteSpeed;
	protected boolean loaded = false;
	public Note(Object_draw drawer, double x, double y, double noteSpeed, String textureSrc) {
		super(drawer, x, y, NotePPSize);
		this.textureSrc = textureSrc;
		this.noteSpeed = noteSpeed;
		addPoint(-noteSize/2, -noteSize/2);
		addPoint(-noteSize/2, noteSize/2);
		addPoint(noteSize/2, noteSize/2);
		addPoint(noteSize/2, -noteSize/2);
	}

	public void load() {
		setTexture(textureSrc);
		loaded = true;
	}
	
	protected void add() {
		getDrawer().add(this);
	}
	
	public void run() {
		if (! loaded) load();
		add();
		getSpeed().setJ(-noteSpeed);
	}
	
	
	/**
	 * {@code since we will have tons of notes, override the rotation calculations}
	 */
	@Override
	public void Update(double frames) {		
		getCoordinates().add(speed.tempStatMultiply(frames));
		speed.add(acceleration.tempStatMultiply(frames));	
	}
	
	@Override
	public void resize() { 
		noteSize = Settings.width/10;
		getPoints().clear();
		loaded = false;
	}

}
