package apgraphicslib;


public class Object_draw_update_thread extends Thread {

	public Object_draw objectDrawer;
	public int state = 0; //0 = stopped, 1 = running, 2 = paused
	public int status = 0;
	
	public Object_draw_update_thread(Object_draw objectDrawer1) {
		objectDrawer = objectDrawer1;
	}
	
	public void run() {
		while (state != 0) {
			if (state == 1) { //running
				status = 1;
				try {
					objectDrawer.doUpdate();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if (state == 2) { //paused
				status = 2;
				try {
					status = 2;
					sleep(1);
					Thread.yield();
				} catch (InterruptedException e) {}
			}		
		}
		
		Thread.yield();
	}
}
