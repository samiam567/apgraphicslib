package apgraphicslib;

/**
 * 
 * @author samiam567
 * This object is compatible with the engine.
 * This interface is for classes that are compatible with the engine but can't extend Physics_object
 */
public interface Physics_engine_compatible {
	public String getName();
	public void setName(String newName);
	public Object_draw getDrawer();
	

}
