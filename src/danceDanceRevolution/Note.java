package danceDanceRevolution;

import apgraphicslib.Physics_2DTexturedPolygon;
import apgraphicslib.Resizable;
import apgraphicslib.Settings;

public class Note extends Physics_2DTexturedPolygon implements Resizable {
	private static double NotePPSize =3;
	public static double noteSize = Settings.width/5;
	private String textureSrc = "noTexture";
	
	protected Song parentSong;
	
	private double noteSpeed, initY;
	protected boolean loaded = false;
	public Note(Song parent, double x, double y, double noteSpeed, String textureSrc) {
		super(parent.getDrawer(), x, y, NotePPSize);
		initY = y;
		this.parentSong = parent;
		this.textureSrc = textureSrc;
		this.noteSpeed = noteSpeed;
		addPoint(-noteSize/2, -noteSize/2);
		addPoint(-noteSize/2, noteSize/2);
		addPoint(noteSize/2, noteSize/2);
		addPoint(noteSize/2, -noteSize/2);
		
		run();
	}
	
	public double getInitY() {
		return initY;
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
	
	public void reposition() {
		// implemented by children
	}
	
	/**
	 * {@code since we will have tons of notes, override the rotation calculations}
	 */
	@Override
	public void Update(double frames) {		
		getCoordinates().add(speed.tempStatMultiply(frames));
		
	}
	
	@Override
	public void prePaintUpdate() {
		if (getY() < 0) {
			reposition();
		}
	}
	
	@Override
	public void resize() { 
		noteSize = Settings.width/10;
		getPoints().clear();
		loaded = false;
	}

}
