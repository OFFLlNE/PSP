package application;

import java.util.ArrayList;
import java.util.Date;

public class ParkingSpotManager {
	public static ArrayList<ParkingSpot> parkingSpotsOrdered = new ArrayList<ParkingSpot>();
	
	/**
	 * Adds an empty parking spot to our database
	 * @param newSpot the parking spot
	 */
	public static void add(ParkingSpot newSpot){

		ArrayList<ParkingSpot> oldOrdered = new ArrayList<ParkingSpot>(parkingSpotsOrdered);
		parkingSpotsOrdered.clear();
		boolean done = false;
		
		for (ParkingSpot spot : oldOrdered) {			
			if(newSpot.getDistance() <= spot.getDistance() && done == false){
				parkingSpotsOrdered.add(newSpot);
				done = true;
			}
			parkingSpotsOrdered.add(spot);
		}
		if(done == false){
			parkingSpotsOrdered.add(newSpot);
			done = true;
		}
	}
	
	public static ParkingSpot getEmptyCheckers(){
		int pl = Algorithm.pl;
		int pk = Algorithm.pk;
		for (int i = 0; i<parkingSpotsOrdered.size(); i++) {
			ParkingSpot parkingSpot = parkingSpotsOrdered.get(i);
			if(!parkingSpot.occupied()){
				int curX = parkingSpot.getX();
				int curY = parkingSpot.getY();
				if(Algorithm.isMovable(curX+pl, curY) 
						&& Algorithm.isMovable(curX, curY+pk)
						&& Algorithm.isMovable(curX-pl, curY) 
						&& Algorithm.isMovable(curX, curY-pk)
						&& Algorithm.isMovable(curX-pl, curY-pk)
						&& Algorithm.isMovable(curX+pl, curY-pk)
						&& Algorithm.isMovable(curX-pl, curY+pk)
						&& Algorithm.isMovable(curX+pl, curY+pk)){
					
					return parkingSpot;
				}				
			}
		}
		return null;
	}
}
