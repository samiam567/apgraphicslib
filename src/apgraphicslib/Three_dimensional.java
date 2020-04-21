package apgraphicslib;

public interface Three_dimensional extends Two_dimensional {
	@Override
	public Coordinate3D getCoordinates();
	
	public void setSize(double xSize, double ySize, double zSize);
	public double getZ();
	public double getZSize();
	void setPos(double x, double y, double z);

}
