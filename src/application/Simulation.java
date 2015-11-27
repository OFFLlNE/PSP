package application;

import java.io.File;
import java.util.ArrayList;

public class Simulation implements Runnable {

	protected static int oneHourInMs = 12000;
	
	protected static int numberOfCarsIn = 10;
	protected static int numberOfCarsOut = 10;
	protected static double botSpeed = 0.1; //1,6
	protected static int numberOfbots = 1;
	
	public static boolean paused = false;

	//public static ArrayList<String> tasks = new ArrayList<String>();
	
	public void run(){
		
		while(!Algorithm.stopped){

			System.out.println("-----------------------------------------------------------");
			System.out.println("New hour has begun");
			System.out.println("tasks: "+Algorithm.tasks);
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
			
			
			//System.out.println(Algorithm.tasks);
			for(int j = 0; j<nrOfComms; j++){


				//System.out.println(Algorithm.tasks);
				int whichComm = (int)(Math.random() * ((2 - 1) + 1)) + 1;
				//System.out.println(whichComm);
				if (whichComm == 1 && nrOfCarsIn>0){
					Algorithm.tasks.add("a1,Audi R1,42424356");
					nrOfCarsIn--;
					
				}
				else if (!ParkingSpotManager.parkingSpotsOccupied.isEmpty() && nrOfCarsOut>0){
					boolean foundSuitable = false;
					int runs = 0;
					
					while(!foundSuitable){
						
						int newRemoveComm = ParkingSpotManager.parkingSpotsOccupied.get((int)(Math.random() * ((ParkingSpotManager.parkingSpotsOccupied.size() - 1) + 1)) + 0).getID();
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
					Algorithm.tasks.add("a1,Audi R1,42424356");
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

	public static boolean stopped = false;
	public static void kill(){
		System.out.println("killing thread");
		stopped = true;
	}
}
