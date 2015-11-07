package application;

public class Animation implements Runnable {

	protected static int numberOfCarsIn = 10;
	protected static int numberOfCarsOut = 10;
	protected static int botSpeed = 200;
	protected static int numberOfbots = 4;
	
	public static boolean paused = false;
	public static boolean stopped = false;
	
	public void run(){
		while (!stopped){
			if (!paused){
				// TODO tick

				
				
				
			}
			
			// time for each tick
    		try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	// will be changed. returns amount of time the movement is supposed to take. 
	public static int speedFormula(int distance){
		return distance*botSpeed;
	}
	
	
	public static void pause(){
		if (paused){
			paused = false;
		}
		else{
			paused = true;
		}
	}
	public static void kill(){
		System.out.println("killing thread");
		stopped = true;
	}
}
