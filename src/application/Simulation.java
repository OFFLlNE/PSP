package application;

public class Simulation implements Runnable {

	protected static int numberOfCarsIn = 10;
	protected static int numberOfCarsOut = 10;
	protected static int botSpeed = 8;
	protected static int numberOfbots = 4;
	public static boolean paused = false;
	public static boolean stopped = false;

	
	public void run(){
		while (!stopped){
			if (!paused){
				// TODO tick
				System.out.println("\nnumberOfCarsIn: "+numberOfCarsIn + "\nnumberOfCarsOut: " + numberOfCarsOut +  "\nbotSpeed: " +  botSpeed  + "\nnumberOfbots: " + numberOfbots +  "\npaused: " + paused + "\n\n");
			}
			// wait
    		try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
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
