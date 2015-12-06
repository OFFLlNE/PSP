package application;

public class Simulation implements Runnable {

	protected static boolean oneHourInMsChanged = false;
	protected static int oneHourInMsNew = 0;
	
	protected static int oneHourInMs = 12000;
	
	protected static int numberOfCarsIn = 20;
	protected static int numberOfCarsOut = 10;
	protected static double botSpeed = 1.6; //1,6
	protected static int numberOfbots = 1;
	
	public static boolean paused = false;

	//public static ArrayList<String> tasks = new ArrayList<String>();
	
	public void run(){
		
		while(!Algorithm.stopped){

			if(oneHourInMsChanged){
				oneHourInMs = oneHourInMsNew;
				oneHourInMsChanged=false;
			}
			System.out.println("-----------------------------------------------------------");
			System.out.println("New hour has begun");
			Main.updateHourLabel();
			System.out.println("Undone tasks in buffer: "+Algorithm.tasks.size());
			System.out.println("-----------------------------------------------------------");
			
			int nrOfComms = numberOfCarsIn+numberOfCarsOut;
			int nrOfCarsIn = numberOfCarsIn;
			int nrOfCarsOut = numberOfCarsOut;
			
			int delays[] = new int[nrOfComms];
			
			for(int i=0;i<delays.length-1;i=i+2){
				int def = oneHourInMs/(numberOfCarsIn+numberOfCarsOut);
				int randomness = (int)(Math.random() * ((def - 0) + 1)) + 0;
				delays[i] = def+randomness;
				delays[i+1] = def-randomness;
			}
			if(delays.length%2!=0){
				delays[delays.length-1] = oneHourInMs/(numberOfCarsIn+numberOfCarsOut);
			}
			
			if(delays.length <= 0){
				try {
					Thread.sleep(oneHourInMs);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//System.out.println(Algorithm.tasks);
			for(int j = 0; j<nrOfComms; j++){


				//System.out.println(Algorithm.tasks);
				int whichComm = (int)(Math.random() * ((2 - 1) + 1)) + 1;
				//System.out.println("rng:"+whichComm);
				if(Algorithm.tasks.size()>100){
					System.out.println("Too many commands.");
				}
				else if (whichComm == 1 && nrOfCarsIn>0){
					Algorithm.tasks.add("a1,Audi R1,42424356,0");
					nrOfCarsIn--;
					
				}
				else if (!ParkingSpotManager.parkingSpotsOccupied.isEmpty() && nrOfCarsOut>0){
					//System.out.println("retrieve");
					boolean foundSuitable = false;
					int runs = 0;
					
					while(!foundSuitable){
						
						int newRemoveComm = (int) ParkingSpotManager.parkingSpotsOccupied.get((int)(Math.random() * ((ParkingSpotManager.parkingSpotsOccupied.size() - 1) + 1)) + 0).getID();
						if(!Algorithm.tasks.contains(newRemoveComm+"")){
							foundSuitable = true;
							Algorithm.tasks.add(""+newRemoveComm);
						}
						else if (runs>5){
							for(int jj = 0; jj<ParkingSpotManager.parkingSpotsOccupied.size(); jj++){
								if(!Algorithm.tasks.contains(ParkingSpotManager.parkingSpotsOccupied.get(jj).getID()+"")){
									Algorithm.tasks.add(""+ParkingSpotManager.parkingSpotsOccupied.get(jj).getID() );
									jj=ParkingSpotManager.parkingSpotsOccupied.size();
								}
							}
							foundSuitable = true;
						}
						runs++;
					}
					nrOfCarsOut--;
				}
				else if(nrOfCarsIn>0){
					Algorithm.tasks.add("a1,Audi R1,42424356,0");
					nrOfCarsIn--;
				}
				else{
					//System.out.println("-----------------------------------------------------------");
					System.out.println("!!!!No commands.");
					//System.out.println("in: "+nrOfCarsIn);
					//System.out.println("out: "+nrOfCarsOut);
					//System.out.println("parked: "+ParkingSpotManager.parkingSpotsOccupied.size());
					//System.out.println("-----------------------------------------------------------");
					
					
				}
				
				// wait
				try {
					//System.out.println("Waiting "+delays[j] + " before next car arrival/retrival.");

					Thread.sleep(delays[j]);
					Main.updateMinuteLabel((int)((delays[j]*60)/oneHourInMs));
					
				}
					
				catch (Exception e) {
					//e.printStackTrace();
					System.out.println(e + " in simulation");
					System.out.println("in: "+numberOfCarsIn+  "out:"+numberOfCarsOut);
				}
				
			}			
			

		}
				
	}
	
	// will be changed. returns amount of time the movement is supposed to take. 
	public static double speedFormula(int distance){
		return distance/(botSpeed/(oneHourInMs/3600));
		
	}
	
	public static boolean validateSpeed(double speed){
    	if( 1/(speed/(oneHourInMs/3600)) >0.1){
    		return true;
    	}
    	else{
        	return false;
    	}
	}
	
	public static double maxSpeed(){
		double second = oneHourInMs/3600;
    	return ((1/0.1)*second)-1;
	}
	
	public static void pause(){
		if (paused){
			paused = false;
		}
		else{
			paused = true;
		}
	}

	public static boolean stopped = false;
	public static void kill(){
		System.out.println("killing thread");
		stopped = true;
	}
}
