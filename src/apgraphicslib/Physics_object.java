package apgraphicslib;

public abstract class Physics_object implements Physics_engine_compatible {
	private String name = "unNamed Physics_object";
	private Object_draw drawer;
	
	public Physics_object(Object_draw drawer) {
		this.drawer = drawer;
	}
	
	/**
	 * Gets the name of the object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the object
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Gets the Object_draw that this object was constructed with 
	 */
	public Object_draw getDrawer() {
		return drawer;
	}
}	
