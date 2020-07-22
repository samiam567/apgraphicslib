package apgraphicslib;

import java.util.ArrayList;

/**
* {@summary used to apply a one-time angularVelocity or angularAcceleration to an object}
* @author apun1
*/
public class ExpirableVectorAdd implements ExpirableVectorAddable {
	private Vector vector;
	private ArrayList<ExpirableVectorAddable> list; //this is the list of ExpirableVectorAddables that we will add and remove ourselves from
	private int times, indx;

	public ExpirableVectorAdd(Vector newAngV, int times, ArrayList<ExpirableVectorAddable> listToAddTo) {
		
		this.vector = newAngV;
		
		if (times <= 0) {
			Exception e = new Exception("Times must be greater than zero");
			e.printStackTrace();
		}else{
			this.times = times;
		}
		
		this.list = listToAddTo;
	
		this.indx = listToAddTo.size(); // multi-threading could sabotage this if another timer is added before this, but this way is blazing fast
		listToAddTo.add(this);
		
		
	}

	@Override
	public Vector tempStatAdd(Vector vecToAddTo, double frames) {
		times-= frames;
		
		if (times <= 0) {
			list.remove(indx); //remove this from the list of ExpirableVectorAddables so that we aren't added anymore
		}
		
		
		
		return vecToAddTo.tempStatAdd(vector.statMultiply(frames));
	}
}
