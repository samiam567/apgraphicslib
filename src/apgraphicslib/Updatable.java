package apgraphicslib;

public interface Updatable extends Physics_engine_compatible {
	public void Update(double frames);
	public void prePaintUpdate(); //called once per paint cycle
}
