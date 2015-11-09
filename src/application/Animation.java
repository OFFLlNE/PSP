package application;

public class Animation implements Runnable {

	protected static int numberOfCarsIn = 10;
	protected static int numberOfCarsOut = 10;
	protected static int botSpeed = 200; //1,6
	protected static int numberOfbots = 4;
	
	public static boolean paused = false;
	public static boolean stopped = false;
	
	public void run(){
		Bot r2 = Main.makeBot(20,1);
		r2.moveDown(1);
		r2.moveLeft(3);
		r2.moveDown(1);
		
		boolean kas = true;
		while(kas){
		
			if (!r2.isBusy){
				Algorithm.colorOccupied(r2.getRobotX(), r2.getRobotY());
				kas=false;
			}
			System.out.println("tere");

    		try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		r2.moveDown(1);
		
		
		r2.moveDown(1);
		r2.moveRight(3);
		r2.moveDown(1);
		
		kas = true;
		while(kas){
		
			if (!r2.isBusy){
				Algorithm.colorOccupied((int)r2.getRobotX(), (int)r2.getRobotY());
				kas=false;
			}
			System.out.println("tere");

    		try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		r2.moveDown(1);
		
		
		
//		while (!stopped){
//			if (!paused){
//				// TODO tick
//
//				
//			}
//			
//			// time for each tick
//    		try {
//				Thread.currentThread().sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//		}
				
				
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
